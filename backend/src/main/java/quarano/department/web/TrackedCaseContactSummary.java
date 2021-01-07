package quarano.department.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.Getter;
import lombok.NonNull;
import quarano.core.EnumMessageSourceResolvable;
import quarano.department.CaseType;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.tracking.ContactPerson;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Relation(collectionRelation = "contacts")
public class TrackedCaseContactSummary extends RepresentationModel<TrackedCaseContactSummary> {

	public static final LinkRelation TRACKED_CASE = LinkRelation.of("trackedCase");
	private final MessageSourceAccessor messages;

	private final @Getter(onMethod = @__(@JsonIgnore)) @NonNull ContactPerson contactPerson;
	private final @Getter(onMethod = @__(@JsonIgnore)) Optional<TrackedCase> contactTrackedCase;
	private final @Getter @NonNull List<LocalDate> contactDates;

	public TrackedCaseContactSummary(ContactPerson contactPerson, List<LocalDate> contactDates,
			Optional<TrackedCase> contactTrackedCase, MessageSourceAccessor messages) {

		this.contactTrackedCase = contactTrackedCase;
		this.messages = messages;
		this.contactPerson = contactPerson;
		this.contactDates = contactDates;
		this.contactDates.sort(Comparator.reverseOrder());

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

	public String getRemark() {
		return contactPerson.getRemark();
	}

	public String getIdentificationHint() {
		return contactPerson.getIdentificationHint();
	}

	public String getCaseId() {
		return contactTrackedCase.map(TrackedCase::getId)
				.map(TrackedCaseIdentifier::toString)
				.orElse(null);
	}

	public String getCaseType() {

		return contactTrackedCase.map(TrackedCase::getType)
				.map(CaseType::getPrimaryCaseType)
				.map(CaseType::name)
				.map(it -> it.toLowerCase(Locale.US))
				.orElse(null);
	}

	public String getCaseTypeLabel() {

		return toResolvedEnum(contactTrackedCase.map(TrackedCase::getType)
				.map(CaseType::getPrimaryCaseType));
	}

	public String getCaseStatusLabel() {
		return toResolvedEnum(contactTrackedCase.map(TrackedCase::getStatus));
	}

	private String toResolvedEnum(Optional<Enum<?>> source) {
		return toResolvedEnum(source, "");
	}

	private String toResolvedEnum(Optional<Enum<?>> source, String defaultValue) {

		return source.map(EnumMessageSourceResolvable::of)
				.map(messages::getMessage)
				.orElse(defaultValue);
	}
}
