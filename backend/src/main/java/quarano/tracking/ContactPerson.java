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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.With;
import quarano.core.QuaranoEntity;
import quarano.tracking.ContactPerson.ContactPersonIdentifier;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

import org.jddd.core.types.Identifier;

/**
 * @author Oliver Drotbohm
 */
@Entity(name = "newContactPerson")
@Getter
@With
@RequiredArgsConstructor(staticName = "named")
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ContactPerson extends QuaranoEntity<TrackedPerson, ContactPersonIdentifier> {

	private final @Getter String firstname, lastname;
	private EmailAddress emailAddress;
	private PhoneNumber phoneNumber;
	private Address address;

	@Embeddable
	@EqualsAndHashCode
	public static class ContactPersonIdentifier implements Identifier, Serializable {
		private static final long serialVersionUID = -8869631517068092437L;
		UUID id;
	}
}
