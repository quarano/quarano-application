package quarano.actions.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Department;
import quarano.account.Department.DepartmentIdentifier;
import quarano.actions.ActionItemRepository;
import quarano.actions.ActionItemsManagement;
import quarano.core.web.ErrorsDto;
import quarano.core.web.LoggedIn;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.TrackedCaseRepository;
import quarano.department.web.ExternalTrackedCaseRepresentations;

import java.util.Comparator;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.context.support.MessageSourceAccessor;
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
public class ActionItemController {

	private final @NonNull ActionItemRepository items;
	private final @NonNull ActionItemsManagement actionItems;
	private final @NonNull MessageSourceAccessor messages;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull ExternalTrackedCaseRepresentations trackedCaseRepresentations;

	@GetMapping("/api/hd/actions/{identifier}")
	HttpEntity<?> allActions(@PathVariable TrackedCaseIdentifier identifier,
			@LoggedIn DepartmentIdentifier department) {

		var trackedCase = cases.findById(identifier)
				.filter(it -> it.belongsTo(department))
				.orElse(null);

		if (trackedCase == null) {
			return ResponseEntity.notFound().build();
		}

		var id = trackedCase.getTrackedPerson().getId();

		return ResponseEntity.ok(CaseActionsRepresentation.of(trackedCase, items.findByTrackedPerson(id), messages));
	}

	@PutMapping("/api/hd/actions/{identifier}/resolve")
	HttpEntity<?> resolveActions(@PathVariable TrackedCaseIdentifier identifier,
			@Valid @RequestBody ActionsReviewed payload,
			Errors errors,
			@LoggedIn DepartmentIdentifier department) {

		TrackedCase trackedCase = cases.findById(identifier)
				.filter(it -> it.belongsTo(department))
				.orElse(null);

		if (trackedCase == null) {
			return ResponseEntity.notFound().build();
		}

		if (errors.hasErrors()) {
			return ErrorsDto.of(errors, messages).toBadRequest();
		}

		actionItems.resolveItemsFor(trackedCase, payload.getComment());

		return allActions(identifier, department);
	}

	@GetMapping("/api/hd/actions")
	Stream<?> getActions(@LoggedIn Department department) {

		return cases.findByDepartmentId(department.getId())
				.map(trackedCase -> {

					var summary = trackedCaseRepresentations.toSummary(trackedCase);

					return new CaseActionSummary(trackedCase, items.findUnresolvedByActiveCase(trackedCase), summary);

				})
				.stream()
				.filter(CaseActionSummary::hasUnresolvedItems)
				.sorted(Comparator.comparing(CaseActionSummary::getPriority).reversed());
	}
}
