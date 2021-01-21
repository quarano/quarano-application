package quarano.account;

import static org.springframework.util.StringUtils.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import quarano.account.DepartmentContact.ContactType;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.core.rki.FederalStates;

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

	public Department getDefaultDepartment() {

		var rkiCode = trimWhitespace(defaultDepartment.rkiCode);
		var federalState = trimWhitespace(defaultDepartment.federalState);

		if (!hasText(federalState)) {
			federalState = new FederalStates().apply(rkiCode).getName();
		}

		return new Department(defaultDepartment.name, rkiCode, federalState,
				trimWhitespace(defaultDepartment.district))
						.setContacts(defaultDepartment.contacts.stream()
								.map(contact -> new DepartmentContact()
										.setType(ContactType.valueOf(contact.type))
										.setEmailAddress(EmailAddress.ofNullable(contact.emailAddress))
										.setPhoneNumber(PhoneNumber.of(contact.phoneNumber)))
								.collect(Collectors.toUnmodifiableSet()));
	}

	@RequiredArgsConstructor
	static class DefaultDepartment {

		private final String name;
		private final String rkiCode;
		private final String federalState;
		private final String district;
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
