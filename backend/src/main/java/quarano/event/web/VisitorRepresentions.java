package quarano.event.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import quarano.core.web.MapperWrapper;
import quarano.event.VisitorGroup;

@RequiredArgsConstructor
@Component
public class VisitorRepresentions {

	private final MapperWrapper mapperWrapper;

	public VisitorGroup from(VisitorTransmissionDto visitorTransmissionDto) {
		return mapperWrapper.map(visitorTransmissionDto, VisitorGroup.class);
	}
}
