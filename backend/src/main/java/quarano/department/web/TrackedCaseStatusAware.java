package quarano.department.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.Getter;
import quarano.core.EnumMessageSourceResolvable;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.Status;

import java.util.function.Supplier;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Oliver Drotbohm
 */
public class TrackedCaseStatusAware<T extends RepresentationModel<T>> extends RepresentationModel<T> {

	private final @Getter(onMethod = @__(@JsonIgnore)) TrackedCase trackedCase;
	private final MessageSourceAccessor messages;

	protected TrackedCaseStatusAware(TrackedCase trackedCase, MessageSourceAccessor messages) {

		this.trackedCase = trackedCase;
		this.messages = messages;

		add(getDefaultLinks(trackedCase));
	}

	@SuppressWarnings("null")
	public static Links getDefaultLinks(TrackedCase trackedCase) {

		var caseId = trackedCase.getId();
		var controller = on(TrackedCaseController.class);

		var links = Links.of(toLink(controller.concludeCase(caseId, null), TrackedCaseLinkRelations.CONCLUDE))
				.and(toLink(controller.getContactsOfCase(caseId, null), TrackedCaseLinkRelations.CONTACTS))
				.and(toLink(controller.getDiaryOfCase(caseId, null), TrackedCaseLinkRelations.DIARY))
				.and(toLink(controller.getCase(caseId, null), IanaLinkRelations.SELF));

		Supplier<Object> uri = () -> on(RegistrationController.class).createRegistration(caseId, null);

		if (trackedCase.getStatus().equals(Status.IN_REGISTRATION)) {
			links = links.and(toLink(uri, TrackedCaseLinkRelations.RENEW));
		}

		// No account yet? Offer creation.
		if (trackedCase.isEligibleForTracking()) {
			links = links.and(toLink(uri, TrackedCaseLinkRelations.START_TRACKING));
		}

		if (trackedCase.getQuestionnaire() != null) {
			links = links.and(toLink(controller.getQuestionnaire(caseId, null), TrackedCaseLinkRelations.QUESTIONNAIRE));
		}

		return links;
	}

	public String getStatus() {
		return messages.getMessage(EnumMessageSourceResolvable.of(trackedCase.getStatus()));
	}

	private static Link toLink(Object invocation, LinkRelation relation) {
		return toLink(() -> invocation, relation);
	}

	private static Link toLink(Supplier<Object> invocation, LinkRelation relation) {
		return Link.of(fromMethodCall(invocation.get()).toUriString(), relation);
	}
}
