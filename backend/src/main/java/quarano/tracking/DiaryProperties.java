package quarano.tracking;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @author Oliver Drotbohm
 */
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("quarano.diary")
public class DiaryProperties {

	private static final Duration DEFAULT_POST_SLOT = Duration.ofHours(24);
	private static final Duration DEFAULT_POST_CREATE = Duration.ofMinutes(15);

	/**
	 * Defines the {@link Duration} that {@link DiaryEntry} can still be edited relative to the official {@link Slot} end
	 * time they're assigned to.
	 */
	private final Duration editDuration;

	/**
	 * Defines the {@link Duration} that {@link DiaryEntry} can still be edited after they have been created. This
	 * potentially extends the value defined for {@link #editDuration} in case a DiaryEntry was created for a {@link Slot}
	 * that's just about to become uneditable.
	 */
	private final Duration editTolerance;

	/**
	 * Defines the number of slots, which may not be filled.
	 */
	private final int toleratedSlotCount;

	/**
	 * Defines the {@link Duration} that {@link DiaryEntry} can still be created.
	 */
	private final Duration createDuration;

	public boolean canBeEdited(DiaryEntry entry) {

		var postSlot = editDuration == null ? DEFAULT_POST_SLOT : editDuration;
		var postCreation = editTolerance == null ? DEFAULT_POST_CREATE : editTolerance;

		var slotEnd = entry.getSlot().getOfficialEnd().plus(postSlot);
		var creationEnd = entry.getReportedAt().plus(postCreation);
		var now = LocalDateTime.now();

		return slotEnd.isAfter(now) || creationEnd.isAfter(now);
	}

	public boolean canBeCreated(Slot slot) {

		var duration = createDuration == null ? DEFAULT_POST_SLOT : createDuration;

		return !slot.isOlderThan(duration);
	}

	public int getToleratedSlotCount() {
		return this.toleratedSlotCount;
	}
}
