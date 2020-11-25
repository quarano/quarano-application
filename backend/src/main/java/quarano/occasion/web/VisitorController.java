package quarano.occasion.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.core.web.MappedPayloads;
import quarano.occasion.OccasionCode;
import quarano.occasion.OccasionManagement;
import quarano.occasion.web.OccasionRepresentions.VisitorGroupDto;

import javax.validation.Valid;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author David Bauknecht
 * @author Oliver Drotbohm
 * @since 1.4
 */
@RestController
@RequiredArgsConstructor
class VisitorController {

	private final @NonNull OccasionManagement occasions;
	private final @NonNull OccasionRepresentions representations;

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
}
