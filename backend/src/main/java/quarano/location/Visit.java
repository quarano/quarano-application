package quarano.location;

import lombok.*;
import org.jddd.core.types.Identifier;
import quarano.core.QuaranoAggregate;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "location_visits")
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Visit extends QuaranoAggregate<Visit, Visit.VisitIdentifier> {

    private @Getter TrackedPersonIdentifier personIdentifier;
    private @Getter Location.LocationIdentifier locationIdentifier;


    @Embeddable
    @EqualsAndHashCode
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    public static class VisitIdentifier implements Identifier, Serializable {

        private static final long serialVersionUID = 7871473225101042167L;

        final UUID visitId;
    }
}
