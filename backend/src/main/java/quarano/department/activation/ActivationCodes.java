package quarano.department.activation;

import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.Optional;

import org.springframework.data.util.Streamable;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
class ActivationCodes implements Streamable<ActivationCode> {

	private final Streamable<ActivationCode> codes;

	boolean hasRedeemedCode() {

		return codes.stream() //
				.anyMatch(ActivationCode::isRedeemed);
	}

	ActivationCodes getCancellableCodes() {

		return new ActivationCodes(codes.stream() //
				.filter(ActivationCode::isWaitingForActivation) //
				.collect(Streamable.toStreamable()));
	}

	Optional<ActivationCode> getPendingActivationCode() {

		return codes.stream() //
				.filter(ActivationCode::isWaitingForActivation) //
				.findFirst();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<ActivationCode> iterator() {
		return codes.iterator();
	}
}
