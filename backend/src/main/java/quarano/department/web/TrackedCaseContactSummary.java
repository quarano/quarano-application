package quarano.department.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NonNull;
import quarano.department.TrackedCase;
import quarano.tracking.ContactPerson;

@Relation(collectionRelation = "contacts")
public class TrackedCaseContactSummary extends RepresentationModel<TrackedCaseContactSummary> {

	public static final LinkRelation TRACKED_CASE = LinkRelation.of("trackedCase");

	private final @Getter(onMethod = @__(@JsonIgnore)) @NonNull ContactPerson contactPerson;
	private final @Getter @NonNull LocalDate contactDate;

	TrackedCaseContactSummary(ContactPerson contactPerson, LocalDate contactDate,
			Optional<TrackedCase> contactTrackedCase) {

		this.contactPerson = contactPerson;
		this.contactDate = contactDate;

		contactTrackedCase.ifPresent(it -> add(getLinks(it)));
	}

	public static Links getLinks(TrackedCase trackedCase) {

		var controller = on(TrackedCaseController.class);

		var links = Links
				.of(Link.of(fromMethodCall(controller.getCase(trackedCase.getId(), null)).toUriString(), TRACKED_CASE));

		return links;
	}

	public String getContactId() {

		return contactPerson.getId().toString();
	}

	public String getFirstName() {

		return contactPerson.getFirstName();
	}

	public String getLastName() {

		return contactPerson.getLastName();
	}

	public Boolean getIsHealthStaff() {

		return contactPerson.getIsHealthStaff();
	}

	public Boolean getHasPreExistingConditions() {

		return contactPerson.getHasPreExistingConditions();
	}

	public Boolean getIsSenior() {

		return contactPerson.getIsSenior();
	}
}
