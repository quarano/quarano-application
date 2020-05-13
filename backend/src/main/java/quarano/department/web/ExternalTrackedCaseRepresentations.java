package quarano.department.web;

import quarano.department.TrackedCase;

/**
 * @author Oliver Drotbohm
 */
public interface ExternalTrackedCaseRepresentations {

	TrackedCaseSummary toSummary(TrackedCase trackedCase);
}
