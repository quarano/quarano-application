package quarano.diary;

import io.vavr.control.Try;
import lombok.NonNull;
import quarano.account.Department;
import quarano.account.DepartmentContact;
import quarano.account.DepartmentContact.ContactType;
import quarano.core.EmailAddress;
import quarano.core.EmailSender.BasicEmailData;
import quarano.core.EmailTemplates.Keys;
import quarano.tracking.TrackedPerson;

import java.util.Map;
import java.util.function.Supplier;

public class TrackedPersonEmailData extends BasicEmailData {

	private final @NonNull TrackedPerson trackedPerson;
	private final @NonNull Department department;

	public TrackedPersonEmailData(@NonNull TrackedPerson trackedPerson, @NonNull Department department, String subject,
			Keys template, Supplier<Try<Map<String, Object>>> placeholders) {

		super(subject, template, placeholders);

		this.trackedPerson = trackedPerson;
		this.department = department;
	}

	@Override
	public String getToLastName() {
		return trackedPerson.getLastName();
	}

	@Override
	public EmailAddress getToEmailAddress() {
		return trackedPerson.getEmailAddress();
	}

	@Override
	public String getToFullName() {
		return trackedPerson.getFullName();
	}

	@Override
	public EmailAddress getFromEmailAddress() {

		return department.getContact(ContactType.CONTACT)
				.or(() -> department.getContact(ContactType.INDEX))
				.map(DepartmentContact::getEmailAddress).orElse(null);
	}

	@Override
	public String getFromFullName() {
		return department.getName();
	}
}
