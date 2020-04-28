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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import quarano.actions.ActionItem;
import quarano.actions.ActionItems;
import quarano.department.Comment;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.TrackedCaseIdentifier;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.util.Streamable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.RepresentationModel;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CaseActionsRepresentation extends RepresentationModel<CaseActionsRepresentation> {

	private static final LinkRelation RESOLVE_REL = LinkRelation.of("resolve");

	private final TrackedCase trackedCase;
	private final ActionItems items;
	private final MessageSourceAccessor messages;

	public static CaseActionsRepresentation of(TrackedCase trackedCase, ActionItems items,
			MessageSourceAccessor messages) {

		CaseActionsRepresentation result = new CaseActionsRepresentation(trackedCase, items, messages);

		@SuppressWarnings("null")
		var uriString = fromMethodCall(on(ActionItemController.class) //
				.resolveActions(trackedCase.getId(), null, null, null)).toUriString();

		return items.hasUnresolvedItems() //
				? result.add(Link.of(uriString, RESOLVE_REL)) //
				: result;
	}

	public TrackedCaseIdentifier getCaseId() {
		return trackedCase.getId();
	}

	public List<Map<?, ?>> getComments() {

		return trackedCase.getComments().stream() //
				.sorted(Comment.BY_DATE_DESCENDING) //
				.map(it -> Map.of("date", it.getDate(), //
						"comment", it.getComment())) //
				.collect(Collectors.toList());
	}

	public Map<String, Object> getAnomalies() {

		return Map.of("health", toDailyItems(items.getHealthItems(), false), //
				"process", toDailyItems(items.getProcessItems(), false), //
				"resolved", toDailyItems(items.getResolvedItems(), true));
	}

	private List<DailyItems> toDailyItems(Streamable<ActionItem> items, boolean done) {

		return items.stream() //
				.collect(Collectors.groupingBy(it -> it.getMetadata().getLastModified().toLocalDate())) //
				.entrySet() //
				.stream() //
				.map(it -> new DailyItems(it.getKey(), it.getValue(), messages)) //
				.sorted(Comparator.comparing(DailyItems::getDate)) //
				.collect(Collectors.toUnmodifiableList());
	}

	@Getter
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	private static class DailyItems extends RepresentationModel<DailyItems> {

		private final LocalDate date;
		private final List<ActionItemDto> items;

		public DailyItems(LocalDate date, List<ActionItem> items, MessageSourceAccessor messages) {

			this.date = date;
			this.items = items.stream() //
					.map(it -> ActionItemDto.of(it, messages)) //
					.sorted(Comparator.comparing(ActionItemDto::getDate)) //
					.collect(Collectors.toUnmodifiableList());
		}
	}
}
