package quarano.tracking;

import quarano.account.Department;
import quarano.account.DepartmentContact.ContactType;
import quarano.account.DepartmentSender;
import quarano.core.EmailSender.AbstractTemplatedEmail;
import quarano.core.EmailTemplates.Key;

import java.util.Map;

public class TrackedPersonEmail extends AbstractTemplatedEmail {

	public TrackedPersonEmail(TrackedPerson trackedPerson, Department department, ContactType contactType,
			String subject, Key templateKey, Map<String, ? extends Object> placeholders) {

		super(DepartmentSender.of(department, contactType), TrackedPersonReceipient.of(trackedPerson), subject, templateKey,
				placeholders, trackedPerson.getLocale());
	}
}
