package quarano.department.web;

import static org.apache.commons.lang3.StringUtils.*;
import static quarano.department.web.TrackedCaseCsvRepresentations.SormasCsvGroup.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.core.web.MapperWrapper;
import quarano.department.CaseType;
import quarano.department.Comment;
import quarano.department.ContactChaser;
import quarano.department.ContactChaser.Contact;
import quarano.department.TestResult;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.Status;
import quarano.department.rki.HealthDepartments;
import quarano.department.rki.HealthDepartments.HealthDepartment;
import quarano.tracking.Address;
import quarano.tracking.Quarantine;
import quarano.tracking.TrackedPerson;
import quarano.tracking.ZipCode;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import com.opencsv.CSVReader;
import com.opencsv.ICSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvBadConverterException;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

/**
 * @author Jens Kutzsche
 * @author Oliver Drotbohm
 * @since 1.4
 */
@Component
@Slf4j
class TrackedCaseCsvRepresentations {

	private static final String LAST_NAME = "LAST NAME";
	private static final String FIRST_NAME = "FIRST NAME";
	private static final String DATE_OF_BIRTH = "DATE OF BIRTH";
	private static final String EMAIL = "EMAIL ADDRESS";
	private static final String PHONE = "PHONE NUMBER";
	private static final String MOBILE = "MOBILE NUMBER";
	private static final String CITY = "CITY";
	private static final String ZIP_CODE = "ZIP CODE";
	private static final String STREET = "STREET";
	private static final String HOUSE_NUMBER = "HOUSE NUMBER";
	private static final String QUARANTINE_FROM = "QUARANTINE FROM";
	private static final String QUARANTINE_TO = "QUARANTINE TO";
	private static final String QUARANTINE_CHANGED = "QUARANTINE LAST MODIFIED";
	private static final String TYPE = "TYPE";
	private static final String STATUS = "STATUS";
	private static final String LOCALE = "LOCALE";
	private static final String TEST_DATE = "TEST DATE";
	private static final String INFECTED = "INFECTED";
	private static final String CREATED = "CREATED";
	private static final String LAST_MODIFIED = "LAST MODIFIED";
	private static final String CASE_ID = "CASE ID";
	private static final String PERSON_ID = "PERSON ID";
	private static final String RKI_CODE = "RKI CODE";
	private static final String HEALH_DEPARTMENT = "HEALH DEPARTMENT";
	private static final String HEALH_DEPARTMENT_MAIL = "HEALH DEPARTMENT EMAIL";
	private static final String COMMENTS = "COMMENTS";
	static final String LAST_CONTACT_DATE = "DATE LAST CONTACT TO INDEXCASE";
	static final String FIRST_NAME_ORIGIN = "FIRST NAME OF INDEXCASE";
	static final String LAST_NAME_ORIGIN = "LAST NAME OF INDEXCASE";
	static final String DATE_OF_BIRTH_ORIGIN = "DATE OF BIRTH OF INDEXCASE";
	static final String ZIP_CODE_ORIGIN = "ZIP CODE OF INDEXCASE";

	static final List<String> QUARANTINE_CSV_ORDER = List.of(ZIP_CODE, CITY, STREET, HOUSE_NUMBER, LAST_NAME,
			FIRST_NAME, DATE_OF_BIRTH, EMAIL, PHONE, MOBILE, LOCALE, QUARANTINE_FROM, QUARANTINE_TO,
			QUARANTINE_CHANGED, LAST_CONTACT_DATE, TYPE);
	static final List<String> CASE_TRANSFER_CSV_ORDER = List.of(TYPE, STATUS, LAST_NAME, FIRST_NAME, DATE_OF_BIRTH, CITY,
			ZIP_CODE, STREET, HOUSE_NUMBER, EMAIL, PHONE, MOBILE, LOCALE, QUARANTINE_FROM, QUARANTINE_TO,
			TEST_DATE, INFECTED, COMMENTS, LAST_CONTACT_DATE, LAST_NAME_ORIGIN, FIRST_NAME_ORIGIN, DATE_OF_BIRTH_ORIGIN,
			ZIP_CODE_ORIGIN, QUARANTINE_CHANGED, CREATED, LAST_MODIFIED, HEALH_DEPARTMENT,
			HEALH_DEPARTMENT_MAIL, RKI_CODE, CASE_ID, PERSON_ID);

	private static final String COMMENT_SEPERATOR = " ### ";

