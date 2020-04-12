package de.wevsvirushackathon.coronareport.client;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.wevsvirushackathon.coronareport.diary.DiaryEntryController;
import de.wevsvirushackathon.coronareport.registration.AccountRegistrationDetails;
import de.wevsvirushackathon.coronareport.registration.AccountRegistry;

@Component
public class ClientService {

	private AccountRegistry accountRegistry;
	private ClientRepository clientRepository;

	private final Logger log = LoggerFactory.getLogger(DiaryEntryController.class);

	@Autowired
	public ClientService(AccountRegistry accountRegistry, ClientRepository clientRepository) {
		super();
		this.accountRegistry = accountRegistry;
		this.clientRepository = clientRepository;
	}

	@Transactional
	void registerAccountForClient(AccountRegistrationDetails details) {
		
		Client client = clientRepository.findById(details.getClientId()).orElseThrow(() -> new EntityNotFoundException("No client with given id +'" + details.getClientId() +"' found."));
		accountRegistry.registerAccountForClient(client, details);

	}

	private boolean usernameIsValidAndUnique(String username) {
		// TODO Auto-generated method stub
		return false;
	}


	/**
	 * Creates a new unique client id
	 * 
	 * @return
	 */
	private String createNewClientId() {
		Client possiblyExistingClient;
		String newClientCode = "";
//		do {
//			newClientCode = UUID.randomUUID().toString();
//			//possiblyExistingClient = this.clientRepository.findByClientCode(newClientCode);
//		} while (possiblyExistingClient != null);
		return newClientCode;
	}
}
