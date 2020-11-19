package quarano.masterdata;

import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.List;

import org.springframework.data.util.Streamable;

/**
 * @author Oliver Drotbohm
 * @author Jens Kutzsche
 */
@RequiredArgsConstructor(staticName = "of")
public class Symptoms implements Streamable<Symptom> {

	private final List<Symptom> symptoms;

	public boolean hasCharacteristicSymptom() {
		return symptoms.stream().anyMatch(Symptom::isCharacteristic);
	}

	/**
	 * @since 1.4
	 */
	public boolean hasSuspiciousSymptomeAtIndex() {
		return symptoms.stream().anyMatch(Symptom::isSuspiciousAtIndex);
	}

	/**
	 * @since 1.4
	 */
	public boolean hasSuspiciousSymptomeAtContact() {
		return symptoms.stream().anyMatch(Symptom::isSuspiciousAtContact);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Symptom> iterator() {
		return symptoms.iterator();
	}
}
