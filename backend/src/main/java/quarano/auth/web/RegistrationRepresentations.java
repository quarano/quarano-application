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
package quarano.auth.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.RequiredArgsConstructor;
import quarano.auth.ActivationCode;
import quarano.core.EmailTemplates;
import quarano.core.EmailTemplates.Keys;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.web.TrackedCaseLinkRelations;
import quarano.department.web.TrackedCaseStatusAware;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Map;

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

	private final EmailTemplates templates;

	PendingRegistration toRepresentation(ActivationCode code, TrackedCase trackedCase) {

		var formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

		var placeholders = Map.of("lastName", trackedCase.getTrackedPerson().getLastName(), //
				"quarantineEndDate", trackedCase.getQuarantine().getTo().format(formatter), //
				"activationCode", code.getId().toString());

		var email = templates.getTemplate(Keys.REGISTRATION, placeholders);
		var result = PendingRegistration.of(trackedCase.getId(), code, email);

		return result.add(TrackedCaseStatusAware.getDefaultLinks(trackedCase));
	}

	RepresentationModel<?> toNoRegistration(TrackedCase trackedCase) {

		var controller = on(RegistrationController.class);

		return new RepresentationModel<>(
				Link.of(fromMethodCall(controller.createRegistration(trackedCase.getId(), null)).toUriString(),
						TrackedCaseLinkRelations.START_TRACKING));
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
