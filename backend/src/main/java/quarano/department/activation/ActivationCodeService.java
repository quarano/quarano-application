package quarano.department.activation;

import static java.util.function.Predicate.*;

import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Department.DepartmentIdentifier;
import quarano.department.activation.ActivationCode.ActivationCodeIdentifier;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * @author Oliver Drotbohm
 */
@Transactional
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
				.map(it -> it.cancelAll(activationCodes::save))
				.map(__ -> new ActivationCode(configuration.getExpiryDate(), personIdentifier, departmentIdentifier))
				.map(activationCodes::save);
	}

	/**
	 * Checks if a code is valid and marks it as redeemed.
	 *
	 * @param identifier
	 * @return will never be {@literal null}.
	 */
	public Try<ActivationCode> redeemCode(ActivationCodeIdentifier identifier) {

		return findCode(identifier)
				.flatMap(ActivationCode::redeem)
				.map(activationCodes::save);
	}

	/**
	 * Cancels the {@link ActivationCode} with the given identifier.
	 *
	 * @param identifier must not be {@literal null}.
	 * @return the cancelled {@link ActivationCode}, will never be {@literal null}.
	 */
	public Try<ActivationCode> cancelCode(ActivationCodeIdentifier identifier) {

		return findCode(identifier)
				.flatMap(ActivationCode::cancel)
				.map(activationCodes::save);
	}

	/**
	 * Returns the {@link ActivationCode} if the activation exists and is still valid.
	 *
	 * @param identifier must not be {@literal null}.
	 * @return the valid {@link ActivationCode}, will never be {@literal null}.
	 */
	public Try<ActivationCode> getValidCode(ActivationCodeIdentifier identifier) {

		Assert.notNull(identifier, "ActivationCode identifier must not be null!");

		return findCode(identifier)
				.filter(ActivationCode::isNotExpired, ActivationCodeException::expired)
				.filter(ActivationCode::isWaitingForActivation, ActivationCodeException::usedOrCanceled);
	}

	/**
	 * Returns the pending {@link ActivationCode} for the {@link TrackedPerson} identified by the given
	 * {@link TrackedPersonIdentifier} if available.
	 *
	 * @param identifier must not be {@literal null}.
	 * @return will never be {@literal null}.
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
