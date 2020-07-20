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
package quarano.actions.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;
import static quarano.department.web.TrackedCaseLinkRelations.*;

import quarano.actions.ActionItems;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.web.TrackedCaseController;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.server.mvc.MvcLink;

/**
 * @author Oliver Drotbohm
 */
public class AnomaliesLinkRelations {

	public static final LinkRelation RESOLVE = LinkRelation.of("resolve");
	public static final LinkRelation ANOMALIES = LinkRelation.of("anomalies");

	@SuppressWarnings("null")
	Links getLinksFor(TrackedCaseIdentifier id, ActionItems items) {

		var controller = on(AnomaliesController.class);
		var trackedCaseController = on(TrackedCaseController.class);

		return Links.NONE
				.and(MvcLink.of(controller.getAnomalies(id, null), IanaLinkRelations.SELF))
				.and(MvcLink.of(trackedCaseController.getCase(id, null), CASE))
				.andIf(items.hasUnresolvedItemsForManualResolution(),
						() -> MvcLink.of(controller.resolveActions(id, null, null, null), RESOLVE));
	}
}
