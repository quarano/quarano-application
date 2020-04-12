package quarano.reference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Getter
@Setter(value = AccessLevel.PACKAGE)
public class NewSymptom {

	@Id //
	private UUID id;
	private String name;
	private boolean isCharacteristic;
}
