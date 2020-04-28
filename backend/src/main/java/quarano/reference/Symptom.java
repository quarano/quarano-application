package quarano.reference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.springframework.data.domain.Persistable;

@Entity
@AllArgsConstructor
@Getter
@Setter(value = AccessLevel.PACKAGE)
public class Symptom implements Persistable<UUID> {

	@Id //
	private UUID id;
	private @Transient boolean isNew = true;
	private String name;
	private boolean isCharacteristic;

	Symptom() {
		this.id = UUID.randomUUID();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Persistable#isNew()
	 */
	@Override
	public boolean isNew() {
		return isNew;
	}

	@PrePersist
	@PostLoad
	void markNotNew() {
		this.isNew = false;
	}
}
