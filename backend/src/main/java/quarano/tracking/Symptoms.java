package quarano.tracking;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.data.util.Streamable;

import lombok.RequiredArgsConstructor;
import quarano.reference.Symptom;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
public class Symptoms implements Streamable<Symptom> {

	private final List<Symptom> symptoms;

	public boolean hasCharacteristicSymptom() {
		return symptoms.stream().anyMatch(Symptom::isCharacteristic);
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
