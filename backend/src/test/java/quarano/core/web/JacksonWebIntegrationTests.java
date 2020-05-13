package quarano.core.web;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
@QuaranoWebIntegrationTest
class JacksonWebIntegrationTests {

	private final ObjectMapper jackson;

	@Test
	void turnsEmptyStringIntoNull() throws Exception {
		assertThat(jackson.readValue("{ \"name\" : \"\" }", Sample.class).name).isNull();
		assertThat(jackson.readValue("{ \"name\" : \" \" }", Sample.class).name).isNull();
	}

	@Test
	void trimsStringValuesOnBind() throws Exception {
		assertThat(jackson.readValue("{ \"name\" : \" foo bar \" }", Sample.class).name).isEqualTo("foo bar");
	}

	@JsonAutoDetect(fieldVisibility = Visibility.ANY)
	static class Sample {
		String name;
	}
}