	private final ObjectProvider<ICSVWriter> csvWriterProvider;
	private final ObjectProvider<StatefulBeanToCsv<TrackedCaseCsv>> beanToCsvProvider;
	private final ObjectProvider<Comparator<String>> columnOrderComperator;
	private final ContactChaser contactChaser;
	private final MapperWrapper mapper;
	private final HealthDepartments healthDepartments;
	private final MessageSourceAccessor messages;

	TrackedCaseCsvRepresentations(@NonNull ObjectProvider<ICSVWriter> csvWriterProvider,
			@Qualifier("givenMappingStrategyAndCsvWriter") @NonNull ObjectProvider<StatefulBeanToCsv<TrackedCaseCsv>> beanToCsvProvider,
			@NonNull ObjectProvider<Comparator<String>> columnOrderComperator,
			@NonNull ContactChaser contactChaser,
			@NonNull MapperWrapper mapper,
			@NonNull HealthDepartments healthDepartments,
			@NonNull MessageSourceAccessor messages) {

		super();

		this.csvWriterProvider = csvWriterProvider;
		this.beanToCsvProvider = beanToCsvProvider;
		this.columnOrderComperator = columnOrderComperator;
		this.contactChaser = contactChaser;
		this.mapper = mapper;
		this.healthDepartments = healthDepartments;
		this.messages = messages;
	}

	public void writeCaseTransferCsv(Writer writer, Stream<TrackedCase> cases, boolean withOriginCase) {

		var mappingStrategy = createAnnotationMarkerMapping(writer, CaseTransfer.class, false,
				CASE_TRANSFER_CSV_ORDER);

		if (!withOriginCase) {

			MultiValuedMap<Class<?>, Field> fields = new ArrayListValuedHashMap<>();
			var clazz = TrackedCaseCsv.class;

			try {

				fields.put(clazz, clazz.getDeclaredField("originCaseLastContactDate"));
				fields.put(clazz, clazz.getDeclaredField("originCaseFirstName"));
				fields.put(clazz, clazz.getDeclaredField("originCaseLastName"));
				fields.put(clazz, clazz.getDeclaredField("originCaseDateOfBirth"));
				fields.put(clazz, clazz.getDeclaredField("originCaseZipCode"));

			} catch (NoSuchFieldException | SecurityException e) {
				throw new RuntimeException(e);
			}

			mappingStrategy.ignoreFields(fields);
		}

		writeCsvWithAnnotationMarkerMapping(writer, cases, mappingStrategy);
	}

	public void writeQuarantineOrderCsv(Writer writer, Stream<TrackedCase> cases) {

		var mappingStrategy = createAnnotationMarkerMapping(writer, QuarantineOrder.class, true, QUARANTINE_CSV_ORDER);

		writeCsvWithAnnotationMarkerMapping(writer, cases, mappingStrategy);
	}

	public void writeSormasCsv(Writer writer, Stream<TrackedCase> cases, CaseType type) {

		var csvWriter = csvWriterProvider.getObject(writer);

		var mappingStrategy = new SormasMappingStrategy(csvWriter, type);

		var beanToCsv = beanToCsvProvider.getObject(csvWriter, mappingStrategy);

		var csvCases = cases.map(it -> new TrackedCaseCsv(it, contactChaser, healthDepartments, messages));

		writeCsv(csvCases, beanToCsv);
	}

	private AnnotationMarkerMappingStrategy<TrackedCaseCsv> createAnnotationMarkerMapping(Writer writer,
			Class<? extends Annotation> annotation, boolean humanReadableExport, List<String> columnOrderList) {

		var mappingStrategy = new AnnotationMarkerMappingStrategy<>(annotation, TrackedCaseCsv.class, humanReadableExport);
		mappingStrategy.setColumnOrderOnWrite(columnOrderComperator.getObject(columnOrderList));

		return mappingStrategy;
	}

	private void writeCsvWithAnnotationMarkerMapping(Writer writer, Stream<TrackedCase> cases,
			MappingStrategy<TrackedCaseCsv> mappingStrategy) {

		var csvWriter = csvWriterProvider.getObject(writer);
		var beanToCsv = beanToCsvProvider.getObject(csvWriter, mappingStrategy);

		var csvCases = cases
				.map(it -> new TrackedCaseCsv(it, contactChaser, healthDepartments, messages));

		writeCsv(csvCases, beanToCsv);
	}

	private void writeCsv(Stream<TrackedCaseCsv> objects, StatefulBeanToCsv<TrackedCaseCsv> beanToCsv) {

		try {

			beanToCsv.write(objects);

		} catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {

			log.error("Error mapping Bean to CSV", e);
			throw new RuntimeException(e);
		}
	}

