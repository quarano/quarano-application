package quarano.occasion.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.AuthenticationManager;
import quarano.account.RoleType;
import quarano.core.web.MappedPayloads;
import quarano.core.web.QuaranoApiRoot;
import quarano.occasion.OccasionCode;
import quarano.occasion.OccasionManagement;
import quarano.occasion.web.OccasionRepresentions.VisitorGroupDto;

import javax.validation.Valid;

import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for visitor related API.
 *
 * @author David Bauknecht
 * @author Oliver Drotbohm
 * @since 1.4
 */
@RestController
@RequiredArgsConstructor
class VisitorController {

	private final @NonNull OccasionManagement occasions;
	private final @NonNull OccasionRepresentions representations;

	/**
	 * Submits a visitor group for the occasion with the given occasion code.
	 *
	 * @param occasionCode must not be {@literal null} or empty.
	 * @param visitorTransmissionDto the actual payload.
	 * @param errors potential binding errors.
	 * @return will never be {@literal null}.
	 */
	@PostMapping("/ext/occasions/{occasionCode:" + OccasionCode.REGEX + "}")
	HttpEntity<?> addEventEntry(@PathVariable String occasionCode,
			@Valid @RequestBody VisitorGroupDto visitorTransmissionDto, Errors errors) {

		return MappedPayloads.of(visitorTransmissionDto, errors)
				.notFoundIf(!OccasionCode.isValid(occasionCode))
				.alwaysMap(VisitorGroupDto::validate)
				.map(representations::from)
				.flatMap(it -> occasions.registerVisitorGroupForEvent(OccasionCode.of(occasionCode), it))
				.onValidGet(() -> ResponseEntity.noContent().build());
	}

	/**
	 * {@link RepresentationModelProcessor} to add a link to
	 * {@link VisitorController#addEventEntry(String, VisitorGroupDto, Errors)} to the API root.
	 *
	 * @author Oliver Drotbohm
	 */
	@Component
	@RequiredArgsConstructor
	static class VisitorRepresentationModelProcessor implements RepresentationModelProcessor<QuaranoApiRoot> {

		private final AuthenticationManager authentication;

		/*
		 * (non-Javadoc)
		 * @see org.springframework.hateoas.server.RepresentationModelProcessor#process(org.springframework.hateoas.RepresentationModel)
		 */
		@Override
		@SuppressWarnings("null")
		public QuaranoApiRoot process(QuaranoApiRoot model) {

			return model.addIf(authentication.hasRole(RoleType.ROLE_THIRD_PARTY),
					() -> linkTo(methodOn(VisitorController.class).addEventEntry(null, null, null))
							.withRel(VisitorsLinkRelations.SUBMIT_VISITORS));
		}
	}
}
