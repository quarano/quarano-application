package quarano.core.rki;

import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.core.ZipCode;

import java.util.List;
import java.util.Optional;

import org.xmlbeam.annotation.XBDocURL;
import org.xmlbeam.annotation.XBRead;

@XBDocURL("resource://masterdata/TransmittingSiteSearchText.xml")
public interface HealthDepartments {

	// Without NamespacePhilosophy.AGNOSTIC the namespace of an element must be declared!
	// @XBRead("/xbdefaultns:TransmittingSites/xbdefaultns:TransmittingSite")
	@XBRead("/TransmittingSites/TransmittingSite")
	List<HealthDepartment> getAll();

	@XBRead("//SearchText[@Value='{0}']/..")
	Optional<HealthDepartment> findDepartmentWithExact(String searchString);

	/**
	 * @param searchString
	 * @return
	 * @since 1.4
	 */
	@XBRead("boolean(//SearchText[@Value='{0}'])")
	boolean hasDepartmentWithExact(String searchString);

	@XBRead("//SearchText[contains(@Value, '{0}')]/..")
	List<HealthDepartment> findDepartmentsContains(String searchString);

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

		@XBRead("@Covid19Hotline")
		PhoneNumber getCovid19Hotline();

		@XBRead("@Covid19Fax")
		PhoneNumber getCovid19Fax();

		@XBRead("@Covid19EMail")
		EmailAddress getCovid19EMail();

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
}
