package quarano.reference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.persistence.*;

import org.springframework.data.domain.Persistable;

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

	@Column(name = "translations", columnDefinition = "clob")
	@Convert(converter = TranslationConverter.class)
	private Map<Locale, String> translations;

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

	public Symptom translate(Locale lang) {

		if(translations != null) {

			var translation = translations.get(lang);
			if(translation != null) {
				this.name = translation;
			}
		}
		return this;
	}
}
