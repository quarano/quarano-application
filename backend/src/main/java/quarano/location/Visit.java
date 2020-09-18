package quarano.location;

import lombok.*;
import org.jddd.core.types.Identifier;
import quarano.core.QuaranoAggregate;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "location_visits")
@Getter
@Setter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Visit extends QuaranoAggregate<Visit, Visit.VisitIdentifier> {

    private TrackedPersonIdentifier personIdentifier;
    private Location.LocationIdentifier locationIdentifier;
    private final LocalDate from;
    private final LocalDate to;

    @Embeddable
    @EqualsAndHashCode
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    public static class VisitIdentifier implements Identifier, Serializable {

        private static final long serialVersionUID = 3440250560396312429L;

        final UUID visitId;
    }
}
