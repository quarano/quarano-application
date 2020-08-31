package quarano.maintenance;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import quarano.diary.DiaryEntryRepository;
import quarano.maintenance.web.TrackedPersonDto;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonRepository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Component
@Slf4j
@RequiredArgsConstructor
public class ObsoleteCleaner {

	private static final int FIVE_MINS = 5 * 60 * 1000;
	private static final int ONE_HOUR = 12 * FIVE_MINS;

	private final @NonNull ObsoleteCleanerProperties config;
	private final @NonNull TrackedPersonRepository trackedPeople;
	private final @NonNull DiaryEntryRepository diaries;
	private final @NonNull ModelMapper mapper;
	private ObjectWriter xmlWriter;
	private String exportPath;
	private long executionStarted = 0;

	@Scheduled(fixedRate = ONE_HOUR, initialDelay = FIVE_MINS)
	@Transactional
	@SchedulerLock(name = "ObsoleteCleaner", lockAtLeastFor = "PT20S", lockAtMostFor = "PT5M")
	public void checkPeriodically() {

		if (!config.isEnabled()) {
			return;
		}

		executionStarted = System.currentTimeMillis();

		var isContactCase = true;
		var saveCopy = config.isSaveCopy();

		deleteAll(!isContactCase, saveCopy, config.getIndexMonths());
		deleteAll(isContactCase, saveCopy, config.getContactMonths());
	}

	@PostConstruct
	public void tweakConfig() {
		mapper.getConfiguration().setAmbiguityIgnored(true);
		XmlMapper xmlMapper = new XmlMapper();
		xmlWriter = xmlMapper.writerWithDefaultPrettyPrinter();
		exportPath = assignExportPath();
		log.info("Using export path {}", exportPath);
	}

	void deleteAll(boolean isContactCase, boolean saveCopy, int months) {

		log.info("Starting cleanup process for {} ", isContactCase ? "contacts" : "index cases");

		PageRequest page = PageRequest.of(0, config.getBatchSize());
		LocalDateTime limit = calculateThresholdDate(months);
		long obsoleteFound = trackedPeople.countObsoletePersons(limit);

		while (obsoleteFound != 0) {

			deletePage(isContactCase, saveCopy, limit, page);

			long obsoleteNotDeleted = trackedPeople.countObsoletePersons(limit);
			if (obsoleteNotDeleted == obsoleteFound) {
				obsoleteFound = 0;
			} else {
				obsoleteFound = obsoleteNotDeleted;
			}
		}
	}

	void deletePage(boolean isContactCase, boolean saveCopy, LocalDateTime limit, PageRequest page) {

		for (TrackedPerson person : trackedPeople.findObsoletePersons(limit, page)) {

			var personId = person.getId();
			var trackedCases = person.getTrackedCases();

			if (trackedCases.isEmpty()) {
				log.info("Skipping person {} (has no tracked case)", personId);
				continue;
			}

			var trackedCase = trackedCases.get(0);
			var proceed = isContactCase ^ trackedCase.isIndexCase();

			if (!proceed) {
				log.info("Skipping person {} - does not fit filter criteria", personId);
				continue;
			}

			var freshDiaryEntries = diaries.countNewerDiaryEntries(person, limit);

			if (freshDiaryEntries > 0) {
				log.info("Person {} has {} newer diary entries - skipping", personId, freshDiaryEntries);
				continue;
			}

			try {
				backupPerson(saveCopy, person);
				log.info("Delete person {}", personId);
				trackedPeople.delete(person);
			} catch (IOException error) {
				log.error("Deletion of person " + personId + " skipped - backup failed!", error);
			}
		}
	}

	void backupPerson(boolean saveCopy, TrackedPerson person) throws IOException {

		if (!saveCopy) {
			return;
		}

		var personId = person.getId().toString();
		String backupName = makeFileName(personId);
		log.info("Writing backup to {}", backupName);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(backupName))) {

			TrackedPersonDto mapped = mapper.map(person, TrackedPersonDto.class);
			String xmlData = xmlWriter.writeValueAsString(mapped);
			writer.write(xmlData);
		}
	}

	String assignExportPath() {

		var testEnvironment = new File("target/classes");
		if (testEnvironment.exists() && testEnvironment.isDirectory() && testEnvironment.canWrite()) {

			var exportPath = new File("target/test-exports/");
			exportPath.mkdirs();
			return exportPath.getAbsolutePath() + "/%s__%d.xml";
		}

		return config.getExportPath();
	}

	LocalDateTime calculateThresholdDate(int months) {
		return LocalDateTime.now().withHour(23).withMinute(59).minusMonths(months);
	}

	String makeFileName(String personId) {
		return String.format(exportPath, personId, executionStarted);
	}
}
