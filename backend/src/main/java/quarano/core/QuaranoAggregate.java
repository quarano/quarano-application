package quarano.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.EmbeddedId;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.jddd.core.types.AggregateRoot;
import org.jddd.core.types.Identifier;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

/**
 * @author Oliver Drotbohm
 */
@MappedSuperclass
@EqualsAndHashCode(of = "id", callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public abstract class QuaranoAggregate<T extends QuaranoAggregate<T, ID>, ID extends Identifier>
		extends AbstractAggregateRoot<T>
		implements AggregateRoot<T, ID>, Persistable<ID> {

	protected @Getter(onMethod = @__(@NonNull)) @EmbeddedId ID id;
	private @Getter AuditingMetadata metadata = new AuditingMetadata();
	private @Transient boolean isNew = true;

	public boolean hasId(ID id) {
		return this.id.equals(id);
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
