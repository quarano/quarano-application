package quarano.tracking;

import java.io.Serializable;
import java.util.*;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.jddd.core.types.Identifier;
import org.jddd.event.types.DomainEvent;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Value;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.core.QuaranoAggregate;
import quarano.department.TrackedCase;
import quarano.diary.DiaryEntry;
import quarano.tracking.ContactPerson.ContactPersonIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

/**
 * @author Oliver Drotbohm
 * @author Michael J. Simons
 * @author Felix Schultze
 */
@Entity
@Table(name = "contact_people")
@Data
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ContactPerson extends QuaranoAggregate<ContactPerson, ContactPersonIdentifier> {

	private String firstName, lastName;
	private @Setter(AccessLevel.NONE) EmailAddress emailAddress;

	@AttributeOverride(name = "value", column = @Column(name = "mobilePhoneNumber"))
	private @Setter(AccessLevel.NONE) PhoneNumber mobilePhoneNumber;
	private @Setter(AccessLevel.NONE) PhoneNumber phoneNumber;
	private Address address;

	private @Enumerated(EnumType.STRING) TypeOfContract typeOfContract;
	private String remark;
	private @Setter(AccessLevel.NONE) String identificationHint;

	private @Getter @Setter Boolean isHealthStaff;
	private @Getter @Setter Boolean isSenior;
	private @Getter @Setter Boolean hasPreExistingConditions;
	private @ManyToMany(mappedBy = "contacts") List<DiaryEntry> diaries = new ArrayList<>();

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "tracked_person_id", referencedColumnName="tracked_person_id", updatable = false, insertable = false)
	private TrackedPerson trackedPerson;

	@OneToMany(cascade = CascadeType.ALL, mappedBy="contact")
	private  @Getter @Setter List<Encounter> encounters = new ArrayList<>();

	@Column(nullable = false)
	@AttributeOverride(name = "trackedPersonId", column = @Column(name = "tracked_person_id"))
	private TrackedPersonIdentifier ownerId;

	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "originContacts")
	private @Getter @Setter List<TrackedCase> trackedCases = new ArrayList<>();

	public ContactPerson(String firstName, String lastName, ContactWays contactWays) {

		this.id = ContactPersonIdentifier.of(UUID.randomUUID());
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = contactWays.getEmailAddress();
		this.phoneNumber = contactWays.getPhoneNumber();
		this.mobilePhoneNumber = contactWays.getMobilePhoneNumber();
		this.identificationHint = contactWays.getIdentificationHint();
	}

	public String getFullName() {
		return String.format("%s %s", firstName, lastName);
	}

	public ContactPerson assignOwner(TrackedPerson person) {

		this.ownerId = person.getId();
		this.trackedPerson = person;
		return this;
	}

	public boolean belongsTo(TrackedPerson person) {
		return this.ownerId.equals(person.getId());
	}

	public boolean isVulnerable() {
		return getIsSenior() == Boolean.TRUE || getHasPreExistingConditions() == Boolean.TRUE;
	}

	public ContactPerson contactWays(ContactWays contactWays) {

		this.emailAddress = contactWays.getEmailAddress();
		this.phoneNumber = contactWays.getPhoneNumber();
		this.mobilePhoneNumber = contactWays.getMobilePhoneNumber();
		this.identificationHint = contactWays.getIdentificationHint();

		return this;
	}

	public enum TypeOfContract {

		O("O"), S("S"), P("P"), AE("Ä"), Aer("Aer"), Mat("Mat"), And("And");

		private final @Getter String label;

		TypeOfContract(final String label) {
			this.label = label;
		}
	}

	public enum TypeOfProtection {
		Zero("0"), M1("M1"), M2("M2"), K("K"), H("H"), S("S");

		private final @Getter String label;

		TypeOfProtection(final String label) {
			this.label = label;
		}
	}

	@Value(staticConstructor = "of")
	public static class ContactPersonAdded implements DomainEvent {
		ContactPerson contactPerson;
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
	public static class ContactPersonIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = -8869631517068092437L;

		@Column(name = "contact_person_id")
		final UUID contactPersonId;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return contactPersonId.toString();
		}
	}
}
