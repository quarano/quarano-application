package quarano.tracking.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import quarano.tracking.Encounter;
import quarano.tracking.TrackedPerson;

import java.time.LocalDate;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Oliver Drotbohm
 */
@Relation(collectionRelation = "encounters", itemRelation = "encounter")
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class EncounterDto extends RepresentationModel<EncounterDto> {

	private static final LinkRelation CONTACT_REL = LinkRelation.of("contact");

	private @Getter(onMethod = @__(@JsonIgnore)) Encounter encounter;
	private @Getter(onMethod = @__(@JsonIgnore)) TrackedPerson person;

	public LocalDate getDate() {
		return encounter.getDate();
	}

	public String getFirstName() {
		return encounter.getContact().getFirstName();
	}

	public String getLastName() {
		return encounter.getContact().getLastName();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.hateoas.RepresentationModel#getLinks()
	 */
	@Override
	@SuppressWarnings("null")
	public Links getLinks() {

		Links links = super.getLinks();

		var contactId = encounter.getContact().getId();
		var contactHandlerMethod = on(ContactPersonController.class).getContact(null, contactId);
		var encounterUri = on(TrackingController.class).getEncounter(encounter.getId(), person);

		return links.and(Link.of(fromMethodCall(encounterUri).toUriString()).withSelfRel())
				.and(Link.of(fromMethodCall(contactHandlerMethod).toUriString(), CONTACT_REL));
	}
}
