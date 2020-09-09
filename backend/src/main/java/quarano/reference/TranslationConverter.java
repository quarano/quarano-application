package quarano.reference;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Converter
public class TranslationConverter implements AttributeConverter<Map<Language, String>, String> {

	ObjectMapper mapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(Map<Language, String> data) {
		try {
			return mapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
		    log.error("Deserialization of translation array failed");
			return "{}";
		}
	}

	@Override
	public Map<Language, String> convertToEntityAttribute(String data) {
        var mapValue = new HashMap<Language, String>();
        if(data == null) {
        	return mapValue;
		}
		var typeRef = new TypeReference<HashMap<Language, String>>() {};
		try {
			mapValue = mapper.readValue(data, typeRef);
		} catch (IOException e) {
		    log.error("Serialization of the translation array failed");
		}
		return mapValue;
	}

}
