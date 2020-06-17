package quarano.diary.web;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.core.web.MapperWrapper;
import quarano.core.web.RepositoryMappingModule.AggregateReferenceMappingException;
import quarano.diary.DiaryEntry;
import quarano.diary.Slot;
import quarano.diary.web.DiaryRepresentations;
import quarano.diary.web.DiaryRepresentations.DiaryEntryInput;
import quarano.tracking.BodyTemperature;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

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
	private final DiaryRepresentations representations;

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

		var source = DiaryEntry.of(Slot.now(), TrackedPersonIdentifier.of(UUID.randomUUID()))//
				.setBodyTemperature(BodyTemperature.of(40.0f));

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
