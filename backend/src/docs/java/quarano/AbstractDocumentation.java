package quarano;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

import capital.scalable.restdocs.AutoDocumentation;
import capital.scalable.restdocs.jackson.JacksonResultHandlers;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.ResponseBodySnippet;
import org.springframework.restdocs.snippet.TemplatedSnippet;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(RestDocumentationExtension.class)
public abstract class AbstractDocumentation {

	@Autowired WebApplicationContext context;
	@Autowired LinkDiscoverers links;
	@Autowired ObjectMapper jackson;

	protected MockMvc mvc;
	protected DocumentationFlow flow;

	@BeforeEach
	void setUp(RestDocumentationContextProvider restDocumentation) {

		mvc = MockMvcBuilders.webAppContextSetup(context)
				.apply(springSecurity())
				.alwaysDo(JacksonResultHandlers.prepareJackson(jackson))
				.apply(documentationConfiguration(restDocumentation)
						.operationPreprocessors()
						.withRequestDefaults(Preprocessors.prettyPrint())
						.withResponseDefaults(Preprocessors.prettyPrint()).and()
						.uris()
						.withHost("api.quarano.de")
						.withScheme("https")
						.withPort(443)
						.and()
						.snippets()
						.withAdditionalDefaults(
								AutoDocumentation.requestFields(),
								AutoDocumentation.responseFields(),
								AutoDocumentation.pathParameters(),
								AutoDocumentation.requestParameters(),
								AutoDocumentation.description(),
								new JsonResponseBodySnippet()))
				.defaultRequest(MockMvcRequestBuilders.get("/").contentType(MediaType.APPLICATION_JSON))
				.build();

		this.flow = DocumentationFlow.NONE;
	}

	/**
	 * Creates a {@link ResultMatcher} that checks for the presence of a link with the given rel.
	 *
	 * @param rel
	 * @return
	 */
	protected ResultMatcher linkWithRelIsPresent(LinkRelation rel) {
		return new LinkWithRelMatcher(rel, true);
	}

	/**
	 * Creates a {@link ResultMatcher} that checks for the non-presence of a link with the given rel.
	 *
	 * @param rel
	 * @return
	 */
	protected ResultMatcher linkWithRelIsNotPresent(LinkRelation rel) {
		return new LinkWithRelMatcher(rel, false);
	}

	protected LinkDiscoverer getDiscovererFor(MockHttpServletResponse response) {
		return links.getRequiredLinkDiscovererFor(response.getContentType());
	}

	@RequiredArgsConstructor
	private class LinkWithRelMatcher implements ResultMatcher {

		private final LinkRelation rel;
		private final boolean present;

		/*
		 * (non-Javadoc)
		 * @see org.springframework.test.web.servlet.ResultMatcher#match(org.springframework.test.web.servlet.MvcResult)
		 */
		@Override
		public void match(MvcResult result) throws Exception {

			MockHttpServletResponse response = result.getResponse();
			String content = response.getContentAsString();
			LinkDiscoverer discoverer = links.getRequiredLinkDiscovererFor(response.getContentType());

			assertThat(discoverer.findLinkWithRel(rel, content)).matches(it -> it.isPresent() == present);
		}
	}

	/**
	 * Custom {@link ResponseBodySnippet} to derive the syntax of the snippet from the {@code Content-Type} header of the
	 * response.
	 *
	 * @author Oliver Drotbohm
	 */
	private static class JsonResponseBodySnippet extends ResponseBodySnippet {

		private static final MediaType ANYTHING_JSON = MediaType.parseMediaType("application/*+json");
		private static final List<MediaType> JSON_TYPES = List.of(MediaType.APPLICATION_JSON, ANYTHING_JSON);

		public JsonResponseBodySnippet() {

			Field field = ReflectionUtils.findField(TemplatedSnippet.class, "templateName");
			ReflectionUtils.makeAccessible(field);
			ReflectionUtils.setField(field, this, "quarano-response-body");
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.restdocs.payload.AbstractBodySnippet#createModel(org.springframework.restdocs.operation.Operation)
		 */
		@Override
		protected Map<String, Object> createModel(Operation operation) {

			var model = super.createModel(operation);
			model.put("language", "");

			var contentType = getContentType(operation);

			if (JSON_TYPES.stream().anyMatch(it -> it.isCompatibleWith(contentType))) {
				model.put("language", "json,");
			}

			return model;
		}
	}
}
