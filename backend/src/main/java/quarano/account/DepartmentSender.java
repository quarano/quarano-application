package quarano.account;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import quarano.account.DepartmentContact.ContactType;
import quarano.core.EmailAddress;
import quarano.core.EmailSender.AbstractTemplatedEmail.Sender;

/**
 * A {@link Sender} implementation based on a {@link Department}.
 *
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DepartmentSender implements Sender {

	private static final String NO_EMAIL_ADDRESS = "Department does not have an email address registered for contact type %s!";

	private final Department department;
	private final EmailAddress email;

	/**
	 * Creates a new {@link Sender} based on the given {@link Department} and {@link ContactType}.
	 *
	 * @param department must not be {@literal null} and expose an {@link EmailAddress} for the given {@link ContactType}.
	 * @param type must not be {@literal null}.
	 * @return
	 */
	public static Sender of(Department department, ContactType type) {

		var emailAddress = department.getContact(type)
				.map(DepartmentContact::getEmailAddress)
				.orElseThrow(() -> new IllegalArgumentException(String.format(NO_EMAIL_ADDRESS, type)));

		return new DepartmentSender(department, emailAddress);
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.core.EmailSender.TemplatedEmail.Sender#getFullName()
	 */
	@Override
	public String getFullName() {
		return department.getName();
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.core.EmailSender.TemplatedEmail.Sender#getEmailAddress()
	 */
	@Override
	public EmailAddress getEmailAddress() {
		return email;
	}
}
