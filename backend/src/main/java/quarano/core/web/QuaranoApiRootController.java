package quarano.core.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.mvc.MvcLink;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The root API resource.
 *
 * @author Oliver Drotbohm
 * @since 1.4
 */
@RestController
@RequiredArgsConstructor
class QuaranoApiRootController {

	private final WebEndpointProperties actuatorProperties;

	@GetMapping("/")
	QuaranoApiRoot root() {

		var controller = on(QuaranoApiRootController.class);
		var self = MvcLink.of(controller.root(), IanaLinkRelations.SELF);
		var actuators = self
				.withHref(self.getHref().concat(actuatorProperties.getBasePath().substring(1)))
				.withRel(LinkRelation.of("actuators"));

		return new QuaranoApiRoot()
				.add(self)
				.add(actuators);
	}
}
