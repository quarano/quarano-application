package quarano.auth;

import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.auth.ActivationCode.ActivationCodeIdentifier;
import quarano.department.Department.DepartmentIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActivationCodeService {

	private final @NonNull ActivationCodeRepository activationCodes;
	private final @NonNull ActivationCodeProperties configuration;

	public Try<ActivationCode> createActivationCode(TrackedPersonIdentifier personIdentifier,
			DepartmentIdentifier departmentIdentifier) {

		ActivationCodes codes = activationCodes.findByTrackedPersonId(personIdentifier);

		if (codes.hasRedeemedCode()) {
			return Try.failure(ActivationCodeException.activationConcluded());
		}

		codes.map(ActivationCode::cancel) //
				.map(it -> it.map(activationCodes::save));

		return Try.ofSupplier(() -> activationCodes
				.save(new ActivationCode(configuration.getExpiryDate(), personIdentifier, departmentIdentifier)));
	}

	/**
	 * Checks if a code is valid and marks it as redeemed.
	 *
	 * @param identifier
	 */
	public Try<ActivationCode> redeemCode(ActivationCodeIdentifier identifier) {

		return findCode(identifier) //
				.flatMap(ActivationCode::redeem) //
				.map(activationCodes::save);
	}

	/**
	 * Returns the activationCode object if the activation exists and is still valid. Otherwise a specific exception is
	 * thrown.
	 *
	 * @param identifier
	 * @return
	 */
	public Try<ActivationCode> getValidCode(ActivationCodeIdentifier identifier) {

		return findCode(identifier) //
				.filter(ActivationCode::isExpired, ActivationCodeException::expired) //
				.filter(it -> !it.isWaitingForActivation(), ActivationCodeException::usedOrCanceled);
	}

	public Optional<ActivationCode> getPendingActivationCode(TrackedPersonIdentifier id) {

		ActivationCodes codes = activationCodes.findByTrackedPersonId(id);

		return codes.getPendingActivationCode();
	}

	private Try<ActivationCode> findCode(ActivationCodeIdentifier identifier) {

		return Try.ofSupplier(() -> activationCodes.findById(identifier) //
				.orElseThrow(() -> ActivationCodeException.notFound(identifier)));
	}
}
