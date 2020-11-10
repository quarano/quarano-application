package quarano.tracking.web;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.core.EmailAddress;
import quarano.core.web.MapperWrapper;
import quarano.tracking.Address.HouseNumber;
import quarano.tracking.ContactPerson;
import quarano.tracking.ContactPersonRepository;

import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
class ContactPersonMappingIntegrationTests {

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
		assertThat(result.getStreet()).isEqualTo(person.getAddress().getStreet());
	}

	@Test
	void bindsToNewContact() {

		var dto = new ContactPersonDto();
		dto.setFirstName("Michael");
		dto.setLastName("Mustermann");
		dto.setStreet("Street");
		dto.setHouseNumber("42");
		dto.setEmail("michael@mustermann.de");

		var person = mapper.map(dto, ContactPerson.class);

		assertThat(person.getId()).isNotNull();
	}
}
