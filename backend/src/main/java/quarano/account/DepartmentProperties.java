package quarano.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("quarano.department")
public class DepartmentProperties {

	private final DefaultDepartment defaultDepartment;
	private final @Getter DefaultAdminAccount defaultAccount;

	@RequiredArgsConstructor
	static class DefaultDepartment {
		private final String name, emailAddress, phoneNumber;
	}

	@Getter
	@RequiredArgsConstructor
	static class DefaultAdminAccount {
		private final String firstname, lastname, emailAddress;
	}

	Department getDefaultDepartment() {

		return new Department(defaultDepartment.name) //
				.setEmailAddress(EmailAddress.of(defaultDepartment.emailAddress)) //
				.setPhoneNumber(PhoneNumber.of(defaultDepartment.phoneNumber));
	}
}
