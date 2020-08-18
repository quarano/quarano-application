package quarano.actions.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Department;
import quarano.account.Department.DepartmentIdentifier;
import quarano.actions.ActionItemRepository;
import quarano.actions.ActionItemsManagement;
import quarano.actions.web.AnomaliesRepresentations.ActionsReviewed;
import quarano.actions.web.AnomaliesRepresentations.CaseActionSummary;
import quarano.actions.web.AnomaliesRepresentations.CaseActionsRepresentation;
import quarano.core.web.LoggedIn;
import quarano.core.web.MappedPayloads;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.TrackedCaseRepository;

import java.util.Comparator;

import javax.validation.Valid;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Oliver Drotbohm
 */
@RestController
@RequiredArgsConstructor
public class AnomaliesController {

	private final @NonNull ActionItemRepository items;
	private final @NonNull ActionItemsManagement actionItems;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull AnomaliesRepresentations representations;

	@GetMapping("/api/hd/actions/{identifier}")
	HttpEntity<CaseActionsRepresentation> getAnomalies(@PathVariable TrackedCaseIdentifier identifier,
			@LoggedIn DepartmentIdentifier department) {

		return ResponseEntity.of(cases.findById(identifier)
				.filter(it -> it.belongsTo(department))
				.map(this::toRepresentation));
	}

	@PutMapping("/api/hd/actions/{identifier}/resolve")
	HttpEntity<?> resolveActions(@PathVariable TrackedCaseIdentifier identifier,
			@Valid @RequestBody ActionsReviewed payload, Errors errors,
			@LoggedIn DepartmentIdentifier department) {

		var trackedCase = cases.findById(identifier)
				.filter(it -> it.belongsTo(department))
				.orElse(null);

		return MappedPayloads.of(payload, errors)
				.notFoundIf(trackedCase == null)
				.map(ActionsReviewed::getComment)
				.peek(it -> actionItems.resolveItemsFor(trackedCase, it))
				.concludeIfValid(__ -> getAnomalies(identifier, department));
	}

	@GetMapping("/api/hd/actions")
	public RepresentationModel<?> getAllCasesByAnomalies(@LoggedIn Department department) {

		var actionRepresentations = cases.findByDepartmentId(department.getId())
				.map(it -> representations.toSummary(it, items.findUnresolvedByActiveCase(it)))
				.stream()
				.filter(CaseActionSummary::hasUnresolvedItems)
				.sorted(Comparator.comparing(CaseActionSummary::getPriority).reversed())
				.map(representations::toSummaryWithOriginCases);

		return HalModelBuilder.emptyHalModel()
				.embed(actionRepresentations, CaseActionSummary.class)
				.build();
	}

	private CaseActionsRepresentation toRepresentation(TrackedCase trackedCase) {

		var id = trackedCase.getTrackedPerson().getId();
		var actionItems = items.findByTrackedPerson(id);

		return representations.toRepresentation(trackedCase, actionItems);
	}
}
