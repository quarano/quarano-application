package quarano.reference;

import lombok.*;
import quarano.core.validation.Alphabetic;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SymptomDto {

	@Getter(onMethod = @__(@JsonProperty))
	@Setter(onMethod = @__(@JsonIgnore))
	private UUID id;

	private @Alphabetic	String name;
	private boolean isCharacteristic;
	private Map<Locale, String> translations;
}
