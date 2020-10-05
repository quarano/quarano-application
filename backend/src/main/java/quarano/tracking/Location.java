package quarano.tracking;

import lombok.*;
import org.jddd.core.types.Identifier;
import quarano.core.QuaranoAggregate;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * A generic geo location ("a place on earth")
 */
@Entity
@Table(name = "locations")
@Data
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Location extends QuaranoAggregate<Location, Location.LocationId> {

	private Address address;

	// TODO CORE-446 in the future this should be an exact geolocation; currently unused
	private BigDecimal latitude = BigDecimal.ZERO;
	private BigDecimal longitude = BigDecimal.ZERO;

	public Location(Address address) {
			super();
			this.id = LocationId.of(UUID.randomUUID());
			this.address = address;
	}

	@Embeddable
	@Value
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static final class LocationId implements Identifier, Serializable {
		private static final long serialVersionUID = 3501699696433586411L;
		private final UUID locationId;

		@Override
		public String toString() {
			return locationId.toString();
		}
	}
}
