package quarano.department.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Account;
import quarano.account.Department;
import quarano.account.Department.DepartmentIdentifier;
import quarano.account.DepartmentRepository;
import quarano.core.web.LoggedIn;
import quarano.core.web.MappedPayloads;
import quarano.department.CaseType;
import quarano.department.EnrollmentCompletion;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.TrackedCaseProperties;
import quarano.department.TrackedCaseRepository;
import quarano.department.web.TrackedCaseRepresentations.CommentInput;
import quarano.department.web.TrackedCaseRepresentations.TrackedCaseDto;
import quarano.department.web.TrackedCaseRepresentations.ValidatedContactCase;
import quarano.department.web.TrackedCaseRepresentations.ValidatedIndexCase;
import quarano.diary.DiaryManagement;
import quarano.tracking.TrackedPerson;
import quarano.tracking.web.TrackedPersonDto;
import quarano.tracking.web.TrackingController;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Oliver Drotbohm
 */
@RestController
@RequiredArgsConstructor
public class TrackedCaseController {

	private final @NonNull TrackingController tracking;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull DiaryManagement diaries;
	private final @NonNull DepartmentRepository departments;
	private final @NonNull TrackedCaseProperties configuration;
	private final @NonNull TrackedCaseRepresentations representations;

	@GetMapping(path = "/api/hd/cases")
	RepresentationModel<?> getCases(@LoggedIn Department department,
			@RequestParam("q") Optional<String> query,
			@RequestParam("projection") Optional<String> projection,
			@RequestParam("type") Optional<String> type) {

		var summaries = cases.findFiltered(query, type, department.getId())
				.map(projection.filter(it -> it.equals("select"))
						.<Function<TrackedCase, Object>> map(it -> representations::toSelect)
						.orElse(representations::toSummary))
				.toList();

		return HalModelBuilder.emptyHalModel()
				.embed(summaries, TrackedCaseSummary.class)
				.build();
	}

	@PostMapping(path = "/api/hd/cases", params = "type=contact")
	HttpEntity<?> postContactCase(@ValidatedContactCase @RequestBody TrackedCaseDto.Input payload, Errors errors,
			@LoggedIn Department department) {
		return createTrackedCase(payload, CaseType.CONTACT, department, errors);
	}

	@PostMapping(path = "/api/hd/cases", params = "type=index")
	HttpEntity<?> postIndexCase(@ValidatedIndexCase @RequestBody TrackedCaseDto.Input payload, Errors errors,
			@LoggedIn Department department) {

		return postCase(payload, errors, department);
	}

	@PostMapping("/api/hd/cases")
	HttpEntity<?> postCase(@ValidatedIndexCase @RequestBody TrackedCaseDto.Input payload, Errors errors,
			@LoggedIn Department department) {

		return createTrackedCase(payload, CaseType.INDEX, department, errors);
	}

	HttpEntity<?> createTrackedCase(TrackedCaseDto payload, CaseType type, Department department, Errors errors) {

		var trackedCase = representations.from(payload, department, type, errors);

		return MappedPayloads.of(trackedCase, errors)
				.map(cases::save)
				.map(representations::toRepresentation)
				.concludeIfValid(it -> {

					var location = on(TrackedCaseController.class).getCase(trackedCase.getId(), department);

					return ResponseEntity
							.created(URI.create(fromMethodCall(location).toUriString()))
							.body(it);
				});
	}

	@GetMapping(path = "/api/hd/cases/form")
	HttpEntity<?> getCaseForm() {

		return ResponseEntity.ok(TrackedCaseDefaults.of(configuration));
	}

	@GetMapping("/api/hd/cases/{identifier}")
	public HttpEntity<?> getCase(@PathVariable TrackedCaseIdentifier identifier, @LoggedIn Department department) {

		return ResponseEntity.of(cases.findById(identifier)
				.filter(it -> it.belongsTo(department))
				.map(representations::toRepresentation));
	}

