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
package quarano.tracking.web;

import static org.assertj.core.api.Assertions.*;

import de.wevsvirushackathon.coronareport.CoronareportBackendApplication;
import lombok.RequiredArgsConstructor;
import quarano.Quarano;
import quarano.core.web.MapperWrapper;
import quarano.tracking.Address.HouseNumber;
import quarano.tracking.ContactPersonRepository;
import quarano.tracking.EmailAddress;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

/**
 * @author Oliver Drotbohm
 */
@ActiveProfiles("integrationtest")
@SpringBootTest(classes = { Quarano.class, CoronareportBackendApplication.class })
@TestInstance(Lifecycle.PER_CLASS)
@TestConstructor(autowireMode = AutowireMode.ALL)
@RequiredArgsConstructor
public class ContactPersonMappingIntegrationTests {

	private final ContactPersonRepository contacts;
	private final MapperWrapper mapper;

	@Test
	void bindsDtoToExistingDomainObject() {

		var reference = contacts.findAll().iterator().next();

		var dto = new ContactPersonDto();
		dto.setFirstName("Michael");
		dto.setLastName("Mustermann");
		dto.setStreet("Street");
		dto.setHouseNumber("42");
		dto.setEmail("michael@mustermann.de");

		var person = mapper.map(dto, reference);

		assertThat(person.getFirstName()).isEqualTo(dto.getFirstName());
		assertThat(person.getLastName()).isEqualTo(dto.getLastName());
		assertThat(person.getAddress().getStreet()).isEqualTo(dto.getStreet());
		assertThat(person.getAddress().getHouseNumber()).isEqualTo(HouseNumber.of(dto.getHouseNumber()));
		assertThat(person.getEmailAddress()).isEqualTo(EmailAddress.of(dto.getEmail()));

		var result = mapper.map(person, ContactPersonDto.class);

		assertThat(result.getId()).isEqualTo(reference.getId());
		assertThat(result.getFirstName()).isEqualTo(person.getFirstName());
		assertThat(result.getLastName()).isEqualTo(person.getLastName());
	}
}
