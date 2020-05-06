/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano.department.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.RequiredArgsConstructor;
import quarano.core.CoreProperties;
import quarano.core.EmailTemplates;
import quarano.core.EmailTemplates.Keys;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.activation.ActivationCode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
class RegistrationRepresentations {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	private final EmailTemplates templates;
	private final CoreProperties configuration;

	PendingRegistration toRepresentation(ActivationCode code, TrackedCase trackedCase) {

		var email = getEmailTemplateFor(trackedCase, code);
		var result = PendingRegistration.of(trackedCase.getId(), code, email);

		return result.add(TrackedCaseStatusAware.getDefaultLinks(trackedCase));
	}

	private String getEmailTemplateFor(TrackedCase trackedCase, ActivationCode code) {

		var placeholders = new HashMap<String, Object>();

		placeholders.put("lastName", trackedCase.getTrackedPerson().getLastName());
		placeholders.put("host", configuration.getHost());
		placeholders.put("activationCode", code.getId().toString());

		if (trackedCase.isIndexCase()) {
			placeholders.put("quarantineEndDate", trackedCase.getQuarantine().getTo().format(FORMATTER));
		}

		var key = trackedCase.isIndexCase() ? Keys.REGISTRATION_INDEX : Keys.REGISTRATION_CONTACT;

		return templates.getTemplate(key, placeholders);
	}

	RepresentationModel<?> toNoRegistration(TrackedCase trackedCase) {

		var controller = on(RegistrationController.class);

		return trackedCase.isEligibleForTracking() //
				? new RepresentationModel<>(
						Link.of(fromMethodCall(controller.createRegistration(trackedCase.getId(), null)).toUriString(),
								TrackedCaseLinkRelations.START_TRACKING)) //
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

			return super.getLinks().and(Links.of( //
					Link.of(fromMethodCall(controller.getRegistrationDetails(trackedCaseIdentifier, null)).toUriString(),
							IanaLinkRelations.SELF),
					Link.of(fromMethodCall(controller.createRegistration(trackedCaseIdentifier, null)).toUriString(),
							TrackedCaseLinkRelations.RENEW)));
		}
	}
}
