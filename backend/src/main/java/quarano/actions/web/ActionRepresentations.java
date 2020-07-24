package quarano.actions.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;
import static quarano.department.web.TrackedCaseLinkRelations.*;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.actions.ActionItem;
import quarano.actions.ActionItem.ItemType;
import quarano.actions.ActionItems;
import quarano.actions.Description;
import quarano.actions.DescriptionCode;
import quarano.actions.DiaryEntryActionItem;
import quarano.department.Comment;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.web.ExternalTrackedCaseRepresentations;
import quarano.department.web.TrackedCaseController;
import quarano.department.web.TrackedCaseSummary;
import quarano.diary.DiaryEntry;
import quarano.diary.web.DiaryController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.util.Streamable;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.MvcLink;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
class ActionRepresentations {

	private final MessageSourceAccessor messages;
	private final @NonNull ExternalTrackedCaseRepresentations trackedCaseRepresentations;

	public CaseActionsRepresentation toRepresentation(TrackedCase trackedCase, ActionItems items) {
		return CaseActionsRepresentation.of(trackedCase, items, messages);
	}

	public CaseActionSummary toSummary(TrackedCase trackedCase, ActionItems items) {

		var summary = trackedCaseRepresentations.toSummary(trackedCase);

		return new CaseActionSummary(trackedCase, items, summary);
	}

	@RequiredArgsConstructor(staticName = "of")
	static class ActionItemDto {

		private final ActionItem item;
		private final MessageSourceAccessor messages;

		public LocalDateTime getDate() {
			return item.getMetadata().getLastModified();
		}

		public ItemType getType() {
			return item.getType();
		}

		public float getWeight() {
			return item.getWeight();
		}

		public String getDescription() {
			return messages.getMessage(item.getDescription());
		}

		@SuppressWarnings("null")
		@JsonInclude(Include.NON_EMPTY)
		@JsonProperty("_links")
		public Map<String, Object> getLinks() {

			var diaryController = on(DiaryController.class);
			var links = new HashMap<String, Object>();

			if (DiaryEntryActionItem.class.isInstance(item)) {

				DiaryEntry entry = ((DiaryEntryActionItem) item).getEntry();

				links.put("entry",
						Map.of("href", fromMethodCall(diaryController.getDiaryEntry(entry.getId(), null)).toUriString()));
			}

			return links;
		}
	}

	@Data
	static class ActionsReviewed {

		private String comment;

		public boolean hasComment() {
			return StringUtils.hasText(comment);
		}
	}

	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	static class CaseActionsRepresentation extends RepresentationModel<CaseActionsRepresentation> {

		private static final LinkRelation RESOLVE_REL = LinkRelation.of("resolve");

		private final TrackedCase trackedCase;
		private final ActionItems items;
		private final MessageSourceAccessor messages;

		@SuppressWarnings("null")
		public static CaseActionsRepresentation of(TrackedCase trackedCase, ActionItems items,
				MessageSourceAccessor messages) {

			var trackedCaseController = on(TrackedCaseController.class);
			var actionItemController = on(ActionItemController.class);

			return new CaseActionsRepresentation(trackedCase, items, messages)
					.add(MvcLink.of(trackedCaseController.getCase(trackedCase.getId(), trackedCase.getDepartment()), CASE))
					.addIf(items.hasUnresolvedItemsForManualResolution(),
							() -> MvcLink.of(actionItemController.resolveActions(trackedCase.getId(), null, null, null),
									RESOLVE_REL));
		}

		public TrackedCaseIdentifier getCaseId() {
			return trackedCase.getId();
		}

		public List<Map<?, ?>> getComments() {

			return trackedCase.getComments().stream()
					.sorted(Comment.BY_DATE_DESCENDING)
					.map(it -> Map.of("date", it.getDate(), "comment", it.getText()))
					.collect(Collectors.toList());
		}

		public Map<String, Object> getAnomalies() {

			return Map.of("health", toDailyItems(items.getHealthItems(), false),
					"process", toDailyItems(items.getProcessItems(), false),
					"resolved", toDailyItems(items.getResolvedItems(), true));
		}

		private List<DailyItems> toDailyItems(Streamable<ActionItem> items, boolean done) {

			return items.stream()
					.collect(Collectors.groupingBy(it -> it.getMetadata().getLastModified().toLocalDate()))
					.entrySet()
					.stream()
					.map(it -> new DailyItems(it.getKey(), it.getValue(), messages))
					.sorted(Comparator.comparing(DailyItems::getDate))
					.collect(Collectors.toUnmodifiableList());
		}

		@Getter
		@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
		private static class DailyItems extends RepresentationModel<DailyItems> {

			private final LocalDate date;
			private final List<ActionItemDto> items;

			public DailyItems(LocalDate date, List<ActionItem> items, MessageSourceAccessor messages) {

				this.date = date;
				this.items = items.stream()
						.map(it -> ActionItemDto.of(it, messages))
						.sorted(Comparator.comparing(ActionItemDto::getDate))
						.collect(Collectors.toUnmodifiableList());
			}
		}
	}

	static class CaseActionSummary {

		private final ActionItems items;
		private final TrackedCaseSummary summary;
		private final TrackedCase trackedCase;

		CaseActionSummary(TrackedCase trackedCase, ActionItems items, TrackedCaseSummary summary) {

			this.trackedCase = trackedCase;
			this.items = items;
			this.summary = summary;
		}

		public String getCaseId() {
			return summary.getCaseId();
		}

		public String getFirstName() {
			return summary.getFirstName();
		}

		public String getLastName() {
			return summary.getLastName();
		}

		public String getExtReferenceNumber() {
			return trackedCase.getExtReferenceNumber();
		}

		public String getZipCode() {
			return summary.getZipCode();
		}

		public String getDateOfBirth() {
			return summary.getDateOfBirth();
		}

		public String getEmail() {
			return summary.getEmail();
		}

		public String getCreatedAt() {
			return summary.getCreatedAt();
		}

		public String getCaseType() {
			return summary.getCaseType();
		}

		public String getCaseTypeLabel() {
			return summary.getCaseTypeLabel();
		}

		public String getPrimaryPhoneNumber() {
			return summary.getPrimaryPhoneNumber();
		}

		@Nullable
		public Map<String, Object> getQuarantine() {
			return summary.getQuarantine();
		}

		public String getStatus() {
			return summary.getStatus();
		}

		public String getName() {
			return trackedCase.getTrackedPerson().getFullName();
		}

		public float getPriority() {
			return items.getPriority() * 100.00f / 100.00f;
		}

		public List<DescriptionCode> getHealthSummary() {
			return getDescriptionCodes(ItemType.MEDICAL_INCIDENT);
		}

		public List<DescriptionCode> getProcessSummary() {
			return getDescriptionCodes(ItemType.PROCESS_INCIDENT);
		}

		boolean hasUnresolvedItems() {
			return items.hasUnresolvedItems();
		}

		@JsonProperty("_links")
		@SuppressWarnings("null")
		public Map<String, Object> getLinks() {

			var detailsLink = on(ActionItemController.class).allActions(trackedCase.getId(), null);

			return Map.of("self", Map.of("href", fromMethodCall(detailsLink).toUriString()));
		}

		private List<DescriptionCode> getDescriptionCodes(ItemType type) {

			return items.stream()
					.filter(it -> it.getType().equals(type))
					.map(ActionItem::getDescription)
					.map(Description::getCode)
					.distinct()
					.collect(Collectors.toUnmodifiableList());
		}
	}
}
