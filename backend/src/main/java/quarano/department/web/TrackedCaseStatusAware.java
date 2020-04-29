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

import lombok.Getter;
import quarano.core.EnumMessageSourceResolvable;
import quarano.department.CaseStatus;
import quarano.department.TrackedCase;

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

		return Links.of(Link.of(fromMethodCall(controller.concludeCase(trackedCase.getId(), null)).toUriString(),
				TrackedCaseLinkRelations.CONCLUDE));
	}

	public String getStatus() {
		return messages.getMessage(EnumMessageSourceResolvable.of(resolveStatus()));
	}

	private CaseStatus resolveStatus() {

		if (trackedCase.isConcluded()) {
			return CaseStatus.STOPPED;
		}

		if (trackedCase.getEnrollment().isComplete()) {
			return CaseStatus.TRACKING_ACTIVE;
		}

		if (hasLink(TrackedCaseLinkRelations.RENEW)) {
			return CaseStatus.IN_REGISTRATION;
		}

		if (trackedCase.getTrackedPerson().hasAccount()) {
			return CaseStatus.REGISTRATION_COMPLETED;
		}

		return CaseStatus.OPENED;
	}
}
