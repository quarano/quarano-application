package quarano.sormas_integration;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "changes_for_sync")
@EqualsAndHashCode(of = "id")
@ToString
@NoArgsConstructor
@Getter
public class ChangesForSync {

	@Id
	private UUID id;
	private LocalDateTime created;
	private String sourceTable;
	private UUID refId;
}
