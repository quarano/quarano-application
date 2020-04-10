package de.wevsvirushackathon.coronareport.client;

import static de.wevsvirushackathon.coronareport.TestUtil.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import de.wevsvirushackathon.coronareport.TestDataProvider;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ClientControllerIT {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private TestDataProvider testData;

	@Test
	public void registerNewClient() throws Exception {

		final ClientDto clientDto = ClientDto.builder().surename("Sarah").firstname("Bauer").phone("Testamt1")
				.infected(true).zipCode("12345").build();

		String resultClientId = mvc
				.perform(post("/client/register").header("Origin", "*").contentType(MediaType.APPLICATION_JSON)
						.content(toJson(clientDto)))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
				.andReturn().getResponse().getContentAsString();

		Assertions.assertNotNull(resultClientId);

	}
	
	@Test
	public void testCheckClientCodeWithValidCode() throws Exception {
		
		String resultAsString = mvc
				.perform(get("/client/checkcode/" + TestDataProvider.validClientCode()).header("Origin", "*").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
		Assertions.assertTrue(Boolean.parseBoolean(resultAsString));
		
	}
	
	
	@Test
	public void testCheckClientCodeWithInvalidCodeFailsWith404() throws Exception {
		
		mvc
			.perform(get("/client/checkcode/"  + TestDataProvider.invalidClientCode()).header("Origin", "*").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andReturn().getResponse().getContentAsString();
	}
	
	@Test
	public void testCheckClientCodeWithoutCodeFailsWith400() throws Exception {
		
		mvc
			.perform(get("/client/checkcode/" ).header("Origin", "*").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn().getResponse().getContentAsString();
	}
	

}