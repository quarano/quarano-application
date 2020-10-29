package quarano.department;

import quarano.account.DepartmentSender;
import quarano.core.EmailSender.AbstractTemplatedEmail;
import quarano.core.EmailTemplates;
import quarano.tracking.TrackedPersonReceipient;

import java.util.Map;

class TrackedCaseEmail extends AbstractTemplatedEmail {

	public TrackedCaseEmail(TrackedCase trackedCase, String subject, EmailTemplates.Key template,
			Map<String, ? extends Object> placeholders) {

		super(DepartmentSender.of(trackedCase.getDepartment(), trackedCase.getType().toContactType()),
				TrackedPersonReceipient.of(trackedCase.getTrackedPerson()),
				subject, template, placeholders, trackedCase.getTrackedPerson().getLocale());
	}
}
