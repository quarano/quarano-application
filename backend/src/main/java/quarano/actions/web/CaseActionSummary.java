package quarano.actions.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import quarano.actions.ActionItem;
import quarano.actions.ActionItem.ItemType;
import quarano.actions.ActionItems;
import quarano.actions.Description;
import quarano.actions.DescriptionCode;
import quarano.department.TrackedCase;
import quarano.department.web.TrackedCaseSummary;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Oliver Drotbohm
 * @author Patrick Otto
 */
class CaseActionSummary {

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
	public Map<String, Object> getLinks() {

		var detailsLink = on(ActionItemController.class).allActions(trackedCase.getId(), null);

		return Map.of("self", Map.of("href", fromMethodCall(detailsLink).toUriString()));
	}

	private List<DescriptionCode> getDescriptionCodes(ItemType type) {

		return items.stream() //
				.filter(it -> it.getType().equals(type)) //
				.map(ActionItem::getDescription) //
				.map(Description::getCode) //
				.distinct() //
				.collect(Collectors.toUnmodifiableList());
	}
}
