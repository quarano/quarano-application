package quarano.tracking;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.department.TrackingEventListener;
import quarano.tracking.ContactPerson.ContactPersonAdded;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.moduliths.test.PublishedEvents;
import org.moduliths.test.PublishedEventsExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * 
 * @author Patrick Otto
 *
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
@ExtendWith(PublishedEventsExtension.class)
public class ContactPersonIntegrationTests {

	private final TrackedPersonRepository people;
	private final ContactPersonRepository contacts;
	
	@MockBean TrackingEventListener listener;
	
	@Test
	void testAddingAContactThrowsEvent(PublishedEvents events) {
		
		var person = people.save(new TrackedPerson("Michael", "Mustermann"));
		var contact  =  new ContactPerson("myFirstname", "myLastname", ContactWays.ofMobilePhoneNumber("06211121212"));
		contact.assignOwner(person);
		contacts.save(contact);
		 
		var expected = events.ofType(ContactPersonAdded.class) //
				.matching(it -> it.getContactPerson().getOwnerId().equals(person.getId()));

		assertThat(expected).hasSize(1);
	}
	
}
