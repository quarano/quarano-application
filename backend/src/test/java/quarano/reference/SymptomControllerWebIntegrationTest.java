package quarano.reference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;

import java.util.UUID;

@QuaranoWebIntegrationTest
@WithQuaranoUser("test3")
@RequiredArgsConstructor
class SymptomControllerWebIntegrationTest {
    private final MockMvc mvc;
    private final ObjectMapper mapper;

    @Test
    void rejectsSymptomWithNameContainingNumbersAndSpecialCharacters() throws Exception {
        var payload = new SymptomDto();
        payload.setId(UUID.randomUUID());
        payload.setName("some invalid name 1231 __\\");

        String response = mvc.perform(post("/api/symptoms") //
                .content(mapper.writeValueAsString(payload)) //
                .contentType(MediaType.APPLICATION_JSON)) //
                .andExpect(status().isBadRequest()) //
                .andReturn().getResponse().getContentAsString();

        var document = JsonPath.parse(response);

        assertThat(document.read("$.name", String.class)).isEqualTo("Dieses Feld darf nur Buchstaben enthalten!");
    }
}
