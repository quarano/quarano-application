/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano.tracking;

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
import quarano.tracking.ContactPerson.ContactPersonIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.jddd.core.types.Identifier;
import org.jddd.event.types.DomainEvent;

/**
 * @author Oliver Drotbohm
 */
@Entity
@Table(name = "contact_people")
@Data
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ContactPerson extends QuaranoAggregate<ContactPerson, ContactPersonIdentifier> {

	private String firstName, lastName;
	private @Setter(AccessLevel.NONE) EmailAddress emailAddress;

	@AttributeOverride(name = "value", column = @Column(name = "mobilePhoneNumber")) //
	private @Setter(AccessLevel.NONE) PhoneNumber mobilePhoneNumber;
	private @Setter(AccessLevel.NONE) PhoneNumber phoneNumber;
	private Address address;
	@Enumerated(EnumType.STRING)
	private TypeOfContract typeOfContract;
	private String remark;
	private @Setter(AccessLevel.NONE) String identificationHint;

	private @Getter @Setter Boolean isHealthStaff;
	private @Getter @Setter Boolean isSenior;
	private @Getter @Setter Boolean hasPreExistingConditions;

	private @Column(nullable = false) @AttributeOverride(name = "trackedPersonId",
			column = @Column(name = "tracked_person_id")) TrackedPersonIdentifier ownerId;

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

		return this;
	}

	public boolean belongsTo(TrackedPerson person) {
		return this.ownerId.equals(person.getId());
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
