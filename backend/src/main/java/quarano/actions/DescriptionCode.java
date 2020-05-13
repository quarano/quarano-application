package quarano.actions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor()
public enum DescriptionCode {

	INCREASED_TEMPERATURE,

	FIRST_CHARACTERISTIC_SYMPTOM(2.0f),

	DIARY_ENTRY_MISSING,

	MISSING_DETAILS_INDEX,
	MISSING_DETAILS_CONTACT,

	INITIAL_CALL_OPEN_INDEX,
	INITIAL_CALL_OPEN_CONTACT;


	private final @Getter float multiplier;

	private DescriptionCode() {
		this(1f);
	}
}
