package quarano.sormas_integration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sync_times")
@EqualsAndHashCode(of = "dataType")
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
public class SyncTimes {

	@Id
	// private int id;
	@Enumerated(EnumType.STRING)
	private DataTypes dataType;
	private long lastSync;

	// static SyncTimes of(DataTypes dataType, int lastSync) {
	// return new SyncTimes(dataType.ordinal(), dataType, lastSync);
	// }

	enum DataTypes {
		Persons, Cases, Contacts, Tasks
	}
}
