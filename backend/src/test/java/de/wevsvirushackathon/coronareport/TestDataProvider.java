package de.wevsvirushackathon.coronareport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.wevsvirushackathon.coronareport.client.Client;
import de.wevsvirushackathon.coronareport.client.ClientDto;

@Component
public class TestDataProvider {
	
	private static final String INVALID_CLIENT_CODE = "xxxxxxx-xxxx-xxxx-xxx-xxxxxxxxx";
	
	@Autowired
	private DummyDataInputBean dummyData;

	public static String validClientCode() {
		return DummyDataInputBean.VALID_CLIENT_CODE_DEP1;
	}
	
	public static String invalidClientCode() {
		return INVALID_CLIENT_CODE;
	}

	public static ClientDto clientDtoWithRegistrationCompleted() {
		final ClientDto clientDto = ClientDto.builder().surename("Sarah").firstname("Bauer").phone("Testamt1")
				.infected(true).zipCode("12345").completedContactRetro(true).completedPersonalData(true)
				.completedQuestionnaire(true).build();
		return clientDto;
	}
	
	public static ClientDto clientDtoWithRegistartionIncomplete() {
		final ClientDto clientDto = ClientDto.builder().surename("Sarah").firstname("Bauer").phone("Testamt1")
				.infected(true).zipCode("12345").build();
		return clientDto;
	}
	
	public Client clientWithNoAccount() {
		return dummyData.getClientWithNoAccount();
	}

}
