package quarano.department.activation;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import quarano.account.Account;
import quarano.account.Department.DepartmentIdentifier;
import quarano.core.QuaranoAggregate;
import quarano.department.activation.ActivationCode.ActivationCodeIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.jmolecules.ddd.types.Identifier;

/**
 * @author Patrick Otto
 * @author Oliver Drotbohm
 * @author Michael J. Simons
 * @author Felix Schultze
 */
@Entity
@ToString
@Table(name = "activation_codes")
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivationCode extends QuaranoAggregate<ActivationCode, ActivationCodeIdentifier> {

	private @Getter LocalDateTime expirationTime;
	private @Getter TrackedPersonIdentifier trackedPersonId;
	private @Getter @Enumerated(EnumType.STRING) ActivationCodeStatus status;
	private @Getter int activationTries;
	private @Getter DepartmentIdentifier departmentId;

	public ActivationCode(LocalDateTime expirationTime, TrackedPersonIdentifier trackedPersonId,
			DepartmentIdentifier departmentId) {
		this(expirationTime, trackedPersonId, departmentId, ActivationCodeIdentifier.of(UUID.randomUUID()));
	}

	// for testing purposes
	ActivationCode(LocalDateTime expirationTime, TrackedPersonIdentifier trackedPersonId,
			DepartmentIdentifier departmentId, ActivationCodeIdentifier uuid) {

		this.departmentId = departmentId;
		this.expirationTime = expirationTime;
		this.trackedPersonId = trackedPersonId;
		this.id = uuid;
		this.status = ActivationCodeStatus.WAITING_FOR_ACTIVATION;

	}

	/**
	 * Determines if the code has expired
	 *
	 * @return
	 */
	public boolean isExpired() {
		return LocalDateTime.now().isAfter(expirationTime);
	}

	public boolean isNotExpired() {
		return !isExpired();
	}

	public boolean isRedeemed() {
		return status == ActivationCodeStatus.REDEEMED;
	}

	public boolean isCancelled() {
		return status == ActivationCodeStatus.CANCELED;
	}

	/**
	 * Checks validity of the code and sets the state of the code to REDEEMED. Only possible if code was in status
	 * 'WAITING_FOR_ACTIVATION' before.
	 *
	 * @return
	 */
	Try<ActivationCode> redeem() {

		return Try.success(this)
				.filter(it -> !it.isExpired(), ActivationCodeException::expired)
				.filter(it -> it.isWaitingForActivation(), ActivationCodeException::usedOrCanceled)
				.onSuccess(it -> it.status = ActivationCodeStatus.REDEEMED);
	}

	/**
	 * Deactivates an existing code, so that it cannot be used to activate an {@link Account} anymore. TODO: Should cancel
	 * be an idempotent operation? Currently we can only cancel {@link ActivationCode}s that are waiting for activation.
	 * Trying to cancel already cancelled ones
	 *
	 * @return
	 */
	Try<ActivationCode> cancel() {

		var result = Try.success(this);

		if (isCancelled()) {
			return result;
		}

		return result
				.filter(ActivationCode::isWaitingForActivation, ActivationCodeException::usedOrCanceled)
				.onSuccess(it -> it.status = ActivationCodeStatus.CANCELED);
	}

	public boolean isValid() {
		return !isExpired() && this.status == ActivationCodeStatus.WAITING_FOR_ACTIVATION;
	}

	/**
	 * @return
	 */
	public boolean isWaitingForActivation() {
		return status == ActivationCodeStatus.WAITING_FOR_ACTIVATION;
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class ActivationCodeIdentifier implements Identifier, Serializable {
		private static final long serialVersionUID = 7871473225101042167L;

		final UUID activationCodeId;

		@Override
		public String toString() {
			return activationCodeId.toString();
		}
	}

	public enum ActivationCodeStatus {
		WAITING_FOR_ACTIVATION, REDEEMED, CANCELED
	}
}
