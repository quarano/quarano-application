package quarano.department.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.Getter;
import quarano.core.EnumMessageSourceResolvable;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.Status;

import java.util.function.Supplier;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.Link;
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

		var controller = on(TrackedCaseController.class);

		var links = Links.of(Link.of(fromMethodCall(controller.concludeCase(trackedCase.getId(), null)).toUriString(),
				TrackedCaseLinkRelations.CONCLUDE));

		links = links.and(Link.of(fromMethodCall(controller.getContactsOfCase(trackedCase.getId(), null)).toUriString(),
				TrackedCaseLinkRelations.CONTACTS));

		links = links.and(Link.of(fromMethodCall(controller.getDiaryOfCase(trackedCase.getId(), null)).toUriString(),
				TrackedCaseLinkRelations.DIARY));

		Supplier<String> uri = () -> fromMethodCall(
				on(RegistrationController.class).createRegistration(trackedCase.getId(), null)).toUriString();

		if (trackedCase.getStatus().equals(Status.IN_REGISTRATION)) {
			links = links.and(Link.of(uri.get(), TrackedCaseLinkRelations.RENEW));
		}

		// No account yet? Offer creation.
		if (trackedCase.isEligibleForTracking()) {
			links = links.and(Link.of(uri.get(), TrackedCaseLinkRelations.START_TRACKING));
		}

		if (trackedCase.getQuestionnaire() != null) {

			var href = fromMethodCall(controller.getQuestionnaire(trackedCase.getId(), null)).toUriString();

			links = links.and(Link.of(href, TrackedCaseLinkRelations.QUESTIONNAIRE));
		}

		return links;
	}

	public String getStatus() {
		return messages.getMessage(EnumMessageSourceResolvable.of(trackedCase.getStatus()));
	}
}
