package quarano.tracking.web;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.tracking.ContactPerson.ContactPersonIdentifier;

import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

/**
 * @author Oliver Drotbohm
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
class JacksonMarshallingIntegrationTests {

	private final ObjectMapper mapper;

	@Test
	@Disabled
	void rendersIdentifierForContactPersonDtoButDoesNotBindIt() throws Exception {

		var dto = new ContactPersonDto();
		dto.setId(ContactPersonIdentifier.of(UUID.randomUUID()));

		var result = mapper.writeValueAsString(dto);

		assertThat(JsonPath.parse(result).read("$.id", String.class)).isNotEmpty();

		dto = mapper.readValue(result, ContactPersonDto.class);

		assertThat(dto.getId()).isNull();
	}
}
