package quarano.auth;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.account.DepartmentDataInitializer;
import quarano.department.activation.ActivationCodeService;
import quarano.tracking.TrackedPersonDataInitializer;

import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
class ActivationCodeServiceIntegrationTests {

	private final ActivationCodeService codes;

	@Test
	void createsActivationCode() {

		var personId = TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1;
		var departmentId = DepartmentDataInitializer.DEPARTMENT_ID_DEP1;

		var code = codes.createActivationCode(personId, departmentId).get();

		assertThat(code.isWaitingForActivation()).isTrue();
		assertThat(codes.getValidCode(code.getId()).isSuccess()).isTrue();
	}
}
