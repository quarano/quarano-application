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
package quarano.tracking.web;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.core.web.MapperWrapper;
import quarano.core.web.RepositoryMappingConfiguration.AggregateReferenceMappingException;
import quarano.tracking.BodyTemperature;
import quarano.tracking.ContactPersonRepository;
import quarano.tracking.DiaryEntry;
import quarano.tracking.Slot;
import quarano.tracking.web.DiaryRepresentations.DiaryEntryInput;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.modelmapper.MappingException;

/**
 * @author Oliver Drotbohm
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
class DiaryEntryMappingIntegrationTests {

	private final MapperWrapper mapper;
	private final ContactPersonRepository contacts;
	private final DiaryRepresentations representations;

	@Test
	void mapsDtoToNewEntry() {

		var source = new DiaryEntryInput();

		var result = mapper.map(source, DiaryEntry.class);

		assertThat(result.getId()).isNotNull();
		assertThat(result.getDateTime()).isNotNull();
	}

	@Test
	void rejectsInvalidContact() {

		var source = new DiaryEntryInput() //
				.setContacts(List.of(UUID.randomUUID()));

		assertThatExceptionOfType(MappingException.class) //
				.isThrownBy(() -> mapper.map(source, DiaryEntry.class)) //
				.satisfies(o_O -> {
					assertThat(o_O.getCause()).isInstanceOfSatisfying(AggregateReferenceMappingException.class, cause -> {
						assertThat(cause.getPath()).isEqualTo("contacts");
					});
				});
	}

	@Test
	void mapsEntityToDetailsDto() {

		var source = DiaryEntry.of(Slot.now());
		source.setBodyTemperature(BodyTemperature.of(40.0f));

		var result = representations.toRepresentation(source);

		assertThat(result.getId()).isEqualTo(source.getId().toString());
		assertThat(result.getBodyTemperature()).isEqualTo(source.getBodyTemperature().getValue());
		assertThat(result.getReportedAt()).isEqualTo(source.getDateTime());
		assertThat(result.getSlot()).containsEntry("date", source.getSlot().getDate());
		assertThat(result.getSlot()).containsEntry("timeOfDay", source.getSlot().getTimeOfDay().name().toLowerCase());
		assertThat(result.getSymptoms()).allSatisfy(it -> {
			assertThat(it.getId()).isNotNull();
			assertThat(it.getName()).isNotBlank();
			assertThat(it.isCharacteristic()).isNotNull();
		});
	}
}
