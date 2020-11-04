package quarano.security.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Oliver Drotbohm
 */
@QuaranoWebIntegrationTest
@RequiredArgsConstructor
public class SecurityWebIntegrationTests {

	private final MockMvc mvc;

	@Test
	@WithQuaranoUser("admin")
	void staffAdminCanAccessStaffResources() throws Exception {

		mvc.perform(get("/hd/cases"))
				.andExpect(status().isOk());
	}
}
