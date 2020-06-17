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
package quarano.core.web;

import lombok.Value;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;
import org.springframework.web.util.UriTemplate;

/**
 * An {@link IdentifierProcessor} that allows to register {@link UriTemplate}s for aggregate types so that the model
 * mapping is able to extract the actual aggregate identifier from URIs.
 *
 * @author Oliver Drotbohm
 */
public class UriTemplateIdentifierProcessor implements IdentifierProcessor {

	private final Map<Class<?>, ExtractionConfiguration> configuration = new HashMap<>();

	/**
	 * Registers the given {@link UriTemplate} and template variable name for the given aggregate type.
	 *
	 * @param type must not be {@literal null}.
	 * @param template must not be {@literal null}.
	 * @param variableName must not be {@literal null} or empty.
	 * @return
	 */
	public UriTemplateIdentifierProcessor register(Class<?> type, UriTemplate template, String variableName) {

		Assert.notNull(type, "Aggregate type must not be null!");
		Assert.notNull(template, "UriTemplate must not be null!");
		Assert.hasText(variableName, "Variable name must not be null or empty!");

		configuration.put(type, ExtractionConfiguration.of(template, variableName));

		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.core.web.IdentifierProcessor#preProcessIdentifier(java.lang.Object, java.lang.Class)
	 */
	@Override
	public Object preProcessIdentifier(Object identifier, Class<?> targetType) {

		ExtractionConfiguration configuration = this.configuration.get(targetType);

		if (configuration == null) {
			return identifier;
		}

		var variables = configuration.template.match(identifier.toString());

		return variables.get(configuration.variableName);
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.core.web.IdentifierProcessor#postProcessIdentifier(java.lang.Object, java.lang.Class)
	 */
	@Override
	public Object postProcessIdentifier(Object identifier, Class<?> sourceType) {

		ExtractionConfiguration configuration = this.configuration.get(sourceType);

		if (configuration == null) {
			return identifier;
		}

		return configuration.template.expand(Map.of(configuration.variableName, identifier));
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.core.web.IdentifierProcessor#getAdditionalSourceTypes()
	 */
	@Override
	public Collection<Class<?>> getAdditionalIdentifierTypes() {
		return Set.of(URI.class);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.plugin.core.Plugin#supports(java.lang.Object)
	 */
	public boolean supports(Class<?> delimiter) {

		return configuration.keySet().stream() //
				.anyMatch(delimiter::isAssignableFrom);
	}

	@Value(staticConstructor = "of")
	private static class ExtractionConfiguration {

		UriTemplate template;
		String variableName;
	}
}
