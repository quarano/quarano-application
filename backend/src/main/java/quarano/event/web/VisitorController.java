package quarano.event.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import quarano.core.web.MappedPayloads;
import quarano.event.EventCode;
import quarano.event.EventManagement;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class VisitorController {
	private final EventManagement eventManagement;
	private final VisitorRepresentions visitorRepresentions;

	@PostMapping("/ext/event/{eventCode:" + EventCode.REGEX + "}")
	HttpEntity<?> addEventEntry(@PathVariable String eventCode,
			@Valid @RequestBody VisitorTransmissionDto visitorTransmissionDto, Errors errors) {
		return MappedPayloads.of(visitorTransmissionDto, errors)
				.notFoundIf(!EventCode.isValid(eventCode))
				.alwaysMap(VisitorTransmissionDto::validate)
				.map(visitorRepresentions::from)
				.flatMap(it -> eventManagement.registerVisitorListForEvent(EventCode.of(eventCode), it))
				.onValidGet(() -> ResponseEntity.noContent().build());
	}
}
