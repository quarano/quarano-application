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

import lombok.RequiredArgsConstructor;
import quarano.core.web.MapperWrapper;
import quarano.department.Department;
import quarano.department.InitialReport;
import quarano.department.TrackedCase;
import quarano.tracking.Quarantine;
import quarano.tracking.TrackedPerson;
import quarano.tracking.web.TrackedPersonDto;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
public class TrackedCaseRepresentations {

	private final MapperWrapper mapper;
	private final SmartValidator validator;

	TrackedCaseDto toRepresentation(TrackedCase trackedCase) {

		var personDto = mapper.map(trackedCase.getTrackedPerson(), TrackedPersonDto.class);
		var result = mapper.map(trackedCase, TrackedCaseDto.class);

		return mapper.map(personDto, result);
	}

	InitialReportDto toRepresentation(InitialReport report) {
		return mapper.map(report, InitialReportDto.class);
	}

	TrackedCase from(TrackedCase existing, TrackedCaseDto source) {

		mapper.map(mapper.map(source, TrackedPersonDto.class), existing.getTrackedPerson());

		return mapper.map(source, existing)
				.setQuarantine(Quarantine.of(source.getQuarantineStartDate(), source.getQuarantineEndDate()));
	}

	TrackedCase from(TrackedCaseDto source, Department department) {

		var personDto = mapper.map(source, TrackedPersonDto.class);
		var person = mapper.map(personDto, TrackedPerson.class);

		return new TrackedCase(person, department)
				.setQuarantine(Quarantine.of(source.getQuarantineStartDate(), source.getQuarantineEndDate()));
	}

	InitialReport from(InitialReportDto source) {
		return mapper.map(source, InitialReport.class);
	}

	InitialReport from(InitialReport existing, InitialReportDto source) {
		return source.applyTo(mapper.map(source, existing));
	}

	Errors validateAfterEnrollment(TrackedCaseDto source, Errors errors) {

		TrackedPersonDto dto = mapper.map(source, TrackedPersonDto.class);
		validator.validate(dto, errors);

		return errors;
	}
}
