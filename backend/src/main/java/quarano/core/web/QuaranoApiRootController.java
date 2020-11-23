package quarano.core.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The root API resource.
 *
 * @author Oliver Drotbohm
 * @since 1.4
 */
@RestController
class QuaranoApiRootController {

	@GetMapping("/")
	QuaranoApiRoot root() {
		return new QuaranoApiRoot();
	}
}
