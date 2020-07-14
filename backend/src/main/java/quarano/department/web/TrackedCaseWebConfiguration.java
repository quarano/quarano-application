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
package quarano.department.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import quarano.core.web.UriTemplateIdentifierProcessor;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.TrackedCaseIdentifier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriTemplate;

/**
 * Web configuration contributions for {@link TrackedCase}s.
 *
 * @author Oliver Drotbohm
 */
@Configuration
public class TrackedCaseWebConfiguration {

	@Bean
	UriTemplateIdentifierProcessor trackedCaseProcessor() {

		return new UriTemplateIdentifierProcessor()
				.register(TrackedCase.class, new UriTemplate("/cases/{id}"), "id", (TrackedCaseIdentifier id) -> {
					return fromMethodCall(on(TrackedCaseController.class).getCase(id, null)).toUriString();
				});
	}
}
