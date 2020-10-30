package quarano.tracking;

import lombok.RequiredArgsConstructor;
import quarano.masterdata.Symptom;

import java.util.Iterator;
import java.util.List;

import org.springframework.data.util.Streamable;

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
