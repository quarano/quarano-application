package quarano.department.activation;

import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Department.DepartmentIdentifier;
import quarano.department.activation.ActivationCode.ActivationCodeIdentifier;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class ActivationCodeService {

	private final @NonNull ActivationCodeRepository activationCodes;
	private final @NonNull ActivationCodeProperties configuration;

	/**
	 * Creates a new {@link ActivationCode} for the {@link TrackedPerson} with the given identifier and department.
	 * Already existing codes will be canceled and become unusable for activation.
	 *
	 * @param personIdentifier must not be {@literal null}.
	 * @param departmentIdentifier must not be {@literal null}.
	 * @return
	 */
	public Try<ActivationCode> createActivationCode(TrackedPersonIdentifier personIdentifier,
			DepartmentIdentifier departmentIdentifier) {

		Assert.notNull(personIdentifier, "TrackedPerson identifier must not be null!");
		Assert.notNull(departmentIdentifier, "Department identifier must not be null!");

		return Try.success(personIdentifier)
				.map(activationCodes::findByTrackedPersonId)
				.filter(it -> !it.hasRedeemedCode(), ActivationCodeException::activationConcluded)
				.map(it -> it.cancelAll(activationCodes::save))
				.map(__ -> new ActivationCode(configuration.getExpiryDate(), personIdentifier, departmentIdentifier))
				.map(activationCodes::save);
	}

	/**
	 * Checks if a code is valid and marks it as redeemed.
	 *
	 * @param identifier
	 */
	public Try<ActivationCode> redeemCode(ActivationCodeIdentifier identifier) {

		return findCode(identifier)
				.flatMap(ActivationCode::redeem)
				.map(activationCodes::save);
	}

	/**
	 * Returns the activationCode object if the activation exists and is still valid. Otherwise a specific exception is
	 * thrown.
	 *
	 * @param identifier must not be {@literal null}.
	 * @return
	 */
	public Try<ActivationCode> getValidCode(ActivationCodeIdentifier identifier) {

		Assert.notNull(identifier, "ActivationCode identifier must not be null!");

		return findCode(identifier)
				.filter(ActivationCode::isNotExpired, ActivationCodeException::expired)
				.filter(ActivationCode::isWaitingForActivation, ActivationCodeException::usedOrCanceled);
	}

	/**
	 * Returns the pending {@link ActivationCode} for the {@link TrackedPerson} identified by the given id if available.
	 *
	 * @param identifier must not be {@literal null}.
	 * @return
	 */
	public Optional<ActivationCode> getPendingActivationCode(TrackedPersonIdentifier identifier) {

		Assert.notNull(identifier, "TrackedPerson identifier must not be null!");

		return activationCodes.findByTrackedPersonId(identifier)
				.getPendingActivationCode();
	}

	private Try<ActivationCode> findCode(ActivationCodeIdentifier identifier) {

		return Try.ofSupplier(() -> activationCodes.findById(identifier)
				.orElseThrow(() -> ActivationCodeException.notFound(identifier)));
	}
}
