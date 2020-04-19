package quarano.registration;

import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.department.Department.DepartmentIdentifier;
import quarano.registration.ActivationCode.ActivationCodeIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActivationCodeService {

	private final @NonNull ActivationCodeRepository activationCodes;
	private final @NonNull ActivationCodeProperties configuration;

	public ActivationCode createActivationCode(TrackedPersonIdentifier personIdentifier,
			DepartmentIdentifier departmentIdentifier) {

		return activationCodes
				.save(new ActivationCode(configuration.getExpiryDate(), personIdentifier, departmentIdentifier));
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

	private Try<ActivationCode> findCode(ActivationCodeIdentifier identifier) {

		return Try.ofSupplier(() -> activationCodes.findById(identifier) //
				.orElseThrow(() -> ActivationCodeException.notFound(identifier)));
	}
}