	@Getter(AccessLevel.PACKAGE)
	public static class TrackedCaseCsv {

		@SormasCsv(export = "person.lastName", group = Person, importDe = "Nachname", importEn = "Last name")
		@CaseTransfer
		@QuarantineOrder
		@CsvBindByName(column = LAST_NAME) //
		private String lastName;

		@SormasCsv(export = "person.firstName", group = Person, importDe = "Vorname", importEn = "First name")
		@CaseTransfer
		@QuarantineOrder
		@CsvBindByName(column = FIRST_NAME) //
		private String firstName;

		@SormasCsv(importDe = "Geburtsdatum (Jahr / Monat / Tag)", importEn = "Date of birth (year / month / day)")
		@CaseTransfer
		@QuarantineOrder
		@CsvDate
		@CsvBindByName(column = DATE_OF_BIRTH) //
		private LocalDate dateOfBirth;

		@SormasCsv(export = "person.birthdateDD", group = Person)
		private Integer dayOfBirth;

		@SormasCsv(export = "person.birthdateMM", group = Person)
		private Integer monthOfBirth;

		@SormasCsv(export = "person.birthdateYYYY", group = Person)
		private Integer yearOfBirth;

		@SormasCsv(export = "person.emailAddress", group = Person)
		@CaseTransfer
		@QuarantineOrder
		@CsvBindByName(column = EMAIL) //
		private String email;

		@SormasCsv(export = "person.phone", group = Person)
		@CaseTransfer
		@QuarantineOrder
		@CsvBindByName(column = PHONE) //
		private String phone;

		@CaseTransfer
		@QuarantineOrder
		@CsvBindByName(column = MOBILE) // don't exists in Sormas
		private String mobile;

		@SormasCsv(export = "person.address.city", group = Location, importDe = "Stadt", importEn = "City")
		@CaseTransfer
		@QuarantineOrder
		@CsvBindByName(column = CITY) //
		private String city;

		@SormasCsv(export = "person.address.postalCode", group = Location, importDe = "Postleitzahl",
				importEn = "Postal code")
		@CaseTransfer
		@QuarantineOrder
		@CsvBindByName(column = ZIP_CODE) //
		private String zipCode;

		@SormasCsv(export = "person.address.street", group = Location, importDe = "Straße", importEn = "Street")
		@CaseTransfer
		@QuarantineOrder
		@CsvBindByName(column = STREET) //
		private String street;

		@SormasCsv(export = "person.address.houseNumber", group = Location, importDe = "Hausnummer",
				importEn = "House number")
		@CaseTransfer
		@QuarantineOrder
		@CsvBindByName(column = HOUSE_NUMBER) //
		private String houseNumber;

		@SormasCsv(export = "quarantineFrom", group = CaseData, importDe = "Beginn der Isolation (dd.MM.yyyy)",
				importEn = "Quarantine start (M/d/yyyy)")
		@CaseTransfer
		@QuarantineOrder
		@CsvDate
		@CsvBindByName(column = QUARANTINE_FROM) //
		private LocalDate quarantineFrom;

		@SormasCsv(export = "quarantineTo", group = CaseData, importDe = "Ende der Isolation (dd.MM.yyyy)",
				importEn = "Quarantine end (M/d/yyyy)")
		@CaseTransfer
		@QuarantineOrder
		@CsvDate
		@CsvBindByName(column = QUARANTINE_TO) //
		private LocalDate quarantineTo;

		@CaseTransfer
		@QuarantineOrder
		@CsvDate
		@CsvBindByName(column = QUARANTINE_CHANGED) //
		private LocalDateTime quarantineChanged;

		@SormasCsv(export = "quarantine", group = CaseData, importDe = "Isolation", importEn = "Quarantine")
		private String isolation;

		@CaseTransfer
		@QuarantineOrder
		@CsvBindByName(column = TYPE) //
		private CaseType type;

		@CaseTransfer
		@QuarantineOrder
		@CsvBindByName(column = LOCALE) //
		private Locale locale;

		@SormasCsv(export = "externalID", group = CaseData, importDe = "Externe ID", importEn = "External ID")
		@CaseTransfer
		@CsvBindByName(column = CASE_ID)
		private String caseId;

		@CaseTransfer
		@CsvBindByName(column = STATUS)
		private Status status;

		@SormasCsv(export = "person.externalId", group = Person)
		@CaseTransfer
		@CsvBindByName(column = PERSON_ID)
		private String personId;

