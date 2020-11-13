package quarano.account.web;

import org.springframework.hateoas.LinkRelation;

/**
 * @author Patrick Otto
 */
class StaffAccountLinkRelations {

	public static final LinkRelation DELETE = LinkRelation.of("delete");
	public static final LinkRelation DISABLE = LinkRelation.of("disable");
	public static final LinkRelation RESET_PASSWORD = LinkRelation.of("reset-password");
}
