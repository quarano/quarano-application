package quarano.reference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Getter
@Setter(value = AccessLevel.PACKAGE)
public class NewSymptom {

	@Id @GeneratedValue //
	private Long id;
	private String name;
	private boolean isCharacteristic;
}
