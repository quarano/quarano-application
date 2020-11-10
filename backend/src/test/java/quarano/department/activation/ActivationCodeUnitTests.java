package quarano.department.activation;

import static org.assertj.core.api.Assertions.*;

import quarano.account.DepartmentDataInitializer;
import quarano.tracking.TrackedPersonDataInitializer;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
class ActivationCodeUnitTests {

	@Test
	void cancelsAlreadyCancelledCode() {

		ActivationCode code = new ActivationCode(LocalDateTime.now().plusHours(1),
				TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1, DepartmentDataInitializer.DEPARTMENT_ID_DEP1);

		assertThat(code.cancel().isSuccess()).isTrue();
		assertThat(code.cancel().isSuccess()).isTrue();
	}
}
