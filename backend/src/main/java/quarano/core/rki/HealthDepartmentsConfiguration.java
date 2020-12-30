package quarano.core.rki;

import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.core.ZipCode;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.xmlbeam.XBProjector;
import org.xmlbeam.config.DefaultXMLFactoriesConfig;
import org.xmlbeam.config.DefaultXMLFactoriesConfig.NamespacePhilosophy;
import org.xmlbeam.types.DefaultTypeConverter;
import org.xmlbeam.types.DefaultTypeConverter.Conversion;

/**
 * Uses the <a href="https://www.rki.de/DE/Content/Infekt/IfSG/Software/Aktueller_Datenbestand.html">data from RKI</a>
 * to offer the search for health departments by zip code or location in one service.
 *
 * @author Jens Kutzsche
 * @author Oliver Drotbohm
 */
@Configuration
public class HealthDepartmentsConfiguration {

	private static final ZoneId ZONE_BERLIN = ZoneId.of("Europe/Berlin");

	@Bean
	HealthDepartments healthDepartments() {

		var config = new DefaultXMLFactoriesConfig().setNamespacePhilosophy(NamespacePhilosophy.AGNOSTIC);
		var projector = new XBProjector(config);

		projector.config()
				.setTypeConverter(new DefaultTypeConverter(Locale.getDefault(), TimeZone.getTimeZone(ZONE_BERLIN))
						.setConversionForType(EmailAddress.class, new EmailConversion())
						.setConversionForType(PhoneNumber.class, new PhoneConversion())
						.setConversionForType(ZipCode.class, new ZipCodeConversion()));

		try {
			return projector.io().fromURLAnnotation(HealthDepartments.class);
		} catch (IOException e) {
			throw new IllegalStateException("Can't initialize service HealthDepartmentOverview!", e);
		}
	}

	/**
	 * @since 1.4
	 */
	@Bean
	FederalStates federalStates() {
		return new FederalStates();
	}

	private final class EmailConversion extends Conversion<EmailAddress> {

		private static final long serialVersionUID = -5135026806926077478L;

		private EmailConversion() {
			super(null);
		}

		/*
		 * (non-Javadoc)
		 * @see org.xmlbeam.types.DefaultTypeConverter.Conversion#convert(java.lang.String)
		 */
		@Nullable
		@Override
		@SuppressWarnings("null")
		public EmailAddress convert(@Nullable String data) {
			return EmailAddress.isValid(data) ? EmailAddress.of(data) : null;
		}
	}

	private final class PhoneConversion extends Conversion<PhoneNumber> {

		private static final long serialVersionUID = -6961139639583293964L;

		private PhoneConversion() {
			super(null);
		}

		/*
		 * (non-Javadoc)
		 * @see org.xmlbeam.types.DefaultTypeConverter.Conversion#convert(java.lang.String)
		 */
		@Override
		@Nullable
		@SuppressWarnings("null")
		public PhoneNumber convert(@Nullable String data) {
			return PhoneNumber.isValid(data) ? PhoneNumber.of(data) : null;
		}
	}

	private final class ZipCodeConversion extends Conversion<ZipCode> {

		private static final long serialVersionUID = -2103389310240500743L;

		private ZipCodeConversion() {
			super(null);
		}

		/*
		 * (non-Javadoc)
		 * @see org.xmlbeam.types.DefaultTypeConverter.Conversion#convert(java.lang.String)
		 */
		@Override
		@Nullable
		@SuppressWarnings("null")
		public ZipCode convert(@Nullable String data) {
			return ZipCode.isValid(data) ? ZipCode.of(data) : null;
		}
	}
}
