package de.wevsvirushackathon.coronareport.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static de.wevsvirushackathon.coronareport.TestUtil.fromJson;
import static de.wevsvirushackathon.coronareport.TestUtil.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .healthDepartmentId("1234")
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

        String resultDtoStr = mvc.perform(
                get("/client/" + resultClientId)
                        .header("Origin", "*")
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        ClientDto resultDto = fromJson(resultDtoStr, ClientDto.class);

        clientDto.setClientId(resultDto.getClientId());
        clientDto.setClientCode(resultDto.getClientCode());

        Assertions.assertEquals(clientDto, resultDto);
    }
}