		@SormasCsv(export = "sampleDateTime", group = Sample, ignoreForContacts = true,
				importDe = "Neueste Probe Datum Zeit (dd.MM.yyyy)", importEn = "Latest sample date time (M/d/yyyy)")
		@CaseTransfer
		@CsvDate
		@CsvBindByName(column = TEST_DATE)
		private LocalDate testDate;

		@SormasCsv(export = "pathogenTestResult", group = Sample, ignoreForContacts = true,
				importDe = "Neueste Probe finales Laborergebnis", importEn = "Latest sample final laboratory result",
				converter = ConvertNegativePositiveBoolean.class)
		@CaseTransfer
		@CsvBindByName(column = INFECTED)
		private Boolean infected;

		@SormasCsv(export = "sampleMaterial", group = Sample, ignoreForContacts = true)
		private String testMaterial = "OTHER";

		@SormasCsv(export = "samplePurpose", group = Sample, ignoreForContacts = true)
		private String testPurpose = "EXTERNAL";

		@SormasCsv(export = "lab", group = Sample, ignoreForContacts = true)
		private String lab = "NO_FACILITY";

		@SormasCsv(export = "reportDate", group = CaseData, exportContact = "reportDateTime",
				importDe = "Meldedatum (dd.MM.yyyy)", importEn = "Date of report (M/d/yyyy)")
		@CaseTransfer
		@CsvDate
		@CsvBindByName(column = CREATED)
		private LocalDateTime created;

		@CaseTransfer
		@CsvDate
		@CsvBindByName(column = LAST_MODIFIED)
		private LocalDateTime lastModified;

		@SormasCsv(exportContact = "caseIdExternalSystem", importDe = "Indexfall-ID", importEn = "Source case ID")
		private String originCaseSormasId;

		@SormasCsv(exportContact = "lastContactDate", importDe = "Datum des letzten Kontakts (dd.MM.yyyy)",
				importEn = "Date of last contact (M/d/yyyy)")
		@QuarantineOrder
		@CaseTransfer
		@CsvDate
		@CsvBindByName(column = LAST_CONTACT_DATE)
		private LocalDate originCaseLastContactDate;

		@CaseTransfer
		@CsvBindByName(column = FIRST_NAME_ORIGIN)
		private String originCaseFirstName;

		@CaseTransfer
		@CsvBindByName(column = LAST_NAME_ORIGIN)
		private String originCaseLastName;

		@CaseTransfer
		@CsvBindByName(column = DATE_OF_BIRTH_ORIGIN)
		private LocalDate originCaseDateOfBirth;

		@CaseTransfer
		@CsvBindByName(column = ZIP_CODE_ORIGIN)
		private String originCaseZipCode;

		@SormasCsv(importDe = "Fall-ID", importEn = "Case ID")
		private String sormasCaseId;

		@SormasCsv(export = "disease", group = CaseData)
		private String disease = "CORONAVIRUS";

		@SormasCsv(export = "region", group = CaseData)
		private String region;

		@SormasCsv(export = "district", group = CaseData)
		private String district;

		@SormasCsv(export = "healthFacility", group = CaseData, ignoreForContacts = true)
		private String healthFacility = "NO_FACILITY";

		@SormasCsv(export = "additionalDetails", group = CaseData, importDe = "Kommentare", importEn = "Comments",
				converter = CommentConverter.class)
		@CaseTransfer
		@CsvBindAndSplitByName(column = COMMENTS, elementType = Comment.class, splitOn = COMMENT_SEPERATOR,
				writeDelimiter = COMMENT_SEPERATOR,				converter = CommentConverter.class)
		private List<Comment> comments;

		@SormasCsv(export = "person.generalPractitionerDetails", group = Person)
		private String familyDoctor;

		@SormasCsv(export = "symptoms.onsetDate", group = Symptoms, ignoreForContacts = true)
		private LocalDate dayOfFirstSymptoms;

		@SormasCsv(export = "symptoms.onsetSymptom", group = Symptoms, ignoreForContacts = true)
		private String firstSymptoms;

		@SormasCsv(export = "epiData.directContactConfirmedCase", group = EpiData,
				importDe = "Direkter Kontakt mit einem bestätigten Fall", importEn = "Direct contact with a confirmed case",
				converter = ConvertYesNoBoolean.class)
		private boolean directContactConfirmedCase = true;

