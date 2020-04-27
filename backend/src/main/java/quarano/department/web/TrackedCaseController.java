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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.auth.web.LoggedIn;
import quarano.core.web.ErrorsDto;
import quarano.department.Department;
import quarano.department.Department.DepartmentIdentifier;
import quarano.department.DepartmentRepository;
import quarano.department.EnrollmentCompletion;
import quarano.department.InitialReport;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.TrackedCaseProperties;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.TrackedPerson;
import quarano.tracking.web.TrackedPersonDto;
import quarano.tracking.web.TrackingController;

import java.net.URI;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
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
class TrackedCaseController {

	private final @NonNull TrackingController tracking;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull DepartmentRepository departments;
	private final @NonNull MessageSourceAccessor accessor;
	private final @NonNull TrackedCaseProperties configuration;
	private final @NonNull TrackedCaseRepresentations representations;
	private final @NonNull MessageSourceAccessor messages;

	@GetMapping(path = "/api/hd/cases", produces = MediaTypes.HAL_JSON_VALUE)
	RepresentationModel<?> getCases(@LoggedIn Department department) {

		return CollectionModel.of(cases.findByDepartmentIdOrderByLastNameAsc(department.getId()) //
				.map(it -> TrackedCaseSummary.of(it, messages)) //
				.toList());
	}

	@PostMapping("/api/hd/cases")
	HttpEntity<?> postCase(@Valid @RequestBody TrackedCaseDto payload, Errors errors, @LoggedIn Department department) {

		if (payload.validate(errors).hasErrors()) {
			return ErrorsDto.of(errors, accessor).toBadRequest();
		}

		var trackedCase = cases.save(representations.from(payload, department));
		var location = on(TrackedCaseController.class).getCase(trackedCase.getId(), department);

		return ResponseEntity //
				.created(URI.create(fromMethodCall(location).toUriString())) //
				.body(representations.toRepresentation(trackedCase));
	}

	@GetMapping(path = "/api/hd/cases/form")
	HttpEntity<?> getCaseForm() {
		return ResponseEntity.ok(TrackedCaseDefaults.of(configuration));
	}

	@GetMapping("/api/hd/cases/{identifier}")
	HttpEntity<?> getCase(@PathVariable TrackedCaseIdentifier identifier, @LoggedIn Department department) {

		return ResponseEntity.of(cases.findById(identifier) //
				.filter(it -> it.belongsTo(department)) //
				.map(representations::toRepresentation));
	}

	@PutMapping("/api/hd/cases/{identifier}")
	HttpEntity<?> putCase(@PathVariable TrackedCaseIdentifier identifier, //
			@Valid @RequestBody TrackedCaseDto payload, //
			Errors errors) {

		var existing = cases.findById(identifier).orElse(null);

		if (existing == null) {
			return ResponseEntity.notFound().build();
		}

		if (existing.getEnrollment().isComplete()) {
			errors = representations.validateAfterEnrollment(payload, errors);
		}

		if (errors.hasErrors()) {
			return ErrorsDto.of(errors, accessor).toBadRequest();
		}

		return ResponseEntity.of(cases.findById(identifier) //
				.map(it -> representations.from(it, payload)) //
				.map(cases::save) //
				.map(representations::toRepresentation));
	}

	@DeleteMapping("/api/hd/cases/{identifier}")
	HttpEntity<?> concludeCase(@PathVariable TrackedCaseIdentifier identifier,
			@LoggedIn DepartmentIdentifier department) {

		return ResponseEntity.of(cases.findById(identifier) //
				.filter(it -> it.belongsTo(department)) //
				.map(TrackedCase::conclude) //
				.map(cases::save));
	}

	@GetMapping("/api/enrollments")
	Stream<?> allEnrollments() {

		return cases.findAll() //
				.map(TrackedCase::getEnrollment) //
				.map(EnrollmentDto::new) //
				.stream();
	}

