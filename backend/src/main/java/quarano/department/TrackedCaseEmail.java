package quarano.department;

import lombok.Getter;
import quarano.account.DepartmentSender;
import quarano.core.EmailSender.AbstractTemplatedEmail;
import quarano.core.EmailTemplates;
import quarano.tracking.TrackedPersonReceipient;

import java.util.Map;

public class TrackedCaseEmail extends AbstractTemplatedEmail {

	private final @Getter TrackedCase trackedCase;

	TrackedCaseEmail(TrackedCase trackedCase, String subject, EmailTemplates.Key template,
			Map<String, ? extends Object> placeholders) {

		super(DepartmentSender.of(trackedCase.getDepartment(), trackedCase.getType().toContactType()),
				TrackedPersonReceipient.of(trackedCase.getTrackedPerson()),
				subject, template, placeholders, trackedCase.getTrackedPerson().getLocale());

		this.trackedCase = trackedCase;
	}
}
