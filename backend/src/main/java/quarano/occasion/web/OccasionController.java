package quarano.occasion.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.occasion.OccasionManagement;

import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Oliver Drotbohm
 * @since 1.4
 */
@RestController
@RequiredArgsConstructor
class OccasionController {

	private final @NonNull OccasionManagement events;
	private final @NonNull OccasionRepresentions representations;

	@GetMapping("/hd/occasions")
	HttpEntity<?> getOccasion() {

		var eventSummaries = events.findAll().map(representations::toSummary);

		var model = HalModelBuilder.emptyHalModel()
				.embed(eventSummaries, LinkRelation.of("events"))
				.build();

		return ResponseEntity.ok(model);
	}
}
