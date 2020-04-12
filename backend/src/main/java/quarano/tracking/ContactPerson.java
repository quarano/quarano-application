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
import lombok.experimental.Accessors;
import quarano.core.QuaranoAggregate;
import quarano.tracking.ContactPerson.ContactPersonIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

import org.jddd.core.types.Identifier;

/**
 * @author Oliver Drotbohm
 */
@Entity(name = "newContactPerson")
@Data
@Setter(AccessLevel.PACKAGE)
@Accessors(chain = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ContactPerson extends QuaranoAggregate<ContactPerson, ContactPersonIdentifier> {

	private String firstName, lastName;
	private EmailAddress emailAddress;
	private PhoneNumber phoneNumber;
	private Address address;
	private TypeOfContract typeOfContract;
	private @Column(nullable = false) TrackedPersonIdentifier ownerId;

	public ContactPerson(String firstName, String lastName) {

		this.id = ContactPersonIdentifier.of(UUID.randomUUID());
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public boolean hasId(ContactPersonIdentifier id) {
		return this.id.equals(id);
	}

	public ContactPerson assignOwner(TrackedPerson person) {

		this.ownerId = person.getId();

		return this;
	}

	public boolean belongsTo(TrackedPerson person) {
		return this.ownerId.equals(person.getId());
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

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
	public static class ContactPersonIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = -8869631517068092437L;

		final UUID id;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return id.toString();
		}
	}
}
