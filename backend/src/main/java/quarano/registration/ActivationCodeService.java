package quarano.registration;

import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.registration.ActivationCode.ActivationCodeIdentifier;

@Component
@RequiredArgsConstructor
public class ActivationCodeService {
	
	private final @NonNull ActivationCodeRepository codeRepo;


	/**
	 * Checks if a code is valid and marks it as redeemed.
	 * @param codeID
	 * @throws CodeNotFoundException
	 * @throws ActivationCodeExpiredException
	 * @throws ActivationNotActiveException 
	 */
	public ActivationCode redeemCode(ActivationCodeIdentifier codeID) throws CodeNotFoundException, ActivationCodeExpiredException, ActivationNotActiveException {
		
		ActivationCode activationCode = getCodeIfValid(codeID);
		
		activationCode.redeem();
		
		return codeRepo.save(activationCode);
	}
	
	
	/**
	 * Returns the activationCode object if the activation exists and is still valid. Otherwise a specific exception is thrown.
	 * @param code
	 * @return
	 * @throws CodeNotFoundException
	 * @throws ActivationCodeExpiredException
	 * @throws ActivationNotActiveException 
	 */
	public ActivationCode getCodeIfValid(ActivationCodeIdentifier code) throws CodeNotFoundException, ActivationCodeExpiredException, ActivationNotActiveException {
		
		ActivationCode activationCode = codeRepo.findById(code)
				.orElseThrow(() -> new CodeNotFoundException("ActivationCode '" + code.toString() + "' does not exist"));
		
		if(!activationCode.isValid()) {
			
			if(activationCode.isExpired()){
				throw new ActivationCodeExpiredException("ActivationCode '" + code.toString() + "' has expired on "  + activationCode.getExpirationTime());
			}
			
			if(activationCode.getStatus() != ActivationCodeStatus.WAITING_FOR_ACTIVATION)
			{
				throw new ActivationNotActiveException("ActivationCode '" + code.toString() + "' was deactivated or is already in use");
			}
			
		}
		
		return activationCode;
		
	}
	

}
