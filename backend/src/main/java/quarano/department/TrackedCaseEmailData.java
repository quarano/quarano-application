package quarano.department;

import io.vavr.control.Try;
import lombok.NonNull;
import quarano.core.EmailTemplates.Keys;
import quarano.diary.TrackedPersonEmailData;

import java.util.Map;
import java.util.function.Supplier;

public class TrackedCaseEmailData extends TrackedPersonEmailData {

	public TrackedCaseEmailData(@NonNull TrackedCase trackedCase, String subject, Keys template,
			Supplier<Try<Map<String, Object>>> placeholders) {

		super(trackedCase.getTrackedPerson(), trackedCase.getDepartment(), subject, template, placeholders);
	}
}