		@SormasCsv(export = "epiData.directContactProbableCase", group = EpiData,
				importDe = "Direkter Kontakt mit einem wahrscheinlichen oder bestätigten Fall",
				importEn = "Direct contact with a probable or confirmed case", converter = ConvertYesNoBoolean.class)
		private boolean directContactProbableCase = true;

		@SormasCsv(export = "epiData.closeContactProbableCase", group = EpiData, converter = ConvertYesNoBoolean.class)
		private boolean closeContactProbableCase = true;

		@SormasCsv(exportContact = "tracingApp")
		private String tracingApp = "OTHER";

		@SormasCsv(exportContact = "tracingAppDetails")
		private String tracingAppDetails = "Quarano";

		@CaseTransfer
		@CsvBindByName(column = RKI_CODE)
		private String rkiCode;

		@CaseTransfer
		@CsvBindByName(column = HEALH_DEPARTMENT)
		private String healthDepartment;

		@CaseTransfer
		@CsvBindByName(column = HEALH_DEPARTMENT_MAIL)
		private String healthDepartmentMail;

		public TrackedCaseCsv() {}

		public TrackedCaseCsv(TrackedCase trackedCase, ContactChaser contactChaser, HealthDepartments healthDepartments,
				MessageSourceAccessor messages) {

			setCaseData(trackedCase, contactChaser, messages);
			setPersonData(trackedCase.getTrackedPerson());
			setQuarantineData(trackedCase.getQuarantine(), trackedCase.getQuarantineLastModified());
			setTestData(trackedCase.getTestResult());
			setDepartmentData(trackedCase);
			setRkiData(healthDepartments);

			familyDoctor = trackedCase.getQuestionnaire() != null ? trackedCase.getQuestionnaire().getFamilyDoctor() : null;

			dayOfFirstSymptoms = trackedCase.getQuestionnaire() != null
					? trackedCase.getQuestionnaire().getDayOfFirstSymptoms()
					: null;
			firstSymptoms = trackedCase.getQuestionnaire() != null && trackedCase.getQuestionnaire().getSymptoms() != null
					? trackedCase.getQuestionnaire().getSymptoms().stream()
							.map(it -> messages.getMessage("symptom." + it.getId()))
							.collect(Collectors.joining(", "))
					: null;
		}

		private void setCaseData(TrackedCase trackedCase, ContactChaser contactChaser, MessageSourceAccessor messages) {

			type = trackedCase.getType();

			status = trackedCase.getStatus();
			caseId = trackedCase.getId().toString();
			comments = trackedCase.getComments();
			sormasCaseId = trackedCase.getSormasCaseId() != null ? trackedCase.getSormasCaseId().toString() : null;

			var lastContact = contactChaser.findLastIndexCaseContactFor(trackedCase);
			originCaseLastContactDate = lastContact.map(Contact::getContactAt).orElse(null);
			originCaseFirstName = lastContact.map(Contact::getPerson).map(TrackedPerson::getFirstName).orElse(null);
			originCaseLastName = lastContact.map(Contact::getPerson).map(TrackedPerson::getLastName).orElse(null);
			originCaseDateOfBirth = lastContact.map(Contact::getPerson).map(TrackedPerson::getDateOfBirth).orElse(null);
			originCaseZipCode = lastContact.map(Contact::getPerson).map(TrackedPerson::getAddress).map(Address::getZipCode)
					.map(ZipCode::toString).orElse(null);
			originCaseSormasId = lastContact.map(Contact::getTrackedCase)
					.map(TrackedCase::getSormasCaseId)
					.map(Object::toString)
					.orElse(null);

			var metadata = trackedCase.getMetadata();
			created = metadata.getCreated();
			lastModified = metadata.getLastModified();
		}

		private void setPersonData(TrackedPerson trackedPerson) {

			personId = trackedPerson.getId().toString();

			lastName = trackedPerson.getLastName();
			firstName = trackedPerson.getFirstName();

			email = Objects.toString(trackedPerson.getEmailAddress(), null);
			phone = Objects.toString(trackedPerson.getPhoneNumber(), null);
			mobile = Objects.toString(trackedPerson.getMobilePhoneNumber(), null);

			dateOfBirth = trackedPerson.getDateOfBirth();

			if (dateOfBirth != null) {
				dayOfBirth = dateOfBirth.getDayOfMonth();
				monthOfBirth = dateOfBirth.getMonthValue();
				yearOfBirth = dateOfBirth.getYear();
			}

			var address = trackedPerson.getAddress();
			city = address == null ? null : address.getCity();
			zipCode = address == null ? null : Objects.toString(address.getZipCode(), null);
			street = address == null ? null : address.getStreet();
			houseNumber = address == null ? null : Objects.toString(address.getHouseNumber(), null);

			locale = trackedPerson.getLocale();
		}

