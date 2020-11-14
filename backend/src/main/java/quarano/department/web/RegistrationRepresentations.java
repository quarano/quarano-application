package quarano.department.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import io.jsonwebtoken.lang.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Account;
import quarano.core.CoreProperties;
import quarano.core.EmailSender.TemplatedEmail;
import quarano.core.EmailTemplates;
import quarano.core.EmailTemplates.Keys;
import quarano.core.validation.UserName;
import quarano.core.web.MapperWrapper;
import quarano.department.RegistrationDetails;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.TrackedCaseEmail;
import quarano.department.activation.ActivationCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	private final EmailTemplates templates;
	private final CoreProperties configuration;
	private final MapperWrapper mapper;
	private final @NonNull MessageSourceAccessor messages;

	PendingRegistration toRepresentation(ActivationCode code, TrackedCase trackedCase, Account officeStaff) {

		var email = getEmailTemplateFor(trackedCase, code, officeStaff);
		var result = PendingRegistration.of(trackedCase.getId(), code, email);

		return result.add(TrackedCaseStatusAware.getDefaultLinks(trackedCase));
	}

	RegistrationDetails from(RegistrationDto payload) {
		return mapper.map(payload, RegistrationDetails.class);
	}

	String getEmailTemplateFor(TrackedCase trackedCase, ActivationCode code, Account officeStaff) {

		var key = determineRegistrationMailKey(trackedCase);
		var placeholders = createRegistrationMailPlaceholders(trackedCase, code, officeStaff);

		return templates.expandTemplate(key, placeholders, trackedCase.getTrackedPerson().getLocale());
	}

	/**
	 * @since 1.4
	 */
	TemplatedEmail getTemplatedEmailFor(TrackedCase trackedCase, ActivationCode code, Account officeStaff) {

		var trackedPerson = trackedCase.getTrackedPerson();
		var subject = messages.getMessage("RegistriationMail.subject",
				new Object[] { trackedCase.getDepartment().getName() }, trackedPerson.getLocale());

		var key = determineRegistrationMailKey(trackedCase);
		var placeholders = createRegistrationMailPlaceholders(trackedCase, code, officeStaff);

		return new TrackedCaseEmail(trackedCase, subject, key, placeholders);
	}

	private HashMap<String, Object> createRegistrationMailPlaceholders(TrackedCase trackedCase, ActivationCode code,
			Account officeStaff) {

		var placeholders = new HashMap<String, Object>();

		placeholders.put("lastName", trackedCase.getTrackedPerson().getLastName());
		placeholders.put("departmentName", trackedCase.getDepartment().getName());
		placeholders.put("staffFullName", officeStaff.getFullName());
		placeholders.put("staffLastName", officeStaff.getLastname());
		placeholders.put("staffFirstName", officeStaff.getFirstname());
		placeholders.put("host", configuration.getHost());
		placeholders.put("activationCode", code.getId().toString());

		var quarantine = trackedCase.getQuarantine();

		if (quarantine != null) {
			placeholders.put("quarantineEndDate", quarantine.getTo().format(FORMATTER));
		}

		return placeholders;
	}

	private Keys determineRegistrationMailKey(TrackedCase trackedCase) {
		return trackedCase.isIndexCase() ? Keys.REGISTRATION_INDEX : Keys.REGISTRATION_CONTACT;
	}

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

		/**
		 * @since 1.4
		 */
		public Boolean isMailed() {
			return code.isMailed();
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
							TrackedCaseLinkRelations.RENEW)))
					.andIf(!isMailed(), Link.of(fromMethodCall(controller.sendMail(trackedCaseIdentifier, null)).toUriString(),
							TrackedCaseLinkRelations.SEND_MAIL));
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