	@GetMapping("/api/enrollment")
	HttpEntity<?> enrollment(@LoggedIn TrackedPerson person) {

		var map = cases.findByTrackedPerson(person) //
				.map(TrackedCase::getEnrollment) //
				.map(EnrollmentDto::new);

		return ResponseEntity.of(map);
	}

	@PutMapping("/api/enrollment/details")
	HttpEntity<?> submitEnrollmentDetails(@Validated @RequestBody TrackedPersonDto dto, Errors errors,
			@LoggedIn TrackedPerson user) {

		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(ErrorsDto.of(errors, accessor));
		}

		tracking.updateTrackedPersonDetails(dto, errors, user);

		cases.findByTrackedPerson(user) //
				.map(TrackedCase::submitEnrollmentDetails) //
				.ifPresentOrElse(cases::save, () -> new IllegalArgumentException("Couldn't find case!"));

		return ResponseEntity.ok() //
				.header(HttpHeaders.LOCATION, getEnrollmentLink()) //
				.build();
	}

	@GetMapping("/api/enrollment/questionnaire")
	HttpEntity<?> showQuestionaire(@LoggedIn TrackedPerson person) {

		var report = cases.findByTrackedPerson(person) //
				.map(TrackedCase::getInitialReport) //
				.map(it -> representations.toRepresentation(it)) //
				.orElseGet(() -> new InitialReportDto());

		return ResponseEntity.ok() //
				.header(HttpHeaders.LOCATION, getEnrollmentLink()) //
				.body(report);
	}

	@PutMapping(path = "/api/enrollment/questionnaire")
	HttpEntity<?> addQuestionaire(@Validated @RequestBody InitialReportDto dto, Errors errors,
			@LoggedIn TrackedPerson person) {

		var trackedCase = cases.findByTrackedPerson(person) //
				.orElseThrow(() -> new IllegalStateException("No case found for tracked person " + person.getId() + "!"));

		if (!trackedCase.getEnrollment().isCompletedPersonalData()) {

			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED) //
					.body(accessor.getMessage("enrollment.detailsSubmissionRequired"));
		}

		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(ErrorsDto.of(errors, accessor));
		}

		// Trigger custom validation
		var validated = dto.validate(errors);

		if (validated.hasErrors()) {
			return ResponseEntity.badRequest().body(ErrorsDto.of(errors, accessor));
		}

		InitialReport from = representations.from(trackedCase.getOrCreateInitialReport(), dto);
		trackedCase.submitQuestionnaire(from);

		cases.save(trackedCase);

		return ResponseEntity.ok() //
				.header(HttpHeaders.LOCATION, getEnrollmentLink()) //
				.build();
	}

	@PostMapping("/api/enrollment/completion")
	HttpEntity<?> completeEnrollment(@LoggedIn TrackedPerson person,
			@RequestParam("withoutEncounters") boolean withoutEncounters) {

		var completion = withoutEncounters //
				? EnrollmentCompletion.WITHOUT_ENCOUNTERS //
				: EnrollmentCompletion.WITH_ENCOUNTERS;

		cases.findByTrackedPerson(person) //
				.map(it -> it.markEnrollmentCompleted(completion)) //
				.map(cases::save);

		return ResponseEntity.ok() //
				.header(HttpHeaders.LOCATION, getEnrollmentLink()) //
				.build();
	}

	@DeleteMapping("/api/enrollment/completion")
	HttpEntity<?> reopenEnrollment(@LoggedIn TrackedPerson person) {

		cases.findByTrackedPerson(person) //
				.map(TrackedCase::reopenEnrollment) //
				.map(cases::save);

		return ResponseEntity.ok() //
				.header(HttpHeaders.LOCATION, getEnrollmentLink()) //
				.build();
	}

	private static String getEnrollmentLink() {
		return fromMethodCall(on(TrackedCaseController.class).enrollment(null)).toUriString();
	}
}