		private void setQuarantineData(Quarantine quarantine, LocalDateTime quarantineLastModified) {

			quarantineFrom = quarantine == null ? null : quarantine.getFrom();
			quarantineTo = quarantine == null ? null : quarantine.getTo();
			isolation = quarantine == null ? null : "HOME";

			quarantineChanged = quarantineLastModified == null ? null : quarantineLastModified;
		}

		private void setTestData(TestResult testResult) {

			testDate = testResult == null ? null : testResult.getTestDate();
			infected = testResult == null ? null : testResult.isInfected();
		}

		private void setDepartmentData(TrackedCase trackedCase) {

			region = trackedCase.getDepartment().getFederalState();
			district = trackedCase.getDepartment().getDistrict();
		}

		private void setRkiData(HealthDepartments healthDepartments) {

			if (zipCode != null) {

				var department = healthDepartments.findDepartmentWithExact(zipCode);

				rkiCode = department.map(HealthDepartment::getCode).orElse(null);
				healthDepartment = department.map(it -> it.getName() + " - " + it.getDepartment()).orElse(null);
				healthDepartmentMail = department.map(it -> it.getCovid19EMail() != null ? it.getCovid19EMail() : it.getEmail())
						.map(it -> it.toString()).orElse(null);
			}
		}
	}

	/**
	 * Marker to export fields into quarantine order CSVs.
	 * 
	 * @author Jens Kutzsche
	 */
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface QuarantineOrder {}

	/**
	 * Marker to export fields into case transfer CSVs.
	 * 
	 * @author Jens Kutzsche
	 */
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface CaseTransfer {}

	/**
	 * Marker and configuration to export fields into Sormas CSVs.
	 */
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface SormasCsv {
		SormasCsvGroup group() default NA;

		String export() default "";

		String exportContact() default "";

		boolean ignoreForContacts() default false;

		String importDe() default "";

		String importEn() default "";

		Class<? extends CsvConverter>[] converter() default {};
	}

	public static enum SormasCsvGroup {
		NA, CaseData, Person, Location, Sample, EpiData, Symptoms
	}

	static class AnnotationMarkerMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {

		private final @NonNull Class<? extends Annotation> annotation;
		private final boolean humanReadableExport;

		private AnnotationMarkerMappingStrategy(@NonNull Class<? extends Annotation> annotation,
				@NonNull Class<T> type, boolean humanReadableExport) {

			this.annotation = annotation;
			this.humanReadableExport = humanReadableExport;

			setType(type);
			setErrorLocale(LocaleContextHolder.getLocale());
		}

		/**
		 * Filters all posible headers to the annotated ones for CSV export.
		 */
		@Override
		public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {

			var headers = super.generateHeader(bean);

			headers = Arrays.stream(headers)
					.filter(this::hasAnnotation)
					.toArray(String[]::new);

			headerIndex.initializeHeaderIndex(headers);

			return headerIndex.getHeaderIndex();
		};

		@Override
		protected CsvConverter determineConverter(Field field, Class<?> elementType, String locale, String writeLocale,
				Class<? extends AbstractCsvConverter> customConverter) throws CsvBadConverterException {

			CsvConverter determineConverter = super.determineConverter(field, elementType, locale, writeLocale,
					customConverter);

			if (determineConverter instanceof ConverterDate) {

				CsvDate annotation = field.getAnnotation(CsvDate.class);

				var format = humanReadableExport ? "dd.MM.yyyy[ HH:mm]" : "yyyyMMdd['T'HHmmss]";

				var readChrono = annotation.chronology();
				var writeChrono = annotation.writeChronologyEqualsReadChronology()
						? readChrono
						: annotation.writeChronology();

				determineConverter = new ConverterDate(elementType, locale, writeLocale,
						errorLocale, format, format, readChrono, writeChrono);
			}

			return determineConverter;
		}

		private boolean hasAnnotation(String fieldName) {
			return fieldMap.get(fieldName).getField().isAnnotationPresent(annotation);
		}
	}

	@RequiredArgsConstructor
	static class SormasMappingStrategy extends HeaderNameBaseMappingStrategy<TrackedCaseCsv> {

		private final static Map<Column, String> importDateFormats = Map.of(Column.ImportDe, "d.M.yyyy",
				Column.ImportEn, "M/d/yyyy");

