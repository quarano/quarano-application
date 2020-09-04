package quarano.reference;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumMap;

import org.junit.jupiter.api.Test;

class TranslationConverterUnitTest {

	EnumMap<Language, String> example = new EnumMap<>(Language.class);
	String jsonExample = "{\"DE\":\"Das ist ein Beispieltext\",\"EN\":\"This is a sample text\",\"TR\":\"Bu örnek bir metindir\",\"RU\":\"Это образец текста\"}";
	TranslationConverter instance = new TranslationConverter();

	public TranslationConverterUnitTest() {
		example.put(Language.DE, "Das ist ein Beispieltext");
		example.put(Language.TR, "Bu örnek bir metindir");
		example.put(Language.EN, "This is a sample text");
		example.put(Language.RU, "Это образец текста");
	}

	@Test
	public void checkSerialization() {
		assertThat(instance.convertToDatabaseColumn(example)).endsWith(jsonExample);
	}

	@Test
	public void checkDeserialization() {
		assertThat(instance.convertToEntityAttribute(jsonExample))
				.containsKeys(Language.DE, Language.TR, Language.EN, Language.RU);
	}
}
