package quarano.reference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.domain.Persistable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import quarano.diary.DiaryEntry;

@Entity
@Table(name = "symptoms")
@AllArgsConstructor
@Getter
@Setter(value = AccessLevel.PACKAGE)
public class Symptom implements Persistable<UUID> {

	@Id @Column(name = "symptom_id")
	private UUID id;
	private @Transient boolean isNew = true;
	private String name;
	private boolean isCharacteristic;
	private @ManyToMany(mappedBy = "symptoms") List<DiaryEntry> diaries = new ArrayList<>();

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
