package quarano.department.activation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.account.DepartmentDataInitializer;
import quarano.core.DataInitializer;
import quarano.department.activation.ActivationCode.ActivationCodeIdentifier;
import quarano.tracking.TrackedPersonDataInitializer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Create some dummy accounts for test and development
 *
 * @author Patrick Otto
 */
@Component
@Order(500)
@Slf4j
@RequiredArgsConstructor
public class ActivationCodeDataInitializer implements DataInitializer {

	private final ActivationCodeRepository codeRepo;

	public static final ActivationCode ACTIVATIONCODE_PERSON1 = new ActivationCode(LocalDateTime.of(2025, 10, 10, 0, 0),
			TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1, DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
			ActivationCodeIdentifier.of(UUID.fromString("acc8b747-1eac-4db4-a8f3-d2a8bbe8320d")));

	public static final ActivationCode ACTIVATIONCODE_PERSON2_REDEEMED = new ActivationCode(
			LocalDateTime.of(2025, 10, 10, 0, 0), TrackedPersonDataInitializer.VALID_TRACKED_PERSON2_ID_DEP1,
			DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
			ActivationCodeIdentifier.of(UUID.fromString("85829049-9c4c-4a20-a854-70e813adaab4")));

	public static final ActivationCode ACTIVATIONCODE_PERSON3_CANCELED = new ActivationCode(
			LocalDateTime.of(2020, 04, 10, 0, 0), TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2,
			DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
			ActivationCodeIdentifier.of(UUID.fromString("dc304a58-082a-4b9c-a635-374082658561")));

	public static final ActivationCode ACTIVATIONCODE_PERSON3_REDEEMED = new ActivationCode(
			LocalDateTime.of(2025, 10, 10, 0, 0), TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2,
			DepartmentDataInitializer.DEPARTMENT_ID_DEP2,
			ActivationCodeIdentifier.of(UUID.fromString("80e43d28-24f9-4781-9d0c-f9d722987803")));

	// codes for security tests

	public static final ActivationCode ACTIVATIONCODE_SEC1_REDEEMED = new ActivationCode(
			LocalDateTime.of(2025, 10, 10, 0, 0), TrackedPersonDataInitializer.VALID_TRACKED_SEC1_ID_DEP1,
			DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
			ActivationCodeIdentifier.of(UUID.fromString("2ae94cb2-f82e-4634-a9c0-dec239f1b9c1")));

	public static final ActivationCode ACTIVATIONCODE_SEC2_REDEEMED = new ActivationCode(
			LocalDateTime.of(2025, 10, 10, 0, 0), TrackedPersonDataInitializer.VALID_TRACKED_SEC2_ID_DEP1,
			DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
			ActivationCodeIdentifier.of(UUID.fromString("bc4c009d-a082-4e43-9d1e-a91330df05fc")));

	public static final ActivationCode ACTIVATIONCODE_SEC3_REDEEMED = new ActivationCode(
			LocalDateTime.of(2025, 10, 10, 0, 0), TrackedPersonDataInitializer.VALID_TRACKED_SEC3_ID_DEP1,
			DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
			ActivationCodeIdentifier.of(UUID.fromString("f5f8c370-ecd6-4e23-aeb8-957a5c585fe8")));

	public static final ActivationCode ACTIVATIONCODE_SEC4_REDEEMED = new ActivationCode(
			LocalDateTime.of(2025, 10, 10, 0, 0), TrackedPersonDataInitializer.VALID_TRACKED_SEC4_ID_DEP1,
			DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
			ActivationCodeIdentifier.of(UUID.fromString("4f1d01d9-096a-496d-9d71-ff709eb46596")));

	public static final ActivationCode ACTIVATIONCODE_SEC5_OPEN = new ActivationCode(LocalDateTime.of(2025, 10, 10, 0, 0),
			TrackedPersonDataInitializer.VALID_TRACKED_SEC5_ID_DEP1, DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
			ActivationCodeIdentifier.of(UUID.fromString("4cf65303-1ad1-4659-80bd-6231839afc47")));

	public static final ActivationCode ACTIVATIONCODE_SEC6_OPEN = new ActivationCode(LocalDateTime.of(2025, 10, 10, 0, 0),
			TrackedPersonDataInitializer.VALID_TRACKED_SEC6_ID_DEP1, DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
			ActivationCodeIdentifier.of(UUID.fromString("7e4cac7e-8924-4a7f-b209-8eefe25a06c6")));

	public static final ActivationCode ACTIVATIONCODE_SEC7_OPEN = new ActivationCode(LocalDateTime.of(2025, 10, 10, 0, 0),
			TrackedPersonDataInitializer.VALID_TRACKED_SEC7_ID_DEP1, DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
			ActivationCodeIdentifier.of(UUID.fromString("a594e8df-1932-455c-a755-8362731a9aeb")));

	public static final ActivationCode ACTIVATIONCODE_SEC8_OPEN = new ActivationCode(LocalDateTime.of(2025, 10, 10, 0, 0),
			TrackedPersonDataInitializer.VALID_TRACKED_SEC8_ID_DEP1, DepartmentDataInitializer.DEPARTMENT_ID_DEP1,
			ActivationCodeIdentifier.of(UUID.fromString("a25b9eb0-82bc-4ffc-a37f-fa7a8d20cbc8")));

	private static List<ActivationCode> CODES = new ArrayList<>();

	static {
		CODES.add(ACTIVATIONCODE_PERSON1);
		CODES.add(ACTIVATIONCODE_PERSON2_REDEEMED);
		CODES.add(ACTIVATIONCODE_PERSON3_CANCELED);
		CODES.add(ACTIVATIONCODE_PERSON3_REDEEMED);

		CODES.add(ACTIVATIONCODE_SEC1_REDEEMED);
		CODES.add(ACTIVATIONCODE_SEC2_REDEEMED);
		CODES.add(ACTIVATIONCODE_SEC3_REDEEMED);
		CODES.add(ACTIVATIONCODE_SEC4_REDEEMED);

		CODES.add(ACTIVATIONCODE_SEC5_OPEN);
		CODES.add(ACTIVATIONCODE_SEC6_OPEN);
		CODES.add(ACTIVATIONCODE_SEC7_OPEN);
		CODES.add(ACTIVATIONCODE_SEC8_OPEN);
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		// set status of codes
		ACTIVATIONCODE_PERSON2_REDEEMED.redeem();

		ACTIVATIONCODE_PERSON3_CANCELED.cancel();
		ACTIVATIONCODE_PERSON3_REDEEMED.redeem();

		// store each code
		for (ActivationCode code : CODES) {

			log.warn("Test data: Adding activation code {} with status {} for tracked person {}.", code.getId(),
					code.getStatus(), code.getTrackedPersonId());

			codeRepo.save(code);
		}
	}
}
