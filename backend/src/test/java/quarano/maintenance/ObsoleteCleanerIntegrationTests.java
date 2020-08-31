package quarano.maintenance;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.tracking.TrackedPersonDataInitializer;

@QuaranoIntegrationTest
@RequiredArgsConstructor
public class ObsoleteCleanerIntegrationTests {

	private final @NonNull TrackedCaseRepositoryHelper people;
	private final @NonNull ObsoleteCleaner deleter;
	private final @NonNull ObsoleteCleanerProperties config;

	@Test
	void verifyCorrectDeletionAndUnlinking() {


		// Joel is an index case suitable for deletion and without current diary entries
		LocalDateTime expireIndexCases = deleter.calculateThresholdDate(config.getIndexMonths());
		people.manipulatePersonDate(expireIndexCases, TrackedPersonDataInitializer.INDEX_PERSON_DELETE1_DEP1);
		people.manipulateDiaryDate(expireIndexCases, TrackedPersonDataInitializer.INDEX_PERSON_DELETE1_DEP1);
		// Carlos is a contact case suitable for deletion but with current diary entries
		LocalDateTime expireContactCases = deleter.calculateThresholdDate(config.getContactMonths());
		people.manipulatePersonDate(expireContactCases, TrackedPersonDataInitializer.CONTACT_PERSON_DELETE1_DEP1);
		// and both original case link to Sandra

		deleter.checkPeriodically();

		// both have original case link to the case of Sandra which is active and current and so we expect this case to be
		// PRESENT
		var sandra = people.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2).orElseThrow();
		// Carlos was not deleted because of fresh diary entries
		var carlos = people.findById(TrackedPersonDataInitializer.CONTACT_PERSON_DELETE1_DEP1).orElseThrow();
		// and Joel WAS deleted because all relevant data is old
		var joel = people.findById(TrackedPersonDataInitializer.INDEX_PERSON_DELETE1_DEP1);
		assertThat(joel.isPresent()).isFalse();
		// final check: the export has been produced
		File folder = new File(deleter.assignExportPath()).getParentFile();
		assertThat(findFile(TrackedPersonDataInitializer.INDEX_PERSON_DELETE1_DEP1.toString(), folder)).isTrue();
	}

	private boolean findFile(String personId, File path) {
		File[] filesInFolder = path.listFiles();
		if (filesInFolder != null) {
			for (File exportFile : filesInFolder) {
				if (exportFile.getName().contains(personId)) {
					return true;
				}
			}
		}
		return false;
	}
}
