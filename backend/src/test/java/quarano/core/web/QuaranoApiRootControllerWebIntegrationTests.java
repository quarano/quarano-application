package quarano.core.web;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import quarano.AbstractDocumentation;
import quarano.DocumentationFlow;
import quarano.QuaranoWebIntegrationTest;
import quarano.security.web.AuthenticationLinkRelations;
import quarano.user.web.UserLinkRelations;

import org.junit.jupiter.api.Test;

/**
 * Web integration tests for {@link QuaranoApiRootController}.
 *
 * @author Oliver Drotbohm
 */
@QuaranoWebIntegrationTest
public class QuaranoApiRootControllerWebIntegrationTests extends AbstractDocumentation {

	DocumentationFlow flow = DocumentationFlow.of("root-resource");

	@Test
	void accessRootResource() throws Exception {

		var loginLink = linkWithRel(AuthenticationLinkRelations.LOGIN.value())
				.description("Log into Quarano. See <<authentication>> for details.");
		var resetPasswordLink = linkWithRel(UserLinkRelations.RESET_PASSWORD.value())
				.description("Reset your password. See <<authentication.password-reset>> for details.");

		mvc.perform(get("/"))
				.andExpect(status().isOk())
				.andDo(flow.document("access-root-resource", links(loginLink, resetPasswordLink)));
	}
}