		private final ICSVWriter csvWriter;

		private final CaseType caseType;

		private Table<String, Column, String> table;

		private Map<String, String> columnMapping = new HashMap<>();

		private Column importColumn = Column.ImportDe;

		{
			setType(TrackedCaseCsv.class);
			setErrorLocale(LocaleContextHolder.getLocale());
		}

		@Override
		public void captureHeader(CSVReader reader) throws IOException, CsvRequiredFieldEmptyException {

			super.captureHeader(reader);

			var foundHeaders = headerIndex.getHeaderIndex();

			var importColumns = EnumSet.of(Column.ImportDe, Column.ImportEn,
					caseType == CaseType.INDEX ? Column.Export : Column.ExportContacts);

			// determine defined column headers in the right language and thus the used language itself
			importColumn = importColumns.stream()
					.filter(it -> Arrays.stream(foundHeaders).anyMatch(table.column(it)::containsValue))
					.findAny()
					.orElseThrow();

			// transform and set column mapping
			columnMapping = table.column(importColumn).entrySet().stream()
					.filter(it -> it.getValue() != null)
					.collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
		}

		@Override
		public String[] generateHeader(TrackedCaseCsv bean) throws CsvRequiredFieldEmptyException {

			var headers = super.generateHeader(bean); // because it is required by the JavaDoc

			// write Sormas group header for index CSV
			if (caseType == CaseType.INDEX) {

				var groupHeaders = Arrays.stream(headers)
						.map(it -> table.get(it, Column.Group))
						.filter(Objects::nonNull)
						.toArray(String[]::new);

				csvWriter.writeNext(groupHeaders);
			}

			var exports = caseType == CaseType.INDEX
					? table.column(Column.Export)
					: table.column(Column.ExportContacts);

			headers = Arrays.stream(headers)
					.map(exports::get)
					.filter(Objects::nonNull)
					.toArray(String[]::new);

			headerIndex.initializeHeaderIndex(headers);

			columnMapping = exports.entrySet().stream()
					.filter(it -> it.getValue() != null)
					.collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

			return headers;
		}

		@Override
		protected void loadFieldMap() {

			super.loadFieldMap();

			String[] fieldNames = null;
			try {
				fieldNames = fieldMap.generateHeader(null);
			} catch (CsvRequiredFieldEmptyException e) {
				// Occurs only when using @CsvBindAndJoinByName, which is impossible with this mapping strategy.
			}

			table = ArrayTable.create(Arrays.asList(fieldNames),
					Arrays.asList(Column.values()));

			Arrays.stream(fieldNames).forEach(this::fillTableFrom);
		}

		@Override
		protected CsvConverter determineConverter(Field field, Class<?> elementType, String locale, String writeLocale,
				Class<? extends AbstractCsvConverter> customConverter) throws CsvBadConverterException {

			var converter = super.determineConverter(field, elementType, locale, writeLocale, customConverter);

			var sormasCsv = field.getAnnotation(SormasCsv.class);

			if (sormasCsv != null) {
				if (converter instanceof ConverterDate) {

					var readFormat = importDateFormats.get(importColumn);
					readFormat += LocalDateTime.class.isAssignableFrom(elementType) ? " HH:mm" : "";

					converter = new ConverterDate(elementType, locale, writeLocale,
							errorLocale, readFormat, "dd/MM/yyyy", "ISO", "ISO") {

						@Override
						public Object convertToRead(String value) throws CsvDataTypeMismatchException {

							if (value != null) {
								value += LocalDateTime.class.isAssignableFrom(elementType) ? " 00:00" : "";
							}

							return super.convertToRead(value);
						}
					};

				} else if (sormasCsv.converter().length > 0) {

					var converterClass = sormasCsv.converter()[0];

					try {
						converter = converterClass.getDeclaredConstructor().newInstance();
					} catch (Exception e) {
						throw new CsvBadConverterException(converterClass, e.getMessage());
					}
				}
			}

			return converter;
		}

		@Override
		protected BeanField<TrackedCaseCsv, String> findField(int col) throws CsvBadConverterException {

			return Optional.ofNullable(headerIndex.getByPosition(col))
					.map(it -> columnMapping.get(it))
					.map(String::trim)
					.map(fieldMap::get)
					.orElse(null);
		}

