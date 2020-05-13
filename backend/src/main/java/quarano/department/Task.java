package quarano.department;

import lombok.EqualsAndHashCode;
import quarano.core.QuaranoAggregate;
import quarano.department.Task.TaskIdentifier;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;

import org.jddd.core.types.Identifier;

/**
 * @author Oliver Drotbohm
 */
public class Task extends QuaranoAggregate<Task, TaskIdentifier> {

	@Embeddable
	@EqualsAndHashCode
	static class TaskIdentifier implements Identifier, Serializable {
		private static final long serialVersionUID = 1253942895031412367L;
		UUID id = UUID.randomUUID();
	}
}
