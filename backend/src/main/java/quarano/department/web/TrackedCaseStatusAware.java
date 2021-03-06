package quarano.department.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;
import static quarano.department.web.TrackedCaseLinkRelations.*;

import lombok.Getter;
import quarano.core.EnumMessageSourceResolvable;
import quarano.department.TrackedCase;

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
		var registrationController = on(RegistrationController.class);

		return Links.of(MvcLink.of(controller.concludeCase(caseId, null), CONCLUDE))
				.and(MvcLink.of(controller.getDiaryOfCase(caseId, null), DIARY))
				.and(MvcLink.of(controller.getCase(caseId, null), IanaLinkRelations.SELF))
				.andIf(trackedCase.isIndexCase(), MvcLink.of(controller.getContactsOfCase(caseId, null), CONTACTS))
				.andIf((trackedCase.getTrackedPerson().getAccount().isEmpty() && !trackedCase.getStatus().equals(TrackedCase.Status.OPEN)), () -> MvcLink.of(registrationController.createRegistration(caseId, null), RENEW))
				.andIf(trackedCase.getTrackedPerson().getAccount().isPresent(), MvcLink.of(registrationController.removeAccountOfTrackedPerson(caseId, null), ACCOUNT))
				.andIf(trackedCase.isEligibleForTracking(), () -> MvcLink.of(registrationController.createRegistration(caseId, null), START_TRACKING))
				.andIf(trackedCase.hasQuestionnaire(),
						() -> MvcLink.of(controller.getQuestionnaire(caseId, null), QUESTIONNAIRE))
				.and(trackedCase.getOriginCases().stream()
						.map(it -> MvcLink.of(controller.getCase(it.getId(), it.getDepartment()), ORIGIN_CASES)));
	}

	public String getStatus() {
		return messages.getMessage(EnumMessageSourceResolvable.of(trackedCase.getStatus()));
	}
}
