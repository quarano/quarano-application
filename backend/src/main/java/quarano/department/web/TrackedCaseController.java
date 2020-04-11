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

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.auth.web.LoggedIn;
import quarano.department.Enrollment;
import quarano.department.EnrollmentException;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;
import quarano.tracking.TrackedPerson;
import quarano.tracking.web.TrackedPersonDto;
import quarano.tracking.web.TrackingController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
		var map = cases.findByTrackedPerson(person).map(TrackedCase::getEnrollment);

		return ResponseEntity.of(map);
	}

	@PutMapping("/api/enrollment/details")
	HttpEntity<?> submitEnrollmentDetails(@Validated @RequestBody TrackedPersonDto dto, Errors errors,
			@LoggedIn TrackedPerson user) {

		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(toMap(errors));
		}

		cases.findByTrackedPerson(user) //
				.map(TrackedCase::markEnrollmentDetailsSubmitted) //
				.ifPresentOrElse(cases::save, () -> new IllegalArgumentException("Couldn't find case!"));

		return tracking.createTrackedPerson(dto, errors, user);
	}

	@GetMapping("/api/enrollment/questionaire")
	HttpEntity<?> showQuestionaire(@LoggedIn TrackedPerson person) {

		var report = cases.findByTrackedPerson(person) //
				.map(TrackedCase::getInitialReport) //
				.map(it -> mapper.map(it, InitialReportDto.class)) //
				.orElseGet(() -> new InitialReportDto());

		return ResponseEntity.ok(report);
	}

	@RequestMapping(path = "/api/enrollment/questionaire", method = { RequestMethod.POST, RequestMethod.PUT })
	HttpEntity<?> addQuestionaire(@Validated @RequestBody InitialReportDto dto, Errors errors,
			@LoggedIn TrackedPerson person) {

		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(errors);
		}

		cases.findByTrackedPerson(person) //
				.map(it -> {

					var report = it.getOrCreateInitialReport();
					mapper.map(dto, report);

					return it.submitQuestionaire(report);

				}).ifPresent(cases::save);

		return ResponseEntity.ok().build();
	}

	@ExceptionHandler
	ResponseEntity<?> handle(EnrollmentException o_O) {
		return ResponseEntity.badRequest().body(o_O.getMessage());
	}

	private Map<String, String> toMap(Errors errors) {

		Map<String, String> fields = new HashMap<>();

		errors.getFieldErrors().forEach(it -> {
			fields.put(it.getField(), accessor.getMessage(it));
		});

		return fields;
	}

	@RequiredArgsConstructor
	static class EnrollmentDto {

		private final @Getter(onMethod = @__(@JsonUnwrapped)) Enrollment enrollment;

		@JsonProperty("_links")
		public Map<String, Object> getLinks() {

			if (enrollment.isComplete()) {
				return Collections.emptyMap();
			}

			if (enrollment.isCompletedPersonalData()) {
				return Map.of("details", Map.of("href", "/api/enrollment/details"));
			}

			return null;
		}
	}
}
