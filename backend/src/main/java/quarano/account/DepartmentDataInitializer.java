package quarano.account;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.account.Department.DepartmentIdentifier;
import quarano.core.DataInitializer;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
@Order(480)
@Slf4j
public class DepartmentDataInitializer implements DataInitializer {

	public final static DepartmentIdentifier DEPARTMENT_ID_DEP1 = DepartmentIdentifier
			.of(UUID.fromString("aba0ec65-6c1d-4b7b-91b4-c31ef16ad0a2"));
	public final static DepartmentIdentifier DEPARTMENT_ID_DEP2 = DepartmentIdentifier
			.of(UUID.fromString("ca3f3e9a-414a-4117-a623-59b109b269f1"));
	private final @NonNull DepartmentRepository departments;

	/*
	 * (non-Javadoc)
	 * @see quarano.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		if (departments.count() > 0) {
			log.info("Departments found, skipping test data creation.");
			return;
		} else {
			log.info("Testdata: creating two health departmens");
		}

		departments.saveAll(List.of(
				new Department("GA Mannheim", DEPARTMENT_ID_DEP1)
						.setContacts(Set.of(
								new DepartmentContact()
										.setType(DepartmentContact.ContactType.INDEX)
										.setEmailAddress(EmailAddress.of("index-email@gamannheim.de"))
										.setPhoneNumber(PhoneNumber.of("0123456789")),
								new DepartmentContact()
										.setType(DepartmentContact.ContactType.CONTACT)
										.setEmailAddress(EmailAddress.of("contact-email@gamannheim.de"))
										.setPhoneNumber(PhoneNumber.of("00123456789")))
						),
				new Department("GA Darmstadt", DEPARTMENT_ID_DEP2)
						.setContacts(Set.of(
								new DepartmentContact()
										.setType(DepartmentContact.ContactType.INDEX)
										.setEmailAddress(EmailAddress.of("index-email@gadarmstadt.de"))
										.setPhoneNumber(PhoneNumber.of("0123456789")),
								new DepartmentContact()
										.setType(DepartmentContact.ContactType.CONTACT)
										.setEmailAddress(EmailAddress.of("contact-email@gadarmstadt.de"))
										.setPhoneNumber(PhoneNumber.of("00123456789")))
						)
		));
	}
}
