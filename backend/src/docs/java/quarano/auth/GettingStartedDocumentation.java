/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano.auth;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import quarano.AbstractDocumentation;
import quarano.DocumentationFlow;
import quarano.QuaranoWebIntegrationTest;
import quarano.actions.web.AnomaliesLinkRelations;
import quarano.core.web.QuaranoHttpHeaders;
import quarano.department.web.TrackedCaseLinkRelations;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.ResultHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
@QuaranoWebIntegrationTest
class GettingStartedDocumentation extends AbstractDocumentation {

	private final ObjectMapper jackson;
	private final LinkDiscoverers discoverers;

	@Test
	void loginAsAdmin() throws Exception {

		this.flow = DocumentationFlow.of("getting-started");

		var payload = Map.of("username", "agent1", "password", "agent1");

		var response = mvc.perform(post("/api/login")
				.content(jackson.writeValueAsString(payload)))
				.andExpect(status().isOk())
				.andExpect(header().string(QuaranoHttpHeaders.AUTH_TOKEN, is(notNullValue())))
				.andDo(documentLogin())
				.andReturn().getResponse();

		var token = response.getHeader(QuaranoHttpHeaders.AUTH_TOKEN);
		var discoverer = discoverers.getRequiredLinkDiscovererFor(response.getContentType());
		var nextLink = discoverer.findRequiredLinkWithRel(IanaLinkRelations.NEXT, response.getContentAsString());

		mvc.perform(get(nextLink.getHref())
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
				.andExpect(status().isOk())
				.andDo(documentUserDetailsForAdmin());
	}

	private ResultHandler documentLogin() {
		return flow.document("login",
				responseHeaders(headerWithName(QuaranoHttpHeaders.AUTH_TOKEN).description("The authentication token to use.")),
				relaxedLinks(linkWithRel(IanaLinkRelations.NEXT.value()).description("The dashboard")));
	}

	private ResultHandler documentUserDetailsForAdmin() {

		var links = relaxedLinks(
				linkWithRel(TrackedCaseLinkRelations.CASES.value())
						.description("All cases available. See <<agent.cases>> for details."),
				linkWithRel(AnomaliesLinkRelations.ANOMALIES.value())
						.description("All cases based on the anomalies they expose. See <<agent.anomalies>> for details."));

		return flow.document("agent-user-home", links);
	}
}
