package quarano.actions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor()
public enum DescriptionCode {

	INCREASED_TEMPERATURE(true),

	FIRST_CHARACTERISTIC_SYMPTOM(2.0f, true),

	DIARY_ENTRY_MISSING(true),

	QUARANTINE_ENDING(true),

	MISSING_DETAILS_INDEX(false),

	MISSING_DETAILS_CONTACT(false),

	INITIAL_CALL_OPEN_INDEX(true),

	INITIAL_CALL_OPEN_CONTACT(true);

	private final @Getter float multiplier;
	private final @Getter boolean manuallyResolvable;

	DescriptionCode(boolean manuallyResolvable) {
		this(1f, manuallyResolvable);
	}
}
