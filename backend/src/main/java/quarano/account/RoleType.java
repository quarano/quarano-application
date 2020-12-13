package quarano.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import org.springframework.util.Assert;

/**
 * Defines which roles exist in the application
 *
 * @author Patrick Otto
 * @author Oliver Drotbohm
 */
@AllArgsConstructor
public enum RoleType {

	ROLE_USER("ROLE_USER", false), //
	ROLE_HD_ADMIN("ROLE_HD_ADMIN", true), //
	ROLE_HD_CASE_AGENT("ROLE_HD_CASE_AGENT", true), //
	ROLE_QUARANO_ADMIN("ROLE_QUARANO_ADMIN", false), //
	ROLE_THIRD_PARTY("ROLE_THIRD_PARTY", false);

	@Getter private String code;
	@Getter private boolean isDepartmentRole;

	/**
	 * Returns whether the current role will be assigned to humans only.
	 *
	 * @return
	 */
	public boolean isHuman() {
		return !this.equals(ROLE_THIRD_PARTY);
	}

	/**
	 * Returns whether the given candidate is a {@link RoleType}.
	 *
	 * @param candidate must not be {@literal null} or empty.
	 * @return
	 */
	public static boolean isRoleType(String candidate) {

		Assert.hasText(candidate, "Candidate must not be null or empty!");

		return Arrays.stream(RoleType.values())
				.map(RoleType::name)
				.anyMatch(candidate::equals);
	}
}
