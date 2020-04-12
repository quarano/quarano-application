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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.auth.web.LoggedIn;
import quarano.core.web.ErrorsDto;
import quarano.department.Enrollment;
import quarano.department.EnrollmentException;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.TrackedPerson;
import quarano.tracking.web.TrackedPersonDto;
import quarano.tracking.web.TrackingController;

import java.util.Map;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * @author Oliver Drotbohm
 */
@RestController
@RequiredArgsConstructor
class TrackedCaseController {

	private final @NonNull TrackingController tracking;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull ModelMapper mapper;
	private final @NonNull MessageSourceAccessor accessor;

	@GetMapping("/api/cases")
	Stream<?> allCases() {
		return cases.findAll().stream();
	}

	@GetMapping("/api/enrollments")
	Stream<?> allEnrollments() {
		return cases.findAll().map(TrackedCase::getEnrollment).stream();
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

		cases.findByTrackedPerson(user) //
				.map(TrackedCase::markEnrollmentDetailsSubmitted) //
				.ifPresentOrElse(cases::save, () -> new IllegalArgumentException("Couldn't find case!"));

		return tracking.createTrackedPerson(dto, errors, user);
	}

	@GetMapping("/api/enrollment/questionnaire")
	HttpEntity<?> showQuestionaire(@LoggedIn TrackedPerson person) {

		var report = cases.findByTrackedPerson(person) //
				.map(TrackedCase::getInitialReport) //
				.map(it -> mapper.map(it, InitialReportDto.class)) //
				.orElseGet(() -> new InitialReportDto());

		return ResponseEntity.ok(report);
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

		trackedCase = apply(dto, trackedCase); //
		cases.save(trackedCase);

		return ResponseEntity.ok().build();
	}

	private TrackedCase apply(InitialReportDto dto, TrackedCase it) {

		var report = it.getOrCreateInitialReport();
		mapper.map(dto, report);
		report = dto.applyTo(report);

		return it.submitQuestionnaire(report);
	}

	@ExceptionHandler
	ResponseEntity<?> handle(EnrollmentException o_O) {
		return ResponseEntity.badRequest().body(o_O.getMessage());
	}

	@RequiredArgsConstructor
	static class EnrollmentDto {

		private final @Getter(onMethod = @__(@JsonUnwrapped)) Enrollment enrollment;

		@JsonProperty("_links")
		public Map<String, Object> getLinks() {

			var questionnareUri = fromMethodCall(on(TrackedCaseController.class).addQuestionaire(null, null, null))
					.toUriString();

			var detailsUri = fromMethodCall(on(TrackingController.class).enrollmentOverview(null)).toUriString();

			if (enrollment.isComplete()) {
				return Map.of(//
						"details", Map.of("href", detailsUri), //
						"questionnaire", Map.of("href", questionnareUri), //
						"contacts", Map.of("href", "/api/enrollment/contacts"));
			}

			if (enrollment.isCompletedQuestionnaire()) {
				return Map.of(//
						"details", Map.of("href", detailsUri), //
						"questionnaire", Map.of("href", questionnareUri), //
						"next", Map.of("href", "/api/enrollment/contacts"));
			}

			if (enrollment.isCompletedPersonalData()) {
				return Map.of(//
						"details", Map.of("href", detailsUri), //
						"next", Map.of("href", questionnareUri));
			}

			return Map.of(//
					"next", Map.of("href", detailsUri));
		}
	}
}
