package quarano.core.web;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.core.web.QuaranoWebConfiguration.ErrorsSerializer.ErrorsJson;
import quarano.util.TestUtils;

import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.validation.MapBindingResult;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
@QuaranoWebIntegrationTest
class JacksonWebIntegrationTests {

	private static final String I18NED_PATTERN_CITY = "Bitte geben Sie eine gültige Stadt an!";

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

	@Test // CORE-442
	void i18nizesMessagesWhenRenderingAnError() throws Exception {

		MapBindingResult result = new MapBindingResult(Map.of(), "test");
		result.rejectValue("field", "Pattern.city");

		ErrorsJson source = new ErrorsJson(result);

		var rendered = TestUtils.executeWithLocale(Locale.GERMAN, () -> jackson.writeValueAsString(source));

		assertThat(JsonPath.parse(rendered).read("$.field", String.class)).isEqualTo(I18NED_PATTERN_CITY);
	}

	@Test // CORE-442
	void rendersSimpleErrorsWithDetails() throws Exception {

		MapBindingResult result = new MapBindingResult(Map.of(), "test");
		result.rejectValue("field", "Pattern.city");

		var errors = ErrorsWithDetails.of(result);
		errors.addDetails("anotherField", Map.of("key", "value"));

		var rendered = JsonPath.parse(jackson.writeValueAsString(errors));

		assertThat(rendered.read("$.anotherField.key", String.class)).isEqualTo("value");
	}

	@Test // CORE-442
	void overridesFieldErrorWithDetails() throws Exception {

		MapBindingResult result = new MapBindingResult(Map.of(), "test");
		result.rejectValue("field", "Pattern.city");

		var errors = ErrorsWithDetails.of(result);
		errors.addDetails("field", Map.of("key", "value"));

		var rendered = TestUtils.executeWithLocale(Locale.GERMAN, () -> jackson.writeValueAsString(errors));

		assertThat(JsonPath.parse(rendered).read("$.field.key", String.class)).isEqualTo("value");
	}

	@Test // CORE-442
	void rendersMessageSourceResolvableWithinDetails() throws Exception {

		MapBindingResult result = new MapBindingResult(Map.of(), "test");
		result.rejectValue("field", "Pattern.city");

		var errors = ErrorsWithDetails.of(result)
				.addDetails("field", Map.of("key", I18nedMessage.of("Pattern.city")));

		var rendered = TestUtils.executeWithLocale(Locale.GERMAN, () -> jackson.writeValueAsString(errors));

		assertThat(JsonPath.parse(rendered).read("$.field.key", String.class)).isEqualTo(I18NED_PATTERN_CITY);
	}

	@JsonAutoDetect(fieldVisibility = Visibility.ANY)
	static class Sample {
		String name;
	}
}
