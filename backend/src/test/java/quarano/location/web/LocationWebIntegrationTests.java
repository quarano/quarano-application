package quarano.location.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.location.LocationRepository;

@QuaranoWebIntegrationTest
@RequiredArgsConstructor
public class LocationWebIntegrationTests {
    private final MockMvc mvc;
    private final ObjectMapper jackson;
    private final @NonNull LocationRepository locations;

    @Test
    @WithQuaranoUser("test3")
    void simplePositiveCreateSuccess() throws Exception {
        var source = createTestLocation();
        var response = mvc.perform(post("/api/locations")
                .content(jackson.writeValueAsString(source))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var document = JsonPath.parse(response);

        assertThat(document.read("$.id", String.class)).isNotEmpty();
        assertThat(document.read("$.street", String.class)).isEqualTo(source.getStreet());
        assertThat(document.read("$.houseNumber", String.class)).isEqualTo(source.getHouseNumber());
        assertThat(document.read("$.city", String.class)).isEqualTo(source.getCity());
        assertThat(document.read("$.zipCode", String.class)).isEqualTo(source.getZipCode());
        assertThat(document.read("$.contactInfo", String.class)).isEqualTo(source.getContactInfo());
        assertThat(document.read("$.organization", String.class)).isEqualTo(source.getOrganization());
        assertThat(document.read("$.longitude", Double.class)).isEqualTo(source.getLongitude());
        assertThat(document.read("$.latitude", Double.class)).isEqualTo(source.getLatitude());
    }

    private LocationDto createTestLocation() {
        LocationDto location = new LocationDto();
        location.setCity("Mannheim");
        location.setZipCode("68167");
        location.setStreet("Weylstraße");
        location.setHouseNumber("9");
        location.setContactInfo("Tel. 0621 333644");
        location.setLatitude(49.47620810813399);
        location.setLongitude(8.419857459960953);
        location.setOrganization("Städt. Kindergarten Mannheim, Baden-Württemberg");
        return location;
    }
}
