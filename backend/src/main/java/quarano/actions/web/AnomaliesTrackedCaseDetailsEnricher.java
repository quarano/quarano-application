package quarano.actions.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.actions.ActionItemsManagement;
import quarano.department.web.TrackedCaseDetailsEnricher;
import quarano.department.web.TrackedCaseRepresentations.TrackedCaseDetails;

import org.springframework.hateoas.server.mvc.MvcLink;
import org.springframework.stereotype.Component;

/**
 * {@link TrackedCaseDetailsEnricher} to populate the number of unresolved anomalies on a {@link TrackedCaseDetails}
 * instance.
 *
 * @author Oliver Drotbohm
 * @soundtrack The Chicks - My Best Friend's Wedding (Gaslighter)
 */
@Component
@RequiredArgsConstructor
class AnomaliesTrackedCaseDetailsEnricher implements TrackedCaseDetailsEnricher {

	private final @NonNull ActionItemsManagement items;

	/*
	 * (non-Javadoc)
	 * @see quarano.department.web.TrackedCaseDetailsEnricher#enrich(quarano.department.web.TrackedCaseRepresentations.TrackedCaseDetails)
	 */
	@Override
	@SuppressWarnings("null")
	public TrackedCaseDetails enrich(TrackedCaseDetails details) {

		var trackedCase = details.getTrackedCase();

		return details.withAdditionalProperty("openAnomaliesCount", items::getNumberOfOpenItems)
				.addIf(trackedCase.isEnrollmentCompleted(),
						() -> MvcLink.of(on(AnomaliesController.class).getAnomalies(trackedCase.getId(), null),
								AnomaliesLinkRelations.ANOMALIES));
	}
}
