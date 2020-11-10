package quarano.department.web;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.department.TrackedCase;

import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

/**
 * @author Jens Kutzsche
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
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

	static final String[] CSV_ORDER = { TYPE, LAST_NAME, FIRST_NAME, DATE_OF_BIRTH, CITY, ZIP_CODE, STREET,
			HOUSE_NUMBER, EMAIL, PHONE, MOBILE, QUARANTINE_FROM, QUARANTINE_TO, QUARANTINE_CHANGED };

	public void writeQuarantineOrderCsv(PrintWriter writer, Stream<TrackedCase> cases) {

		var csvCases = cases.map(QuarantineOrderCsv::new);

		try {

			var mappingStrategy = new HeaderColumnNameMappingStrategy<QuarantineOrderCsv>();
			mappingStrategy.setType(QuarantineOrderCsv.class);
			mappingStrategy.setColumnOrderOnWrite(createOrderComparatorFor(CSV_ORDER));

			var beanToCsv = new StatefulBeanToCsvBuilder<QuarantineOrderCsv>(writer)
					.withMappingStrategy(mappingStrategy)
					.withSeparator(';')
					.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
					.withEscapechar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
					.withLineEnd(CSVWriter.DEFAULT_LINE_END)
					.build();

			beanToCsv.write(csvCases);

		} catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			log.error("Error mapping Bean to CSV", e);
			throw new RuntimeException(e);
		}
	}

	private static Comparator<String> createOrderComparatorFor(String[] orderArray) {
		return Comparator.comparingInt(it -> ArrayUtils.indexOf(orderArray, it));
	}

	@Getter(AccessLevel.PACKAGE)
	private static class QuarantineOrderCsv {

		@CsvBindByName(column = LAST_NAME) //
		private final String lastName;
		@CsvBindByName(column = FIRST_NAME) //
		private final String firstName;
		@CsvBindByName(column = DATE_OF_BIRTH) //
		private final String dateOfBirth;
		@CsvBindByName(column = EMAIL) //
		private final String email;
		@CsvBindByName(column = PHONE) //
		private final String phone;
		@CsvBindByName(column = MOBILE) //
		private final String mobile;
		@CsvBindByName(column = CITY) //
		private final String city;
		@CsvBindByName(column = ZIP_CODE) //
		private final String zipCode;
		@CsvBindByName(column = STREET) //
		private final String street;
		@CsvBindByName(column = HOUSE_NUMBER) //
		private final String houseNumber;
		@CsvBindByName(column = QUARANTINE_FROM) //
		private final String quarantineFrom;
		@CsvBindByName(column = QUARANTINE_TO) //
		private final String quarantineTo;
		@CsvBindByName(column = TYPE) //
		private final String type;
		@CsvBindByName(column = QUARANTINE_CHANGED) //
		private final String quarantineChanged;

		public QuarantineOrderCsv(TrackedCase trackedCase) {

			DateTimeFormatter localizer = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
			DateTimeFormatter localizerDateTime = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

			var trackedPerson = trackedCase.getTrackedPerson();
			lastName = trackedPerson.getLastName();
			firstName = trackedPerson.getFirstName();
			email = Objects.toString(trackedPerson.getEmailAddress(), null);
			phone = Objects.toString(trackedPerson.getPhoneNumber(), null);
			mobile = Objects.toString(trackedPerson.getMobilePhoneNumber(), null);

			var birth = trackedPerson.getDateOfBirth();
			dateOfBirth = birth == null ? null : birth.format(localizer);

			var address = trackedPerson.getAddress();
			city = address == null ? null : address.getCity();
			zipCode = address == null ? null : Objects.toString(address.getZipCode(), null);
			street = address == null ? null : address.getStreet();
			houseNumber = address == null ? null : Objects.toString(address.getHouseNumber(), null);

			var quarantine = trackedCase.getQuarantine();
			quarantineFrom = quarantine == null ? null : quarantine.getFrom().format(localizer);
			quarantineTo = quarantine == null ? null : quarantine.getTo().format(localizer);

			var quarantineLastModified = trackedCase.getQuarantineLastModified();
			quarantineChanged = quarantineLastModified == null ? null : quarantineLastModified.format(localizerDateTime);

			type = trackedCase.getType().getPrimaryCaseType().toString();
		}
	}
}
