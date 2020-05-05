package quarano.account.web;

import org.springframework.hateoas.LinkRelation;

/**
 * @author Patrick Otto
 */
public class StaffAccountLinkRelations {

	public static final LinkRelation DELETE = LinkRelation.of("delete");
	public static final LinkRelation DISABLE = LinkRelation.of("disable");
}
