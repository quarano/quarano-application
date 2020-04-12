package de.wevsvirushackathon.coronareport.registration;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.wevsvirushackathon.coronareport.client.Client;
import de.wevsvirushackathon.coronareport.client.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
class DummyDataInitializer implements ApplicationListener<ApplicationReadyEvent> {
	
	private final ActivationCodeRepository codeRepo;
	private final ClientRepository clientRepo;

	@Override
	@Transactional
	public void onApplicationEvent(ApplicationReadyEvent event) {

		Client client = clientRepo.findByClientCode("738d3d1f-a9f1-4619-9896-2b5cb3a89c22");
		
		ActivationCode codeClient = new ActivationCode(LocalDateTime.of(2025, 10, 10, 0, 0), client.getClientId(), UUID.fromString("acc8b747-1eac-4db4-a8f3-d2a8bbe8320d"));
		log.warn("Adding dummy activation code '" + codeClient.getId().toString() + "'  for client '" + client.getClientId());
				
		codeRepo.save(codeClient);
        
		
	}
}
