package quarano.core.web;

import java.io.IOException;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

class EmptyStringDeserializer extends JsonDeserializer<String> {

	/*
	 * (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.DeserializationContext)
	 */
	@Nullable
	@Override
	@SuppressWarnings("null")
	public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

		return jp.getCurrentToken() == JsonToken.VALUE_STRING && !StringUtils.hasText(jp.getText()) //
				? null //
				: StringDeserializer.instance.deserialize(jp, ctxt).trim();
	}
}
