package quarano.tracking.web;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.tracking.DiaryManagement;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.TrackedPersonRepository;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Oliver Drotbohm
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
class DiaryControllerIntegrationTests {

	private final DiaryController controller;
	private final TrackedPersonRepository repository;
	private final DiaryManagement entries;

	@Test
	void rendersDiaryEntryCorrectly() throws Exception {

		var person = repository.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2).orElseThrow();
		var entry = entries.findDiaryFor(person).iterator().next();

		ResponseEntity<?> entity = controller.getDiaryEntry(entry.getId(), person);

		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
}
