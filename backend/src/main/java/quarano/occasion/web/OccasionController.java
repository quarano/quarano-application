package quarano.occasion.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.hateoas.server.mvc.MvcLink;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import quarano.core.web.MappedPayloads;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.occasion.Occasion;
import quarano.occasion.OccasionCode;
import quarano.occasion.OccasionManagement;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

/**
 * @author Oliver Drotbohm
 * @since 1.4
 */
@RestController
@RequiredArgsConstructor
class OccasionController {

	private final @NonNull OccasionManagement occasions;
	private final @NonNull OccasionRepresentions representations;


	@GetMapping("/hd/occasions")
	HttpEntity<?> getOccasions() {

		var eventSummaries = occasions.findAll().map(this::toModel);

		var model = HalModelBuilder.emptyHalModel()
				.embed(eventSummaries, LinkRelation.of("occasions"))
				.build();

		return ResponseEntity.ok(model);
	}

	@GetMapping("/ext/occasions/{occasionCode:" + OccasionCode.REGEX + "}")
	HttpEntity<?> getOccasion(@PathVariable OccasionCode occasionCode) {

		return occasions.findOccasionBy(occasionCode)
				.map(this::toModel)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());

	}

	@PostMapping("/hd/cases/{id}/occasions")
	HttpEntity<?> postOccasions(@PathVariable("id") TrackedCaseIdentifier trackedCaseId,
			@RequestBody OccasionsDto payload,
			Errors errors) {

		return MappedPayloads.of(payload, errors)
				.flatMap(it -> occasions.createOccasion(it.title, it.start, it.end, trackedCaseId))
				.map(representations::toSummary)
				.concludeIfValid(ResponseEntity::ok);
	}

	private EntityModel<OccasionRepresentions.OccasionSummary> toModel(Occasion occasion) {
		return EntityModel.of(representations.toSummary(occasion))
				.add(MvcLink.of(on(OccasionController.class).getOccasion(occasion.getOccasionCode()), IanaLinkRelations.SELF));
	}
}
