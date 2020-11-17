package quarano.security.web;

import org.springframework.hateoas.LinkRelation;

/**
 * Link relations related to authentication.
 *
 * @author Oliver Drotbohm
 * @since 1.4
 */
public class AuthenticationLinkRelations {

	public static final LinkRelation LOGIN = LinkRelation.of("login");
}
