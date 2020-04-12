package de.wevsvirushackathon.coronareport.registration;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

import org.jddd.core.types.Identifier;

import de.wevsvirushackathon.coronareport.registration.ActivationCode.ActivationCodeIdentifier;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import quarano.core.QuaranoAggregate;

@Entity
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ActivationCode extends QuaranoAggregate<ActivationCode, ActivationCodeIdentifier>{
	
	private @Getter LocalDateTime expirationTime;
	private @Getter Long clientId;
	private @Getter ActivationCodeStatus status;
	
	public ActivationCode(LocalDateTime expirationTime, Long clientId) {

		this.expirationTime = expirationTime;
		this.clientId = clientId;
		this.id = ActivationCodeIdentifier.of(UUID.randomUUID());
		this.status = ActivationCodeStatus.WAITING_FOR_ACTIVATION;
		
	}
	
	// for testing purposes
	ActivationCode(LocalDateTime expirationTime, Long clientId, UUID uuid) {

		this.expirationTime = expirationTime;
		this.clientId = clientId;
		this.id = ActivationCodeIdentifier.of(uuid);
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
	 * @param clientId
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
