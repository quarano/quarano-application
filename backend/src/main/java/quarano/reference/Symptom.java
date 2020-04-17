package quarano.reference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@Getter
@Setter(value = AccessLevel.PACKAGE)
public class Symptom {

	@Id //
	private UUID id;
	private String name;
	private boolean isCharacteristic;

	Symptom() {
		this.id = UUID.randomUUID();
	}
}
