package de.wevsvirushackathon.coronareport.client;

import static de.wevsvirushackathon.coronareport.TestUtil.fromJson;
import static de.wevsvirushackathon.coronareport.TestUtil.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import de.wevsvirushackathon.coronareport.firstReport.FirstReport;
import de.wevsvirushackathon.coronareport.healthdepartment.HealthDepartment;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class ClientControllerIT {

    @Autowired
    private MockMvc mvc;

    @Test
    public void registerNewClient()
            throws Exception {

        final ClientDto clientDto = ClientDto.builder()
                .surename("Sarah")
                .firstname("Bauer")
                .phone("Testamt1")
                .infected(true)
                .zipCode("12345")
                .build();

        String resultClientId = mvc.perform(
                post("/client/register")
                .header("Origin", "*")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(clientDto))).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn().getResponse().getContentAsString();

        Assertions.assertNotNull(resultClientId);
        
    }
    
    
    @Test
    public void testLoginWithValidCredentials()
            throws Exception {
    	
    	// Accounts and password created by dummy data input beans
    	// given
    	final String username = "DemoAccount";
    	final String password = "DemoPassword";
    	
        final HealthDepartment hd1 = HealthDepartment.builder().fullName("Testamt 1")
                .id("Testamt1").passCode(UUID.fromString("aba0ec65-6c1d-4b7b-91b4-c31ef16ad0a2")).build();
        
        final ClientDto expectedClientDto = ClientDto.builder().firstname("Fabian")
        		.surename("Bauer").infected(true).clientCode("738d3d1f-a9f1-4619-9896-2b5cb3a89c22")
        		.healthDepartment(hd1)
        		.phone("0175 664845454").zipCode("66845")
        		.build();
    	String requestbody = createLoginRequestBody(username, password);
    	
    	// when
        String resultLogin = mvc.perform(
                post("/login")
                .header("Origin", "*")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestbody)
        		)
        		
        		.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();  
        TokenResponse response = fromJson(resultLogin, TokenResponse.class);
        
        
        // check if  token is valid for authentication
        String resultDtoStr = mvc.perform(
                get("/client/me" )
                        .header("Origin", "*")
                        .header("Authorization", "Bearer " + response.getToken())
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        ClientDto resultDto = fromJson(resultDtoStr, ClientDto.class);
        
        
        // then
        expectedClientDto.setClientId(resultDto.getClientId());
        expectedClientDto.setClientCode(resultDto.getClientCode());

        Assertions.assertEquals(expectedClientDto, resultDto);
	
    }
    
    @Test
    public void testLoginWithInvalidCredentials()
            throws Exception {
    	
    	// Accounts and password created by dummy data input beans
    	// given
    	final String username = "DemoAccount";
    	final String password = "My-Wrong-Password";
    	
        final HealthDepartment hd1 = HealthDepartment.builder().fullName("Testamt 1")
                .id("Testamt1").passCode(UUID.fromString("aba0ec65-6c1d-4b7b-91b4-c31ef16ad0a2")).build();
        
    	String requestbody = createLoginRequestBody(username, password);
    	
    	// when
        String resultLogin = mvc.perform(
                post("/login")
                .header("Origin", "*")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestbody)
        		)
        		
        		.andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();  
        TokenResponse response = fromJson(resultLogin, TokenResponse.class);
        	
    }    


	private String createLoginRequestBody(String username, String password) {
		return "{ \"username\": \""+ username + "\", \"password\": \"" + password + "\" }";
	}
    
}