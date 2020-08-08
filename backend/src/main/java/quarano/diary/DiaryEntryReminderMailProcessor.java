package quarano.diary;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.core.EmailSender;
import quarano.core.EmailTemplates.Keys;
import quarano.core.EnumMessageSourceResolvable;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.util.Streamable;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This class collects the persons with missing diary entries and sends a reminder mail to every person.
 *
 * @author Jens Kutzsche
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DiaryEntryReminderMailProcessor {

	private final @NonNull DiaryManagement diaries;
	private final @NonNull EmailSender emailSender;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull MessageSourceAccessor messages;

	@Scheduled(cron = "0 10 12,23 * * *")
	void checkForReminderMail() {

		if (emailSender.testConnection().isFailure()) {
			return;
		}

		var slot = Slot.now().previous();

		collectPersonsMissingEntry(slot)
				.flatMap(it2 -> cases.findCaseWithDepartmentAndContactsByTrackedPerson(it2).stream())
				.forEach(it -> sendReminderMail(it, slot));
	}

	Streamable<TrackedPersonIdentifier> collectPersonsMissingEntry(Slot slot) {

		log.debug("Check missing diary entries for slot {}.", slot);

		return diaries.findMissingDiaryEntryPersons(List.of(slot));
	}

	void sendReminderMail(TrackedCase trackedCase, Slot slot) {

		var trackedPerson = trackedCase.getTrackedPerson();
		var subject = messages.getMessage("DiaryEntryReminderMail.subject");
		var textTemplate = Keys.DIARY_REMINDER;
		var slotTranslated = messages.getMessage(EnumMessageSourceResolvable.of(slot.getTimeOfDay()).getCodes()[0],
				new Object[] { slot.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) });
		var logArgs = new Object[] { trackedPerson.getFullName(), String.valueOf(trackedPerson.getEmailAddress()),
				trackedCase.getId().toString() };

		Supplier<Try<Map<String, Object>>> placeholders = () -> Try
				.success(Map.<String, Object> of("slot", slotTranslated));

		emailSender
				.sendMail(trackedCase, subject, textTemplate, placeholders)
				.onSuccess(it -> log.debug("Reminder mail sended to {{}; {}; Case-ID {}}", logArgs))
				.onFailure(e -> log.debug("Can't send reminder mail to {{}; {}; Case-ID {}}", logArgs))
				.onFailure(e -> log.debug("Exception", e));
	}
}
