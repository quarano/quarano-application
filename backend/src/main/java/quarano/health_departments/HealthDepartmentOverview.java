package quarano.health_departments;

import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.tracking.ZipCode;

import java.io.IOException;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.stereotype.Service;
import org.xmlbeam.XBProjector;
import org.xmlbeam.annotation.XBDocURL;
import org.xmlbeam.annotation.XBRead;
import org.xmlbeam.config.DefaultXMLFactoriesConfig;
import org.xmlbeam.config.DefaultXMLFactoriesConfig.NamespacePhilosophy;
import org.xmlbeam.types.DefaultTypeConverter;
import org.xmlbeam.types.DefaultTypeConverter.Conversion;

/**
 * Uses the <a href="https://www.rki.de/DE/Content/Infekt/IfSG/Software/Aktueller_Datenbestand.html">data from RKI</a>
 * to offer the search for health departments by zip code or location in one service.
 * 
 * @author Jens Kutzsche
 */
@Service
public class HealthDepartmentOverview {

	private static final ZoneId ZONE_BERLIN = ZoneId.of("Europe/Berlin");

	private final HealthDepartments healthDepartments;

	HealthDepartmentOverview() {

		var config = new DefaultXMLFactoriesConfig().setNamespacePhilosophy(NamespacePhilosophy.AGNOSTIC);
		var projector = new XBProjector(config);

		var converter = new DefaultTypeConverter(Locale.getDefault(), TimeZone.getTimeZone(ZONE_BERLIN))
				.setConversionForType(EmailAddress.class, new EmailConversion())
				.setConversionForType(PhoneNumber.class, new PhoneConversion())
				.setConversionForType(ZipCode.class, new ZipCodeConversion());
		projector.config().setTypeConverter(converter);

		try {
			this.healthDepartments = projector.io().fromURLAnnotation(HealthDepartments.class);
		} catch (IOException e) {
			throw new IllegalStateException("Can't initialize service HealthDepartmentOverview!", e);
		}
	}

	public List<HealthDepartment> getAllHealthDepartments() {
		return healthDepartments.getAll();
	}

	public Optional<HealthDepartment> findHealthDepartmentWithExact(String searchString) {
		return Optional.ofNullable(healthDepartments.findDepartmentWithExact(searchString));
	}

	public List<HealthDepartment> findHealthDepartmentsContains(String searchString) {
		return healthDepartments.findDepartmentsContains(searchString);
	}

	@XBDocURL("resource://masterdata/TransmittingSiteSearchText.xml")
	private interface HealthDepartments {

		// Without NamespacePhilosophy.AGNOSTIC the namespace of an element must be declared!
		// @XBRead("/xbdefaultns:TransmittingSites/xbdefaultns:TransmittingSite")
		@XBRead("/TransmittingSites/TransmittingSite")
		List<HealthDepartment> getAll();

		@XBRead("//SearchText[@Value='{0}']/..")
		HealthDepartment findDepartmentWithExact(String searchString);

		@XBRead("//SearchText[contains(@Value, '{0}')]/..")
		List<HealthDepartment> findDepartmentsContains(String searchString);
	}

	public interface HealthDepartment {
		/**
		 * @return Often the administrative unit (e.g. the county)
		 */
		@XBRead("@Name")
		String getName();

		/**
		 * @return The code from RKI
		 */
		@XBRead("@Code")
		String getCode();

		/**
		 * @return The department of the administrative unit.
		 */
		@XBRead("@Department")
		String getDepartment();

		@XBRead("@Phone")
		PhoneNumber getPhone();

		@XBRead("@Fax")
		PhoneNumber getFax();

		@XBRead("@Email")
		EmailAddress getEmail();

		@XBRead(".")
		Address getAddress();

		@XBRead("SearchText/@Value")
		List<String> getSearchTexts();

		public interface Address {
			@XBRead("@Street")
			String getStreet();

			@XBRead("@Postalcode")
			ZipCode getZipcode();

			@XBRead("@Place")
			String getPlace();
		}
	}

	private final class EmailConversion extends Conversion<EmailAddress> {

		private static final long serialVersionUID = -5135026806926077478L;

		private EmailConversion() {
			super(null);
		}

		@Override
		public EmailAddress convert(final String data) {
			return EmailAddress.isValid(data) ? EmailAddress.of(data) : null;
		}
	}

	private final class PhoneConversion extends Conversion<PhoneNumber> {

		private static final long serialVersionUID = -6961139639583293964L;

		private PhoneConversion() {
			super(null);
		}

		@Override
		public PhoneNumber convert(final String data) {
			return PhoneNumber.isValid(data) ? PhoneNumber.of(data) : null;
		}
	}

	private final class ZipCodeConversion extends Conversion<ZipCode> {

		private static final long serialVersionUID = -2103389310240500743L;

		private ZipCodeConversion() {
			super(null);
		}

		@Override
		public ZipCode convert(final String data) {
			return ZipCode.isValid(data) ? ZipCode.of(data) : null;
		}
	}
}
