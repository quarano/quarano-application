package quarano.department.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import quarano.department.Enrollment;
import quarano.tracking.web.TrackingController;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

@RequiredArgsConstructor
public class EnrollmentDto {

	private final @Getter(onMethod = @__(@JsonUnwrapped)) Enrollment enrollment;

	@JsonProperty("_links")
	public Map<String, Object> getLinks() {

		var caseController = on(TrackedCaseController.class);
		var trackingController = on(TrackingController.class);

		var questionnareUri = fromMethodCall(caseController.addQuestionaire(null, null, null)).toUriString();
		var detailsUri = fromMethodCall(trackingController.enrollmentOverview(null)).toUriString();
		var encountersUri = fromMethodCall(trackingController.getEncounters(null)).toUriString();
		var reopenUri = fromMethodCall(caseController.reopenEnrollment(null)).toUriString();

		if (enrollment.isComplete()) {
			return Map.of(//
					"details", Map.of("href", detailsUri),
					"questionnaire", Map.of("href", questionnareUri),
					"encounters", Map.of("href", encountersUri),
					"reopen", Map.of("href", reopenUri));
		}

		if (enrollment.isCompletedQuestionnaire()) {
			return Map.of(//
					"details", Map.of("href", detailsUri),
					"questionnaire", Map.of("href", questionnareUri),
					"next", Map.of("href", encountersUri));
		}

		if (enrollment.isCompletedPersonalData()) {
			return Map.of(//
					"details", Map.of("href", detailsUri),
					"next", Map.of("href", questionnareUri));
		}

		return Map.of(//
				"next", Map.of("href", detailsUri));
	}
}
