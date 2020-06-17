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

import static org.assertj.core.api.Assertions.*;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.core.web.RepositoryMappingModule.AggregateReferenceMappingException;
import quarano.core.web.RepositoryMappingModule.NullHandling;
import quarano.reference.Symptom;
import quarano.reference.SymptomRepository;

import java.net.URI;
import java.util.UUID;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.repository.support.Repositories;
import org.springframework.web.util.UriTemplate;

/**
 * Integration tests for {@link RepositoryMappingModule}.
 *
 * @author Oliver Drotbohm
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
class RepositoryMappingModuleIntegrationTests {

	private final String VALID_URI = "/symptoms/9ebd9beb-5af6-4c69-9b79-f04b472d1bec";
	private final String INVALID_URI = "/symptoms/9ebd9beb-5af6-4c69-9b79-f04b472d1beb";

	private final ApplicationContext context;
	private final ConversionService conversions;
	private final SymptomRepository symptoms;

	@Test
	void extractsSymptomFromUri() {

		var source = new Source();
		source.symptom = VALID_URI;
		source.symptomUri = URI.create(VALID_URI);

		Sink sink = initMapper().map(source, Sink.class);

		assertThat(sink.symptom).isNotNull();
		assertThat(sink.symptomUri).isNotNull();
	}

	@Test
	void rejectsFailedLookupWithException() {

		var source = new Source();
		source.symptom = INVALID_URI;

		assertThatExceptionOfType(MappingException.class) //
				.isThrownBy(() -> initMapper().map(source, Sink.class)) //
				.satisfies(it -> {
					it.getErrorMessages().forEach(message -> {
						assertThat(message.getCause()).isInstanceOf(AggregateReferenceMappingException.class);
					});
				});
	}

	@Test
	void returnsNullForMissingAggregateIfExplicitlyConfigured() {

		var mapper = initMapper(it -> {
			it.nullHandling(Symptom.class, NullHandling.RETURN_NULL);
		});

		var source = new Source();
		source.symptom = INVALID_URI;

		var sink = mapper.map(source, Sink.class);

		assertThat(sink.symptom).isNull();
		assertThat(sink.symptomUri).isNull();
	}

	@Test
	void writesUriForAggregate() {

		var symptom = symptoms.findById(UUID.fromString("9ebd9beb-5af6-4c69-9b79-f04b472d1bec")).orElseThrow();

		var sink = new Sink();
		sink.symptom = symptom;
		sink.symptomUri = symptom;

		var source = initMapper().map(sink, Source.class);

		assertThat(source.symptom).isEqualTo(VALID_URI);
		assertThat(source.symptomUri.toString()).isEqualTo(VALID_URI);
	}

	private ModelMapper initMapper() {
		return initMapper(__ -> {});
	}

	private ModelMapper initMapper(Consumer<RepositoryMappingModule> configurer) {

		var module = new RepositoryMappingModule(new Repositories(context), conversions)
				.register(new UriTemplateIdentifierProcessor() //
						.register(Symptom.class, new UriTemplate("/symptoms/{id}"), "id"));

		configurer.accept(module);

		var mapper = new ModelMapper();
		mapper.registerModule(module);

		return mapper;
	}

	@Data
	static class Source {
		String symptom;
		URI symptomUri;
	}

	@Data
	static class Sink {
		Symptom symptom;
		Symptom symptomUri;
	}
}
