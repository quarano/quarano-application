package de.wevsvirushackathon.coronareport.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Defines which roles exist in the application
 * @author Patrick Otto
 *
 */
@AllArgsConstructor
public enum RoleType {

	ROLE_USER("ROLE_USER"),
	ROLE_HD_ADMIN("ROLE_HD_ADMIN"),
	ROLE_HD_CASE_AGENT("ROLE_HD_CASE_AGENT");
	
	@Getter
	private String code;
}
