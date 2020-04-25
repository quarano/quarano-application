package quarano.department;

import lombok.extern.slf4j.Slf4j;
import quarano.tracking.ContactPerson.ContactPersonAdded;

import org.springframework.context.event.EventListener;

@Slf4j
public class ContactPersonCreationEventListener {
	
	@EventListener
	void on(ContactPersonAdded event) {
		log.debug("Listened to  contact person creation event: " + event.getContactPerson());
	}
}
