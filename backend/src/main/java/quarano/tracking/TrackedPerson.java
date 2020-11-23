package quarano.tracking;

import static java.util.Comparator.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Value;
import quarano.account.Account;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.core.QuaranoAggregate;
import quarano.tracking.Encounter.EncounterIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import javax.persistence.*;

import org.apache.commons.lang3.LocaleUtils;
import org.jmolecules.ddd.types.Identifier;
import org.jmolecules.event.types.DomainEvent;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author Oliver Drotbohm
 * @author Patrick Otto
 */
@Entity
@Table(name = "tracked_people")
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class TrackedPerson extends QuaranoAggregate<TrackedPerson, TrackedPersonIdentifier>
		implements Comparable<TrackedPerson> {

	private static final Comparator<TrackedPerson> COMPARATOR = Comparator
			.comparing(TrackedPerson::getLastName, nullsLast(String::compareToIgnoreCase))
			.thenComparing(TrackedPerson::getFirstName, nullsLast(String::compareToIgnoreCase));

	private @Getter @Setter String firstName, lastName;
	private @Getter(onMethod = @__(@Nullable)) @Setter(onMethod = @__(@Nullable)) EmailAddress emailAddress;
	private @Getter @Setter PhoneNumber phoneNumber;

	@AttributeOverride(name = "value", column = @Column(name = "mobilePhoneNumber"))
	private @Getter @Setter PhoneNumber mobilePhoneNumber;
	private @Getter @Setter Address address = new Address();
	private @Getter @Setter LocalDate dateOfBirth;

	private @Getter @Nullable Locale locale;

	@OneToOne
	@JoinColumn(name = "account_id")
	private Account account;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "tracked_person_id")
	private List<Encounter> encounters;

	public TrackedPerson(String firstName, String lastName) {
		this(new TrackedPersonIdentifier(UUID.randomUUID()), firstName, lastName, null, null, null);
	}

	TrackedPerson(TrackedPersonIdentifier fixedId, String firstName, String lastName, @Nullable EmailAddress emailAddress,
			@Nullable PhoneNumber phoneNumber, @Nullable LocalDate dateOfBirth) {

		this.id = fixedId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.dateOfBirth = dateOfBirth;
		this.encounters = new ArrayList<>();
	}

	public TrackedPerson(ContactPerson contact) {
		this(new TrackedPersonIdentifier(UUID.randomUUID()), contact.getFirstName(), contact.getLastName(),
				contact.getEmailAddress(), contact.getPhoneNumber(), null);
		this.mobilePhoneNumber = contact.getMobilePhoneNumber();
	}

	public boolean isEligibleForTracking() {
		return emailAddress != null && dateOfBirth != null;
	}

	public boolean hasAccount() {
		return account != null;
	}

	public String getFullName() {
		return String.join(" ", firstName, lastName);
	}

	public Encounters getEncounters() {
		return Encounters.of(encounters);
	}

	public boolean hasBirthdayOf(LocalDate date) {
		return this.dateOfBirth.equals(date);
	}

	public Optional<Account> getAccount() {
		return Optional.ofNullable(account);
	}

	public TrackedPerson markAccountRegistration(Account account) {

		Assert.notNull(account, "Account must not be null!");

		this.account = account;

		return this;
	}

	@Nullable
	public LocalDate getAccountRegistrationDate() {
		return account == null ? null : account.getMetadata().getCreated().toLocalDate();
	}

	public Stream<ContactPerson> getContactPersons() {

		return encounters.stream()
				.map(Encounter::getContact);
	}

	/**
	 * Reports the contact with the given {@link ContactPerson} on the given {@link LocalDate}. If described contact has
	 * already been recorded, nothing will happen. If not, a new {@link Encounter} will be registered. I.e. registering a
	 * contact with the same person on the same day twice will only create a single {@link Encounter}.
	 *
	 * @param person must not be {@literal null}.
	 * @param date must not be {@literal null}.
	 * @return
	 * @see Encounters
	 * @see #getEncounters()
	 */
	public Encounter reportContactWith(ContactPerson person, LocalDate date) {

		Assert.notNull(person, "ContactPerson must not be null!");
		Assert.notNull(date, "Date must not be null!");

		var encounters = getEncounters();

		return encounters.getEncounter(person, date)
				.orElseGet(() -> {

					var encounter = Encounter.with(person, date);

					registerEvent(!encounters.hasBeenInTouchWith(person)
							? EncounterReported.firstEncounter(encounter, id)
							: EncounterReported.subsequentEncounter(encounter, id));

					this.encounters.add(encounter);

					return encounter;
				});
	}

	public TrackedPerson removeEncounter(EncounterIdentifier identifier) {

		encounters.stream()
				.filter(it -> it.hasId(identifier))
				.findFirst()
				.ifPresent(encounters::remove);

		return this;
	}

	public boolean isDetailsCompleted() {

		return StringUtils.hasText(firstName)
				&& StringUtils.hasText(lastName)
				&& emailAddress != null
				&& address.isComplete()
				&& dateOfBirth != null;
	}

	/**
	 * Initializes the {@link TrackedPerson}'s preferred {@link Locale} to the given one unless one is already set.
	 *
	 * @param locale must not be {@literal null}.
	 * @return
	 * @since 1.4
	 */
	TrackedPerson initializeLocale(Locale locale) {

		Assert.notNull(locale, "Locale must not be null!");

		if (this.locale == null) {
			this.locale = locale;
		}

		return this;
	}

	/**
	 * Sets the language of the given {@link Locale} as preferred {@link Locale} for the current {@link TrackedPerson}.
	 * I.e. if {@code Locale#GERMANY} is provided, we store {@code Locale.GERMAN}.
	 *
	 * @param locale can be {@literal null}
	 * @return
	 */
	public TrackedPerson setLocale(@Nullable Locale locale) {

		this.locale = locale == null ? null : LocaleUtils.toLocale(locale.getLanguage());

		return this;
	}

	@Override
	public int compareTo(TrackedPerson o) {
		return COMPARATOR.compare(this, o);
	}

	@Value
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public static class EncounterReported implements DomainEvent {

		Encounter encounter;
		TrackedPersonIdentifier personIdentifier;
		boolean firstEncounterWithTargetPerson;

		public static EncounterReported firstEncounter(Encounter encounter, TrackedPersonIdentifier id) {
			return new EncounterReported(encounter, id, true);
		}

		public static EncounterReported subsequentEncounter(Encounter encounter, TrackedPersonIdentifier id) {
			return new EncounterReported(encounter, id, false);
		}
	}

	@PostLoad
	private void init() {

		if (address == null) {
			this.address = new Address();
		}
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
	public static class TrackedPersonIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = -853047182358126916L;

		private final UUID trackedPersonId;

		@Override
		public String toString() {
			return trackedPersonId.toString();
		}
	}
}
