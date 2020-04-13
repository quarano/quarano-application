package quarano.registration;

import static quarano.tracking.TrackedPersonDataInitializer.INDEX_PERSON1_NOT_REGISTERED;
import static quarano.tracking.TrackedPersonDataInitializer.INDEX_PERSON2_IN_ENROLLMENT;
import static quarano.tracking.TrackedPersonDataInitializer.INDEX_PERSON3_WITH_ACTIVE_TRACKING;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.registration.ActivationCode.ActivationCodeIdentifier;

/**
 * Create some dummy accounts for test and development
 * 
 * @author Patrick Otto
 *
 */
@Component
@Order(500)
@Slf4j
@RequiredArgsConstructor
class ActivationCodeDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

	private final ActivationCodeRepository codeRepo;
	
	private static final ActivationCode ACTIVATIONCODE_PERSON1 = new ActivationCode(LocalDateTime.of(2025, 10, 10, 0, 0),
			INDEX_PERSON1_NOT_REGISTERED.getId(),
			ActivationCodeIdentifier.of(UUID.fromString("acc8b747-1eac-4db4-a8f3-d2a8bbe8320d")));
	
	private static final ActivationCode ACTIVATIONCODE_PERSON2_REDEEMED = new ActivationCode(LocalDateTime.of(2025, 10, 10, 0, 0),
			INDEX_PERSON2_IN_ENROLLMENT.getId(),
			ActivationCodeIdentifier.of(UUID.fromString("85829049-9c4c-4a20-a854-70e813adaab4")));

	private static final ActivationCode ACTIVATIONCODE_PERSON3_CANCELED = new ActivationCode(LocalDateTime.of(2020, 04, 10, 0, 0),
			INDEX_PERSON3_WITH_ACTIVE_TRACKING.getId(),
			ActivationCodeIdentifier.of(UUID.fromString("dc304a58-082a-4b9c-a635-374082658561")));
	
	private static final ActivationCode ACTIVATIONCODE_PERSON3_REDEEMED = new ActivationCode(LocalDateTime.of(2025, 10, 10, 0, 0),
			INDEX_PERSON3_WITH_ACTIVE_TRACKING.getId(),
			ActivationCodeIdentifier.of(UUID.fromString("80e43d28-24f9-4781-9d0c-f9d722987803")));	
	
	private static List<ActivationCode> CODES = new ArrayList<>();
	
	static {
		CODES.add(ACTIVATIONCODE_PERSON1);
		CODES.add(ACTIVATIONCODE_PERSON2_REDEEMED);
		CODES.add(ACTIVATIONCODE_PERSON3_CANCELED);
		CODES.add(ACTIVATIONCODE_PERSON3_REDEEMED);
	}
	


	@Override
	@Transactional
	public void onApplicationEvent(ApplicationReadyEvent event) {

		// set status of codes
		ACTIVATIONCODE_PERSON2_REDEEMED.redeem();
		
		ACTIVATIONCODE_PERSON3_CANCELED.cancel();
		ACTIVATIONCODE_PERSON3_REDEEMED.redeem();
		
		// store each code
		for(ActivationCode code: CODES) {
			log.warn("Test data: Adding  activation code '" + code.getId().toString() + "'  for client '"
					+ code.getId().toString() + "' with status '" + code.getStatus() +"'");
			codeRepo.save(code);
		}
		
	}
}
