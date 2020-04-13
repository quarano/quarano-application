package quarano.registration;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

import org.jddd.core.types.Identifier;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import quarano.core.QuaranoAggregate;
import quarano.registration.ActivationCode.ActivationCodeIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

@Entity
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ActivationCode extends QuaranoAggregate<ActivationCode, ActivationCodeIdentifier>{
	
	private @Getter LocalDateTime expirationTime;
	private @Getter TrackedPersonIdentifier trackedPersonId;
	private @Getter ActivationCodeStatus status;
	private @Getter int activationTries;
	
	public ActivationCode(LocalDateTime expirationTime, TrackedPersonIdentifier trackedPersonId) {

		this.expirationTime = expirationTime;
		this.trackedPersonId = trackedPersonId;
		this.id = ActivationCodeIdentifier.of(UUID.randomUUID());
		this.status = ActivationCodeStatus.WAITING_FOR_ACTIVATION;
		
	}
	
	// for testing purposes
	ActivationCode(LocalDateTime expirationTime, TrackedPersonIdentifier trackedPersonId, ActivationCodeIdentifier uuid) {

		this.expirationTime = expirationTime;
		this.trackedPersonId = trackedPersonId;
		this.id = uuid;
		this.status = ActivationCodeStatus.WAITING_FOR_ACTIVATION;
		
	}
	
	
	/**
	 * Determines if the code has expired
	 * @return
	 */
	public boolean isExpired() {
		return LocalDateTime.now().isAfter(expirationTime);
	}
	
	
	/**
	 * Checks validity of the code and sets the state of the code to REDEEMED.
	 * Only possible if code was in status 'WAITING_FOR_ACTIVATION' before.
	 * @return
	 */
	public boolean redeem() {
		
		if(this.isValid() )
		{
			this.status = ActivationCodeStatus.REDEEMED;
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Deactivates an existing code, so that it cannot be used anymore
	 * @return
	 */
	public boolean cancel() {
		
		if(this.status == ActivationCodeStatus.WAITING_FOR_ACTIVATION) {
			this.status = ActivationCodeStatus.CANCELED;
			return true;
		}
		
		return false;

	}
	
	
	public boolean isValid() {
		return !isExpired() && this.status == ActivationCodeStatus.WAITING_FOR_ACTIVATION;
	}


	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class ActivationCodeIdentifier implements Identifier, Serializable {
		private static final long serialVersionUID = 7871473225101042167L;
		final UUID id;
		
		public String toString() {
			return id.toString();
		}
	}

}
