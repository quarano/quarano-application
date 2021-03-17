package quarano.tracking;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import quarano.core.Address;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.core.QuaranoAggregate;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jmolecules.ddd.types.Identifier;

/**
 * @author David Bauknecht
 */
@Entity
@Table(name = "locations")
@Data
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Location extends QuaranoAggregate<Location, Location.LocationIdentifier> {

    private @Setter(AccessLevel.NONE) String name;
    private @Setter(AccessLevel.NONE) String contactPersonName;
    private @Setter(AccessLevel.NONE) EmailAddress contactPersonEmail;
    private @Setter(AccessLevel.NONE) PhoneNumber contactPersonPhone;
    private Address address;
    private String comment;

    @Column(nullable = false)
    @AttributeOverride(name = "trackedPersonId", column = @Column(name = "tracked_person_id"))
    private TrackedPerson.TrackedPersonIdentifier ownerId;

    public Location(String name, String contactPersonName, EmailAddress emailAddress, PhoneNumber phoneNumber, String comment) {

        this.id = Location.LocationIdentifier.of(UUID.randomUUID());
        this.name = name;
        this.contactPersonName = contactPersonName;
        this.contactPersonEmail = emailAddress;
        this.contactPersonPhone = phoneNumber;
        this.comment = comment;
    }

    public Location assignOwner(TrackedPerson person) {

        this.ownerId = person.getId();

        return this;
    }

    public boolean belongsTo(TrackedPerson person) {
        return this.ownerId.equals(person.getId());
    }

    @Embeddable
    @EqualsAndHashCode
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
    public static class LocationIdentifier implements Identifier, Serializable {

        private static final long serialVersionUID = -8869631517067092437L;


        final UUID locationId;

        /*
         * (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return locationId.toString();
        }
    }
}
