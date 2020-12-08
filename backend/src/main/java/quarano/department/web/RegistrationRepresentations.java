package quarano.department.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import io.jsonwebtoken.lang.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.core.CoreProperties;
import quarano.core.EmailTemplates;
import quarano.core.validation.UserName;
import quarano.core.web.MapperWrapper;
import quarano.department.RegistrationDetails;
import quarano.department.RegistrationEmailProvider;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.activation.ActivationCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
class RegistrationRepresentations {

	private final @NonNull EmailTemplates templates;
	private final @NonNull CoreProperties configuration;
	private final @NonNull MapperWrapper mapper;
	private final @NonNull MessageSourceAccessor messages;
	private final @NonNull RegistrationEmailProvider emails;

	PendingRegistration toRepresentation(ActivationCode code, TrackedCase trackedCase) {

		var email = emails.getRegistrationEmail(trackedCase, code)
				.getBody(templates, configuration);
		var result = PendingRegistration.of(trackedCase.getId(), code, email);

		return result.add(TrackedCaseStatusAware.getDefaultLinks(trackedCase));
	}

	RegistrationDetails from(RegistrationDto payload) {
		return mapper.map(payload, RegistrationDetails.class);
	}

	@SuppressWarnings("null")
	RepresentationModel<?> toNoRegistration(TrackedCase trackedCase) {

		var controller = on(RegistrationController.class);

		return trackedCase.isEligibleForTracking()
				? new RepresentationModel<>(
						Link.of(fromMethodCall(controller.createRegistration(trackedCase.getId(), null)).toUriString(),
								TrackedCaseLinkRelations.START_TRACKING))
				: RepresentationModel.of(null);
	}

	@RequiredArgsConstructor(staticName = "of")
	static class PendingRegistration extends RepresentationModel<PendingRegistration> {

		private final TrackedCaseIdentifier trackedCaseIdentifier;
		private final ActivationCode code;
		private final String email;

		public String getActivationCode() {
			return code.getId().toString();
		}

		public LocalDateTime getExpirationDate() {
			return code.getExpirationTime();
		}

		public String getEmail() {
			return email;
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.hateoas.RepresentationModel#getLinks()
		 */
		@Override
		@SuppressWarnings("null")
		public Links getLinks() {

			var controller = on(RegistrationController.class);

			return super.getLinks().and(Links.of(
					Link.of(fromMethodCall(controller.getRegistrationDetails(trackedCaseIdentifier, null)).toUriString(),
							IanaLinkRelations.SELF),
					Link.of(fromMethodCall(controller.createRegistration(trackedCaseIdentifier, null)).toUriString(),
							TrackedCaseLinkRelations.RENEW)));
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class RegistrationDto {

		private @UserName @NotBlank String username;
		private @NotBlank String password, passwordConfirm; // password rules are tested in entity
		private @NotNull @Past LocalDate dateOfBirth;
		private @NotNull UUID clientCode;
		private UUID clientId;
		private String departmentId;

		RegistrationDto validate(Errors errors) {

			if (!Objects.nullSafeEquals(password, passwordConfirm)) {
				errors.rejectValue("passwordConfirm", "Password.nonMatching");
			}

			return this;
		}
	}
}
