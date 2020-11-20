package quarano;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import quarano.core.web.QuaranoHttpHeaders;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationRequestFactory;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.operation.OperationResponseFactory;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.util.Assert;

/**
 * Helper class to ease the documentation of a certain hypermedia flow.
 *
 * @author Oliver Gierke
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentationFlow {

	public static DocumentationFlow NONE = new DocumentationFlow(null, new ArrayList<>());
	private static String ELLIPSIS = "...";

	private final @Nullable String name;
	private final List<OperationPreprocessor> customResponsePreprocessors;

	public static DocumentationFlow of(String name) {
		return new DocumentationFlow(name, new ArrayList<>());
	}

	public DocumentationFlow withResponsePreprocessor(OperationPreprocessor processor) {

		var newList = new ArrayList<>(customResponsePreprocessors);
		newList.add(processor);

		return new DocumentationFlow(name, newList);
	}

	/**
	 * Creates a documenting {@link ResultHandler} for a named step.
	 *
	 * @param step must not be {@literal null} or empty.
	 * @param snippets must not be {@literal null}.
	 * @return
	 */
	public ResultHandler document(String step, Snippet... snippets) {
		return document(step, true, snippets);
	}

	/**
	 * Creates a documenting {@link ResultHandler} for a named step without masking links.
	 *
	 * @param step must not be {@literal null}.
	 * @param snippets must not be {@literal null}.
	 * @return
	 */
	public ResultHandler documentUnmasked(String step, Snippet... snippets) {
		return document(step, false, snippets);
	}

	private ResultHandler document(String step, boolean maskUris, Snippet... snippets) {

		Assert.notNull(step, "Step name must not be null!");

		if (name == null) {
			return result -> {};
		}

		var authenticationProcessor = new AuthenticationHeaderProcessor();

		var preprocessRequest = preprocessRequest(prettyPrint(),
				replacePattern(Pattern.compile("\"password\" : \".*\""), ellipsisField("password")),
				replacePattern(Pattern.compile("\"passwordConfirm\" : \".*\""), ellipsisField("passwordConfirm")),
				authenticationProcessor);

		var responseProcessors = new ArrayList<OperationPreprocessor>();

		if (maskUris) {
			responseProcessors.add(maskLinks(ELLIPSIS));
		}

		responseProcessors.addAll(List.of(prettyPrint(), authenticationProcessor));
		responseProcessors.addAll(customResponsePreprocessors);

		var preprocessResponse = preprocessResponse(responseProcessors.toArray(OperationPreprocessor[]::new));

		return MockMvcRestDocumentation.document(name.concat("/").concat(step), preprocessRequest, preprocessResponse,
				snippets);
	}

	private static String ellipsisField(String key) {
		return String.format("\"%s\" : \"%s\"", key, ELLIPSIS);
	}

	private static class AuthenticationHeaderProcessor implements OperationPreprocessor {

		/*
		 * (non-Javadoc)
		 * @see org.springframework.restdocs.operation.preprocess.OperationPreprocessor#preprocess(org.springframework.restdocs.operation.OperationRequest)
		 */
		@Override
		public OperationRequest preprocess(OperationRequest request) {

			if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				return request;
			}

			var headers = new HttpHeaders();
			headers.putAll(request.getHeaders());
			headers.set(HttpHeaders.AUTHORIZATION, "Bearer $TOKEN");

			return new OperationRequestFactory().createFrom(request, headers);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.restdocs.operation.preprocess.OperationPreprocessor#preprocess(org.springframework.restdocs.operation.OperationResponse)
		 */
		@Override
		public OperationResponse preprocess(OperationResponse response) {

			if (!response.getHeaders().containsKey(QuaranoHttpHeaders.AUTH_TOKEN)) {
				return response;
			}

			var headers = new HttpHeaders();
			headers.putAll(response.getHeaders());
			headers.set(QuaranoHttpHeaders.AUTH_TOKEN, "$TOKEN");

			return new OperationResponseFactory().createFrom(response, headers);
		}
	}
}
