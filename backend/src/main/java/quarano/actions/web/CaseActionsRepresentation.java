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

		return items.hasUnresolvedItemsForManuallyReslovation() //
				? result.add(Link.of(uriString, RESOLVE_REL)) //
				: result;
	}

	public TrackedCaseIdentifier getCaseId() {
		return trackedCase.getId();
	}

	public List<Map<?, ?>> getComments() {

		return trackedCase.getComments().stream() //
				.sorted(Comment.BY_DATE_DESCENDING) //
				.map(it -> Map.of("date", it.getDate(), "comment", it.getText())) //
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
