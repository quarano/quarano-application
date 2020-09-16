package quarano.reference;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;
import quarano.user.LocaleConfiguration;

class TranslationConverterUnitTest {

	Locale localeRu = new Locale("ru");
	Map<Locale, String> example = new HashMap<>();
	String jsonExample = "{\"de\":\"Das ist ein Beispieltext\",\"tr\":\"Bu örnek bir metindir\",\"en\":\"This is a sample text\",\"ru\":\"Это образец текста\"}";
	TranslationConverter instance = new TranslationConverter();

	public TranslationConverterUnitTest() {
		example.put(Locale.GERMAN, "Das ist ein Beispieltext");
		example.put(Locale.ENGLISH, "This is a sample text");
		example.put(localeRu, "Это образец текста");
		example.put(LocaleConfiguration.TURKISH, "Bu örnek bir metindir");
	}

	@Test
	public void checkSerialization() {
		assertThat(instance.convertToDatabaseColumn(example)).endsWith(jsonExample);
	}

	@Test
	public void checkDeserialization() {

		assertThat(instance.convertToEntityAttribute(jsonExample))
				.containsKeys(Locale.GERMAN, LocaleConfiguration.TURKISH, Locale.ENGLISH, localeRu);
	}
}
