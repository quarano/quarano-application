package quarano.auth;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import quarano.auth.ActivationCode.ActivationCodeIdentifier;
import quarano.core.QuaranoAggregate;
import quarano.department.Department.DepartmentIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

import org.jddd.core.types.Identifier;

@Entity
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ActivationCode extends QuaranoAggregate<ActivationCode, ActivationCodeIdentifier> {

	private @Getter LocalDateTime expirationTime;
	private @Getter TrackedPersonIdentifier trackedPersonId;
	private @Getter ActivationCodeStatus status;
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

	/**
	 * Checks validity of the code and sets the state of the code to REDEEMED. Only possible if code was in status
	 * 'WAITING_FOR_ACTIVATION' before.
	 *
	 * @return
	 */
	Try<ActivationCode> redeem() {

		return Try.success(this) //
				.filter(it -> !it.isExpired(), ActivationCodeException::expired) //
				.filter(it -> it.isWaitingForActivation(), ActivationCodeException::usedOrCanceled) //
				.onSuccess(it -> it.status = ActivationCodeStatus.REDEEMED); //
	}

	public boolean isRedeemed() {
		return status == ActivationCodeStatus.REDEEMED;
	}

	/**
	 * Deactivates an existing code, so that it cannot be used anymore
	 *
	 * @return
	 */
	public Try<ActivationCode> cancel() {

		return Try.success(this) //
				.filter(ActivationCode::isRedeemed, ActivationCodeException::usedOrCanceled)
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
		final UUID id;

		@Override
		public String toString() {
			return id.toString();
		}
	}

	public enum ActivationCodeStatus {
		WAITING_FOR_ACTIVATION, REDEEMED, CANCELED
	}

}
