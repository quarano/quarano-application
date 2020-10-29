package quarano.reference;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/**
 * Component to provide mapping capabilities between {@link Symptom}s and their representations.
 *
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
class SymptomRepresentations {

	private final @NonNull MessageSourceAccessor messages;

	SymptomDto toRepresentation(Symptom symptom) {

		var i18nedName = messages.getMessage("symptom." + symptom.getId());

		return new SymptomDto(symptom.getId(), i18nedName, symptom.isCharacteristic());
	}
}
