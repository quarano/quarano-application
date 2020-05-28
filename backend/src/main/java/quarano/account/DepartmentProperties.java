package quarano.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import quarano.account.DepartmentContact.ContactType;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;

import java.util.List;
import java.util.stream.Collectors;

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

	Department getDefaultDepartment() {

		return new Department(defaultDepartment.name) //
				.setContacts(defaultDepartment.contacts.stream() //
						.map(contact -> new DepartmentContact() //
								.setType(ContactType.valueOf(contact.type)) //
								.setEmailAddress(EmailAddress.of(contact.emailAddress)) //
								.setPhoneNumber(PhoneNumber.of(contact.phoneNumber))) //
						.collect(Collectors.toUnmodifiableSet()));
	}

	@RequiredArgsConstructor
	static class DefaultDepartment {

		private final String name;
		private final List<DefaultDepartmentContact> contacts;

		@RequiredArgsConstructor
		static class DefaultDepartmentContact {
			private final String type, emailAddress, phoneNumber;
		}
	}

	@Getter
	@RequiredArgsConstructor
	static class DefaultAdminAccount {
		private final String firstname, lastname, emailAddress;
	}
}
