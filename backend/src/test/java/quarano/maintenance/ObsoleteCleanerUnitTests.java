package quarano.maintenance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;

import quarano.QuaranoUnitTest;
import quarano.department.TrackedCase;
import quarano.diary.DiaryEntryRepository;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonRepository;

@QuaranoUnitTest
public class ObsoleteCleanerUnitTests {
	@Mock ObsoleteCleanerProperties config;
	@Mock TrackedPersonRepository trackedPeople;
	@Mock DiaryEntryRepository diaries;
	@Mock ModelMapper mapper;
	@InjectMocks ObsoleteCleaner instance;

	@BeforeEach
	void restart() {
		Mockito.reset(config, trackedPeople, diaries, mapper);
		instance = new ObsoleteCleaner(config, trackedPeople, diaries, mapper);
	}

	/**
	 * we are naturally in testing context here -> verify the path propsed
	 * is a directory and writeable
	 */
	@Test
	void testAssignExportPath() {
		String path = instance.assignExportPath();
		assertThat(path).contains("target");
		assertThat(path).contains("test-exports");
		File check = new File(new File(path).getParent());
		assertThat(check.isDirectory()).isTrue();
		assertThat(check.canWrite()).isTrue();
	}

	/**
	 * verify calculated offset date indeed differs by X number of months from today
	 */
	@Test
	void testCalculateThresholdDate() {

		int monthsIntoPast = 5;
		LocalDateTime now = LocalDateTime.now().withHour(23).withMinute(59);
		LocalDateTime thresholdDate = instance.calculateThresholdDate(monthsIntoPast);
		assertThat(now.compareTo(thresholdDate)).isEqualTo(monthsIntoPast);
	}

	/**
	 * deletion disabled by config -> quick exit expected
	 */
	@Test
	void verifyDisabledConfigStopsProcess() {

		instance.checkPeriodically();
		verify(trackedPeople, times(0)).countObsoletePersons(any(LocalDateTime.class));
	}

	/**
	 * count query returns 0 -> quick process termination expected
	 */
	@Test
	void verifyProcessStopsOnZeroMatches() {

		mockRegularConfig();
		when(trackedPeople.countObsoletePersons(any(LocalDateTime.class))).thenReturn(0L);

		instance.checkPeriodically();

		verify(trackedPeople, times(2)).countObsoletePersons(any(LocalDateTime.class));
		verify(trackedPeople, never()).findObsoletePersons(any(LocalDateTime.class), any(PageRequest.class));
	}

	/**
	 * count query returns 20 matches, the deletion process is unabled to delete anything
	 * next iteration return unchanged count of 20 macthes -> exit the deletion loop.
	 * the unchanged count indicates that even though eligible persons exists, none of
	 * them gets deleted for other business reason and thus we quit the process
	 */
	@Test
	void verifyProcessStopsOnZeroDeletions() {

		mockRegularConfig();
		when(trackedPeople.countObsoletePersons(any(LocalDateTime.class))).thenReturn(20L);
		when(trackedPeople.findObsoletePersons(any(), any())).thenReturn(Collections.emptyList());

		instance.checkPeriodically();

		verify(trackedPeople, never()).delete(any());
		verify(trackedPeople, times(4)).countObsoletePersons(any(LocalDateTime.class));
		verify(trackedPeople, times(2)).findObsoletePersons(any(LocalDateTime.class), any(PageRequest.class));
	}

	/**
	 * search result contains a match, the person has newer diaries though -> nothing deleted
	 */
	@Test
	public void verifyProcessStopsOnNewDiaries() {
		mockRegularConfig();
		when(trackedPeople.countObsoletePersons(any(LocalDateTime.class))).thenReturn(1L);
		var mockedPerson = new TrackedPerson("A", "B");
		var mockCase = mock(TrackedCase.class);
		when(mockCase.isIndexCase()).thenReturn(true);
		mockedPerson.getTrackedCases().add(mockCase);
		var mockResult = List.of(mockedPerson);
		when(trackedPeople.findObsoletePersons(any(), any())).thenReturn(mockResult);
		when(diaries.countNewerDiaryEntries(any(), any())).thenReturn(12L);

		instance.checkPeriodically();

		verify(trackedPeople, never()).delete(any());
		verify(trackedPeople, times(4)).countObsoletePersons(any(LocalDateTime.class));
		verify(trackedPeople, times(2)).findObsoletePersons(any(LocalDateTime.class), any(PageRequest.class));
	}

	void mockRegularConfig() {
		when(config.isEnabled()).thenReturn(true);
		when(config.isSaveCopy()).thenReturn(true);
		when(config.getIndexMonths()).thenReturn(2);
		when(config.getContactMonths()).thenReturn(1);
		when(config.getBatchSize()).thenReturn(5);
	}
}
