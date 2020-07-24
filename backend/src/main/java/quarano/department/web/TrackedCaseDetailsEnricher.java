package quarano.department.web;

import quarano.department.web.TrackedCaseRepresentations.TrackedCaseDetails;

/**
 * SPI to enrich {@link TrackedCaseDetails} from downstream modules.
 *
 * @author Oliver Drotbohm
 * @soundtrack The Chicks - March, March (Gaslighter)
 */
public interface TrackedCaseDetailsEnricher {

	/**
	 * Process the given {@link TrackedCaseDetails}.
	 *
	 * @param details must not be {@literal null}.
	 * @return must not be {@literal null}.
	 */
	TrackedCaseDetails enrich(TrackedCaseDetails details);
}
