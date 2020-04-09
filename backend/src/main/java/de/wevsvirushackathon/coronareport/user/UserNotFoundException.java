package de.wevsvirushackathon.coronareport.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = 6204511600376304550L;

	private String username;
}
