package quarano.department.csv;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.department.TrackedCase;
import quarano.tracking.Address;
import quarano.tracking.Quarantine;

import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
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
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TrackedCaseCsvRepresentations {

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

	private Comparator<String> createOrderComparatorFor(String[] orderArray) {
		return Comparator.comparingInt(it -> ArrayUtils.indexOf(orderArray, it));
	}

	public static class QuarantineOrderCsv {

		@CsvBindByName(column = LAST_NAME)
		private final @Getter String lastName;
		@CsvBindByName(column = FIRST_NAME)
		private final @Getter String firstName;
		@CsvBindByName(column = DATE_OF_BIRTH)
		private final @Getter String dateOfBirth;
		@CsvBindByName(column = EMAIL)
		private final @Getter String email;
		@CsvBindByName(column = PHONE)
		private final @Getter String phone;
		@CsvBindByName(column = MOBILE)
		private final @Getter String mobile;
		@CsvBindByName(column = CITY)
		private final @Getter String city;
		@CsvBindByName(column = ZIP_CODE)
		private final @Getter String zipCode;
		@CsvBindByName(column = STREET)
		private final @Getter String street;
		@CsvBindByName(column = HOUSE_NUMBER)
		private final @Getter String houseNumber;
		@CsvBindByName(column = QUARANTINE_FROM)
		private final @Getter String quarantineFrom;
		@CsvBindByName(column = QUARANTINE_TO)
		private final @Getter String quarantineTo;
		@CsvBindByName(column = TYPE)
		private final @Getter String type;
		@CsvBindByName(column = QUARANTINE_CHANGED)
		private final @Getter String quarantineChanged;

		public QuarantineOrderCsv(TrackedCase trackedCase) {

			DateTimeFormatter localizer = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
			DateTimeFormatter localizerDateTime = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

			var trackedPerson = trackedCase.getTrackedPerson();
			lastName = trackedPerson.getLastName();
			firstName = trackedPerson.getFirstName();
			email = Objects.toString(trackedPerson.getEmailAddress(), null);
			phone = Objects.toString(trackedPerson.getPhoneNumber(), null);
			mobile = Objects.toString(trackedPerson.getMobilePhoneNumber(), null);

			var birth = Optional.ofNullable(trackedPerson.getDateOfBirth());
			dateOfBirth = birth.map(it -> it.format(localizer)).orElse(null);

			var address = Optional.ofNullable(trackedPerson.getAddress());
			city = address.map(Address::getCity).orElse(null);
			zipCode = address.map(Address::getZipCode).map(it -> Objects.toString(it, null)).orElse(null);
			street = address.map(Address::getStreet).orElse(null);
			houseNumber = address.map(Address::getHouseNumber).map(it -> Objects.toString(it)).orElse(null);

			var quarantine = Optional.ofNullable(trackedCase.getQuarantine());
			quarantineFrom = quarantine.map(Quarantine::getFrom).map(it -> it.format(localizer)).orElse(null);
			quarantineTo = quarantine.map(Quarantine::getTo).map(it -> it.format(localizer)).orElse(null);
			quarantineChanged = quarantine.map(Quarantine::getLastModified).map(it -> it.format(localizerDateTime))
					.orElse(null);

			type = trackedCase.getType().getPrimaryCaseType().toString();
		}
	}
}