		private void fillTableFrom(String fieldName) {
			var sormasCsv = fieldMap.get(fieldName).getField().getAnnotation(SormasCsv.class);

			if (sormasCsv != null) {

				var export = stripToNull(sormasCsv.export());
				var group = sormasCsv.group() != NA ? sormasCsv.group().name() : null;

				if (export == null ^ group == null) {
					throw new IllegalArgumentException(
							"In SormasCsvBind export and group must be set together or both must be empty.");
				}

				String importDe = stripToNull(sormasCsv.importDe());
				String importEn = stripToNull(sormasCsv.importEn());

				if (export == null ^ group == null) {
					throw new IllegalArgumentException(
							"In SormasCsvBind importDe and importEn must be set together or both must be empty.");
				}

				table.put(fieldName, Column.Export, export);
				table.put(fieldName, Column.Group, group);
				table.put(fieldName, Column.ImportDe, importDe);
				table.put(fieldName, Column.ImportEn, importEn);

				if (!sormasCsv.ignoreForContacts()) {

					var exportContact = StringUtils.getIfBlank(sormasCsv.exportContact(), () -> export);

					table.put(fieldName, Column.ExportContacts, exportContact);
				}

			}
		}

		enum Column {
			Field, Export, Group, ExportContacts, ImportDe, ImportEn
		}
	}

	public static class CommentConverter extends AbstractCsvConverter {

		@Override
		public Object convertToRead(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {

			if (StringUtils.isBlank(value)) {
				return null;
			}

			var retList = new ArrayList<Comment>();

			var comments = value.split(COMMENT_SEPERATOR);

			for (String comment : comments) {

				var parts = comment.split("|");

				if (parts.length == 3) {
					try {

						LocalDateTime date = LocalDateTime.parse(parts[0]);

						retList.add(new Comment(parts[2], parts[1], date));

					} catch (DateTimeParseException e) {

						CsvDataTypeMismatchException csve = new CsvDataTypeMismatchException(
								value, type, ResourceBundle
										.getBundle("messages", errorLocale)
										.getString("csv.input.not.datetime"));
						csve.initCause(e);
						throw csve;
					}

				} else {
					retList.add(new Comment(comment, null, LocalDateTime.now()));
				}
			}

			return List.class.isAssignableFrom(type) ? retList : retList.get(0);
		}

		@Override
		public String convertToWrite(Object value) throws CsvDataTypeMismatchException {

			if (value == null) {
				return "";
			} else if (value instanceof List) {

				if (((List) value).isEmpty()) {
					return "";
				} else if (((List) value).get(0) instanceof Comment) {

					return ((List<Object>) value).stream()
							.map(this::convertToString)
							.collect(Collectors.joining(COMMENT_SEPERATOR));

				}
			} else if (value instanceof Comment) {
				return convertToString(value);
			}

			throw new CsvDataTypeMismatchException(ResourceBundle
					.getBundle("messages", errorLocale)
					.getString("csv.field.not.comment"));
		}

		private String convertToString(Object value) {

			Comment comment = (Comment) value;

			return String.join("|", comment.getDate().toString(), comment.getAuthor(), comment.getText());
		}
	}

	static class ConvertYesNoBoolean extends AbstractCsvConverter {

		@Override
		public Object convertToRead(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
			return new BooleanConverter(null).convert(Boolean.class, trim(value));
		}

		@Override
		public String convertToWrite(Object value) throws CsvDataTypeMismatchException {

			if (value == null) {
				return "UNKNOWN";
			} else if (value instanceof Boolean) {
				return (Boolean) value ? "YES" : "NO";
			} else {

				throw new CsvDataTypeMismatchException(ResourceBundle
						.getBundle("messages", errorLocale)
						.getString("csv.field.not.boolean"));
			}
		}
	}

	static class ConvertNegativePositiveBoolean extends AbstractCsvConverter {

		enum Result {
			NEGATIVE, POSITIVE
		}

		@Override
		public Object convertToRead(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {

			String[] trueStrings = { Result.POSITIVE.name(), "Positiv" };
			String[] falseStrings = { Result.NEGATIVE.name(), "Negativ" };
			var converter = new BooleanConverter(trueStrings, falseStrings, null);

			return converter.convert(Boolean.class, trim(value));
		}

		@Override
		public String convertToWrite(Object value) throws CsvDataTypeMismatchException {

			if (value == null) {
				return null;
			} else if (value instanceof Boolean) {
				return ((Boolean) value ? Result.POSITIVE : Result.NEGATIVE).name();
			} else {

				throw new CsvDataTypeMismatchException(ResourceBundle
						.getBundle("messages", errorLocale)
						.getString("csv.field.not.boolean"));
			}
		}
	}
}
