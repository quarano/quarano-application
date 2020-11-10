package quarano.department;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.account.DepartmentDataInitializer;
import quarano.account.Password.UnencryptedPassword;
import quarano.core.DataInitializer;
import quarano.core.QuaranoDateTimeProvider;
import quarano.tracking.TrackedPersonDataInitializer;

import java.time.Period;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Create some dummy accounts for test and development
 *
 * @author Patrick Otto
 */
@Component
@Order(501)
@RequiredArgsConstructor
@Slf4j
class RegistrationDataInitializer implements DataInitializer {

	private final RegistrationManagement registration;
	private final QuaranoDateTimeProvider dateTimeProvider;

	/*
	 * (non-Javadoc)
	 * @see quarano.core.DataInitializer#preInitialize()
	 */
	@Override
	public void preInitialize() {
		dateTimeProvider.setDelta(Period.ofDays(-3));
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		log.debug("Test data: creating accounts for tracked people.");

		// person 1 should not have an account yet

		// account for person 2

		registration.createTrackedPersonAccount("DemoAccount", UnencryptedPassword.of("DemoPassword"), "Markus", "Hanser",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, TrackedPersonDataInitializer.VALID_TRACKED_PERSON2_ID_DEP1);

		// account for person 3
		registration.createTrackedPersonAccount("test3", UnencryptedPassword.of("test123"), "Sandra", "Schubert",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP2, TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2);

		// account for person 4
		registration.createTrackedPersonAccount("test4", UnencryptedPassword.of("test123"), "Gustav", "Meier",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, TrackedPersonDataInitializer.VALID_TRACKED_PERSON4_ID_DEP1);

		// account for person 5
		registration.createTrackedPersonAccount("test5", UnencryptedPassword.of("test123"), "Nadine", "Ebert",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, TrackedPersonDataInitializer.VALID_TRACKED_PERSON5_ID_DEP1);

		registration.createTrackedPersonAccount("test6", UnencryptedPassword.of("test123"), "Jessica", "Wagner",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP2, TrackedPersonDataInitializer.VALID_TRACKED_PERSON4_ID_DEP2);

		registration.createTrackedPersonAccount("secUser1", UnencryptedPassword.of("secur1tyTest!"), "Siggi", "Seufert",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, TrackedPersonDataInitializer.VALID_TRACKED_SEC1_ID_DEP1);

		registration.createTrackedPersonAccount("secUser2", UnencryptedPassword.of("secur1tyTest!"), "Sarah", "Siebel",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, TrackedPersonDataInitializer.VALID_TRACKED_SEC2_ID_DEP1);

		registration.createTrackedPersonAccount("secUser3", UnencryptedPassword.of("secur1tyTest!"), "Sonja", "Straßmann",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, TrackedPersonDataInitializer.VALID_TRACKED_SEC3_ID_DEP1);

		registration.createTrackedPersonAccount("secUser4", UnencryptedPassword.of("secur1tyTest!"), "Samuel", "Soller",
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1, TrackedPersonDataInitializer.VALID_TRACKED_SEC4_ID_DEP1);
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.core.DataInitializer#postInitialize()
	 */
	@Override
	public void postInitialize() {
		dateTimeProvider.reset();
	}
}
