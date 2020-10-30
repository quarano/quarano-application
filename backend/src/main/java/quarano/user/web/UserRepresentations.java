package quarano.user.web;

import lombok.RequiredArgsConstructor;
import quarano.account.Department;
import quarano.core.web.MapperWrapper;
import quarano.department.TrackedCase;
import quarano.department.web.TrackedCaseRepresentations;
import quarano.department.web.TrackedCaseRepresentations.EnrollmentDto;
import quarano.tracking.TrackedPerson;
import quarano.tracking.web.TrackedPersonDto;

import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
class UserRepresentations {

	private final MapperWrapper mapper;
	private final TrackedCaseRepresentations delegate;

	EnrollmentDto toEnrollmentRepresentation(TrackedCase trackedCase) {
		return delegate.toEnrollmentRepresentation(trackedCase);
	}

	DepartmentDto toRepresentation(Department department) {
		return mapper.map(department, DepartmentDto.class);
	}

	TrackedPersonDto toRepresentation(TrackedPerson person) {
		return mapper.map(person, TrackedPersonDto.class);
	}
}
