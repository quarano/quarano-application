package quarano.department.web;

import org.springframework.hateoas.LinkRelation;

/**
 * @author Oliver Drotbohm
 */
public class TrackedCaseLinkRelations {

	public static final LinkRelation START_TRACKING = LinkRelation.of("start-tracking");
	public static final LinkRelation CONCLUDE = LinkRelation.of("conclude");
	public static final LinkRelation RENEW = LinkRelation.of("renew");
}
