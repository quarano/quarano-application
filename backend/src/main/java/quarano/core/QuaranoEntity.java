package quarano.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.jddd.core.types.Entity;
import org.jddd.core.types.Identifier;
import org.springframework.data.domain.Persistable;
import org.springframework.lang.NonNull;

/**
 * @author Oliver Drotbohm
 */
@MappedSuperclass
@EqualsAndHashCode(of = "id")
public abstract class QuaranoEntity<T extends QuaranoAggregate<T, ?>, ID extends Identifier>
		implements Entity<T, ID>, Persistable<ID> {

	protected @Getter(onMethod = @__(@NonNull)) @Id @GeneratedValue ID id;
	private @Transient boolean isNew = true;

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