	@GetMapping("/api/hd/cases/{identifier}/questionnaire")
	HttpEntity<?> getQuestionnaire(@PathVariable TrackedCaseIdentifier identifier, @LoggedIn Department department) {

		return ResponseEntity.of(cases.findById(identifier)
				.filter(it -> it.belongsTo(department))
				.map(TrackedCase::getQuestionnaire)
				.map(representations::toRepresentation));
	}

	@GetMapping("/api/hd/cases/{identifier}/diary")
	HttpEntity<?> getDiaryOfCase(@PathVariable TrackedCaseIdentifier identifier,
			@LoggedIn DepartmentIdentifier departmentIdentifier) {

		TrackedCase trackedCase = cases.findById(identifier)
				.filter(it -> it.belongsTo(departmentIdentifier))
				.orElse(null);

		if (trackedCase == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(HalModelBuilder.halModel()
				.embed(createDiaryEntrySummaries(trackedCase), TrackedCaseDiaryEntrySummary.class)
				.build());
	}

	@GetMapping("/api/hd/cases/{identifier}/contacts")
	HttpEntity<?> getContactsOfCase(@PathVariable TrackedCaseIdentifier identifier,
			@LoggedIn DepartmentIdentifier department) {

		TrackedCase trackedCase = cases.findById(identifier)
				.filter(it -> it.belongsTo(department))
				.orElse(null);

		if (trackedCase == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(HalModelBuilder.halModel()
				.embed(createContactSummaries(trackedCase), TrackedCaseContactSummary.class)
				.build());
	}

	// PUT Mapping for transformation into index case

	@PutMapping("/api/hd/cases/{identifier}")
	HttpEntity<?> putCase(@PathVariable TrackedCaseIdentifier identifier,
			@RequestBody TrackedCaseDto.Input payload,
			Errors errors) {

		var existing = cases.findById(identifier).orElse(null);

		return MappedPayloads.of(payload, errors)
				.notFoundIf(existing == null)
				.map((it, nested) -> representations.from(it, existing, nested))
				.map(cases::save)
				.map(representations::toRepresentation)
				.concludeIfValid(ResponseEntity::ok);
	}

	@DeleteMapping("/api/hd/cases/{identifier}")
	HttpEntity<?> concludeCase(@PathVariable TrackedCaseIdentifier identifier,
			@LoggedIn DepartmentIdentifier department) {

		return ResponseEntity.of(cases.findById(identifier)
				.filter(it -> it.belongsTo(department))
				.map(TrackedCase::conclude)
				.map(cases::save));
	}

	@PostMapping("/api/hd/cases/{identifier}/comments")
	HttpEntity<?> postComment(@PathVariable TrackedCaseIdentifier identifier, @LoggedIn Account account,
			@Valid @RequestBody CommentInput payload, Errors errors) {

		var trackedCase = cases.findById(identifier)
				.filter(it -> it.belongsTo(account.getDepartmentId()))
				.orElse(null);

		return MappedPayloads.of(payload, errors)
				.notFoundIf(trackedCase == null)
				.map(it -> representations.from(it, account))
				.map(trackedCase::addComment)
				.map(cases::save)
				.map(representations::toRepresentation)
				.concludeIfValid(ResponseEntity::ok);
	}

	@GetMapping("/api/enrollments")
	Stream<?> allEnrollments() {

		return cases.findAll()
				.map(TrackedCase::getEnrollment)
				.map(representations::toRepresentation)
				.stream();
	}

	@GetMapping("/api/enrollment")
	HttpEntity<?> enrollment(@LoggedIn TrackedPerson person) {

		var map = cases.findByTrackedPerson(person)
				.map(TrackedCase::getEnrollment)
				.map(representations::toRepresentation);

		return ResponseEntity.of(map);
	}

	@PutMapping("/api/enrollment/details")
	HttpEntity<?> submitEnrollmentDetails(@Validated @RequestBody TrackedPersonDto dto, Errors errors,
			@LoggedIn TrackedPerson user) {

		return MappedPayloads.of(errors)
				.onValidGet(() -> {

					tracking.updateTrackedPersonDetails(dto, errors, user);

					cases.findByTrackedPerson(user)
							.map(TrackedCase::submitEnrollmentDetails)
							.ifPresentOrElse(cases::save, () -> new IllegalArgumentException("Couldn't find case!"));

					return ResponseEntity.ok()
							.header(HttpHeaders.LOCATION, getEnrollmentLink())
							.build();
				});
	}

	@GetMapping("/api/enrollment/questionnaire")
	HttpEntity<?> showQuestionaire(@LoggedIn TrackedPerson person) {

		var report = cases.findByTrackedPerson(person)
				.map(TrackedCase::getQuestionnaire)
				.map(it -> representations.toRepresentation(it))
				.orElseGet(() -> new QuestionnaireDto());

		return ResponseEntity.ok()
				.header(HttpHeaders.LOCATION, getEnrollmentLink())
				.body(report);
	}

	@PutMapping(path = "/api/enrollment/questionnaire")
	HttpEntity<?> addQuestionaire(@Validated @RequestBody QuestionnaireDto dto, Errors errors,
			@LoggedIn TrackedPerson person) {

		var trackedCase = cases.findByTrackedPerson(person)
				.orElseThrow(() -> new IllegalStateException("No case found for tracked person " + person.getId() + "!"));

		if (!trackedCase.getEnrollment().isCompletedPersonalData()) {

			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
					.body(representations.resolve("enrollment.detailsSubmissionRequired"));
		}

		return MappedPayloads.of(dto, errors)
				.map(QuestionnaireDto::validate)
				.map(it -> representations.from(it, trackedCase.getQuestionnaire()))
				.map(trackedCase::submitQuestionnaire)
				.map(cases::save)
				.concludeIfValid(__ -> {
					return ResponseEntity.ok()
							.header(HttpHeaders.LOCATION, getEnrollmentLink())
							.build();
				});
	}

	@PostMapping("/api/enrollment/completion")
	HttpEntity<?> completeEnrollment(@LoggedIn TrackedPerson person,
			@RequestParam("withoutEncounters") boolean withoutEncounters) {

		var completion = withoutEncounters
				? EnrollmentCompletion.WITHOUT_ENCOUNTERS
				: EnrollmentCompletion.WITH_ENCOUNTERS;

		cases.findByTrackedPerson(person)
				.map(it -> it.markEnrollmentCompleted(completion))
				.map(cases::save);

		return ResponseEntity.ok()
				.header(HttpHeaders.LOCATION, getEnrollmentLink())
				.build();
	}

	@DeleteMapping("/api/enrollment/completion")
	HttpEntity<?> reopenEnrollment(@LoggedIn TrackedPerson person) {

		cases.findByTrackedPerson(person)
				.map(TrackedCase::reopenEnrollment)
				.map(cases::save);

		return ResponseEntity.ok()
				.header(HttpHeaders.LOCATION, getEnrollmentLink())
				.build();
	}

	private Collection<TrackedCaseContactSummary> createContactSummaries(TrackedCase trackedCase) {

		var encounters = trackedCase.getTrackedPerson().getEncounters();

		return encounters.getContactDatesGroupedByContactPerson().entrySet().stream()//
				.map(it -> representations.toContactSummary(it.getKey(), it.getValue())) //
				.collect(Collectors.toUnmodifiableList());
	}

	private Collection<TrackedCaseDiaryEntrySummary> createDiaryEntrySummaries(TrackedCase trackedCase) {

		return diaries.findDiaryFor(trackedCase.getTrackedPerson()).stream()
				.map(representations::toDiaryEntrySummary)
				.collect(Collectors.toUnmodifiableList());
	}

	@SuppressWarnings("null")
	private static String getEnrollmentLink() {
		return fromMethodCall(on(TrackedCaseController.class).enrollment(null)).toUriString();
	}
}
