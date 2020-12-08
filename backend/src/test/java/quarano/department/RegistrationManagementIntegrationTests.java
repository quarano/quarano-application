package quarano.department;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.vavr.api.VavrAssertions.assertThat;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.account.DepartmentDataInitializer;
import quarano.account.DepartmentRepository;
import quarano.tracking.TrackedPerson;

import org.junit.jupiter.api.Test;

/**
 * Integration tests for {@link RegistrationManagement}.
 *
 * @author Oliver Drotbohm
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
public class RegistrationManagementIntegrationTests {

	private final RegistrationManagement registrations;
	private final DepartmentRepository departments;

	@Test // CORE-585
	void failsRegistrationIfEmailIsMissing() {

		var department = departments.findById(DepartmentDataInitializer.DEPARTMENT_ID_DEP1).orElseThrow();
		var person = new TrackedPerson("Max", "Mustermann");
		var trackedCase = new TrackedCase(person, CaseType.CONTACT, department);

		assertThat(registrations.initiateRegistration(trackedCase)).isFailure();
		assertThat(trackedCase.getComments()).hasSize(1);
	}
}
