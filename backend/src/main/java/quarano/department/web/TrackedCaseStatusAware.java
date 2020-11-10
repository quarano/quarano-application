package quarano.department.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;
import static quarano.department.web.TrackedCaseLinkRelations.*;

import lombok.Getter;
import quarano.core.EnumMessageSourceResolvable;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.Status;

import java.util.function.Supplier;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.MvcLink;

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

		Supplier<Object> uri = () -> on(RegistrationController.class).createRegistration(caseId, null);

		return Links.of(MvcLink.of(controller.concludeCase(caseId, null), CONCLUDE))
				.and(MvcLink.of(controller.getContactsOfCase(caseId, null), CONTACTS))
				.and(MvcLink.of(controller.getDiaryOfCase(caseId, null), DIARY))
				.and(MvcLink.of(controller.getCase(caseId, null), IanaLinkRelations.SELF))
				.andIf(trackedCase.getStatus().equals(Status.IN_REGISTRATION), () -> MvcLink.of(uri, RENEW))
				.andIf(trackedCase.isEligibleForTracking(), () -> MvcLink.of(uri, START_TRACKING))
				.andIf(trackedCase.hasQuestionnaire(),
						() -> MvcLink.of(controller.getQuestionnaire(caseId, null), QUESTIONNAIRE))
				.and(trackedCase.getOriginCases().stream()
						.map(it -> MvcLink.of(controller.getCase(it.getId(), it.getDepartment()), ORIGIN_CASES)));
	}

	public String getStatus() {
		return messages.getMessage(EnumMessageSourceResolvable.of(trackedCase.getStatus()));
	}

	public String getNewContactCaseMailStatus() {
		return messages.getMessage(EnumMessageSourceResolvable.of(trackedCase.getNewContactCaseMailStatus()));
	}
}
