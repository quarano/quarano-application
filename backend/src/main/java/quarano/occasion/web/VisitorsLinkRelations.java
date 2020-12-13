package quarano.occasion.web;

import org.springframework.hateoas.LinkRelation;

/**
 * Link relations for visitor related API.
 *
 * @author Oliver Drotbohm
 * @since 1.4
 */
public class VisitorsLinkRelations {
	public static final LinkRelation SUBMIT_VISITORS = LinkRelation.of("submit-visitors");
}
