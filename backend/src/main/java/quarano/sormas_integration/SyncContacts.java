package quarano.sormas_integration;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import quarano.account.DepartmentRepository;
import quarano.core.web.MapperWrapper;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;
import quarano.sormas_integration.backlog.ContactsSyncBacklogRepository;
import quarano.sormas_integration.mapping.SormasContactDto;
import quarano.sormas_integration.mapping.SormasContactMapper;
import quarano.sormas_integration.mapping.SormasPersonDto;
import quarano.sormas_integration.mapping.SormasPersonMapper;
import quarano.sormas_integration.person.SormasContact;
import quarano.sormas_integration.person.SormasPerson;
import quarano.sormas_integration.report.ContactsSyncReport;
import quarano.sormas_integration.report.ContactsSyncReportRepository;
import quarano.sormas_integration.report.IndexSyncReport;
import quarano.tracking.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Data
@Setter(AccessLevel.NONE)
@RequiredArgsConstructor
@Component
public class SyncContacts {

    private final @NonNull MapperWrapper mapper;
    private final @NonNull TrackedCaseRepository trackedCases;
    private final @NonNull TrackedPersonRepository trackedPersons;
    private final @NonNull DepartmentRepository departments;
    private final @NonNull ContactsSyncReportRepository reports;
    private final @NonNull ContactsSyncBacklogRepository backlog;
    private final @NonNull SormasIntegrationProperties properties;

    @Scheduled(cron="${quarano.sormas-synch.interval.contacts:-}")
    public void syncContactCases() {
        if(StringUtils.isNotBlank(properties.getSormasurl())) {
            log.info("Contact cases synchronization started [V1]");
            log.info("MASTER: " + properties.getMaster().getContacts());

            long executionTimeSpan = System.currentTimeMillis();

            ContactsSyncReport.ReportStatus executionStatus = ContactsSyncReport.ReportStatus.STARTED;

            // Store starting date of sync
            log.debug("Creating report instance...");
            ContactsSyncReport newReport = new ContactsSyncReport(
                    UUID.randomUUID(),
                    String.valueOf(0),
                    LocalDateTime.now(),
                    String.valueOf(0),
                    String.valueOf(ContactsSyncReport.ReportStatus.STARTED)
            );
            log.debug("Report instance created");

            // Retrieving reports number...
            long reportsCount = reports.count();

            try{
                log.debug("Getting last report...");
                List<ContactsSyncReport> report = reports.getOrderBySyncDateDesc();

                // if reports table is not empty...
                if(!report.isEmpty()){
                    ContactsSyncReport singleReport = report.get(0);

                    // if is already present an active report quit current synchronization
                    if(singleReport.getStatus().equals(String.valueOf(ContactsSyncReport.ReportStatus.STARTED))){
                        executionStatus = ContactsSyncReport.ReportStatus.FAILED;
                        log.warn("Another schedule is already running... ABORTED");
                        return;
                    }
                }

                // Save current synchronization entry
                reports.save(newReport);

                SormasClient sormasClient = new SormasClient(properties.getSormasurl(), properties.getSormasuser(), properties.getSormaspass());

                // if master is sormas...
                if(properties.getMaster().getContacts().equals("sormas")) {
                }
                // if master is quarano
                else if(properties.getMaster().getContacts().equals("quarano")) {
                    // if reports table is empty
                    if(reportsCount == 0){
                        // start an initial synchronization
                        initialSynchFromQuarano(sormasClient, newReport);
                    }
                    // else
                    else{
                        // start a standard synchronization
                        syncCasesFromQuarano(sormasClient, newReport.getSyncDate(), newReport);
                    }
                }

                // Save report with success status
                executionStatus = ContactsSyncReport.ReportStatus.SUCCESS;
            }
            catch(Exception ex){
                log.error(ex.getMessage(), ex);
                // Save report with failed status
                executionStatus = ContactsSyncReport.ReportStatus.FAILED;
            }
            finally {
                updateReport(newReport, executionTimeSpan, executionStatus);
            }
        }
        else {
            log.warn("Sormas URL not present: NON-INTEGRATED MODE");
        }
    }

    // Initial synchronization from Quarano
    private void initialSynchFromQuarano(SormasClient sormasClient, ContactsSyncReport newReport) {

        // Get first tracked persons page
        //Page<TrackedPerson> personsPage = trackedPersons.findAllWithEncounters(PageRequest.of(0, 1000));

        Page<TrackedPerson> personsPage = trackedPersons.findAll(PageRequest.of(0, 1000));

        int pages = personsPage.getTotalPages();

        Integer newReportPersonsCount = 0;

        // for every page...
        for(int i = 0; i < pages; i++){

            List<TrackedPerson> persons = personsPage.stream().collect(Collectors.toList());

            List<Tuple2<TrackedPerson, TrackedCase>> indexPersons = new ArrayList<>();

            // for every tracked person...
            for(int j = 0; j < persons.size(); j++){
                TrackedPerson trackedPerson = persons.get(j);

                newReportPersonsCount++;

                // Search Tracked Case related to person
                Optional<TrackedCase> trackedCaseQuery = trackedCases.findByTrackedPersonWithOrigin(trackedPerson);

                if(trackedCaseQuery.isPresent()){
                    TrackedCase trackedCase = trackedCaseQuery.get();
                    // if case is of type CONTACT
                    if(trackedCase.isContactCase()){

                        List<TrackedCase> originCases = trackedCase
                                .getOriginCases();

                        TrackedCase originCase = null;
                        if(!originCases.isEmpty()){
                            originCase = originCases.get(0);
                        }

                        // synchronize person
                        indexPersons.add(Tuple.of(trackedPerson, originCase));
                    }
                }
            }

            synchronizePersons(sormasClient, indexPersons);
            synchronizeContacts(sormasClient, indexPersons);

            if(i + 1 < pages){
                personsPage = trackedPersons.findAll(PageRequest.of(i + 1, 1000));
            }
        }

        newReport.setPersonsNumber(newReportPersonsCount.toString());
    }

    private void syncCasesFromQuarano(SormasClient sormasClient, LocalDateTime synchDate, ContactsSyncReport newReport) {
        // Determine IDs to sync
        List<UUID> entities = backlog.findBySyncDate(synchDate);

        List<Tuple2<TrackedPerson, TrackedCase>> indexPersons = new ArrayList<>();

        Integer newReportPersonsCount = 0;

        // for every tracked person...
        for(int i = 0; i < entities.size(); i++){
            UUID entity = entities.get(i);

            // Fetch person from Database
            Optional<TrackedPerson> trackedPersonQuery = trackedPersons.findByIdWithEncounters(TrackedPerson.TrackedPersonIdentifier.of(entity));

            if(trackedPersonQuery.isPresent()){

                newReport.setPersonsNumber(newReport.getPersonsNumber() + 1);
                TrackedPerson trackedPerson = trackedPersonQuery.get();

                newReportPersonsCount++;

                // Search Tracked Case related to person
                Optional<TrackedCase> trackedCaseQuery = trackedCases.findByTrackedPersonWithOrigin(trackedPerson);

                if(trackedCaseQuery.isPresent()){

                    TrackedCase trackedCase = trackedCaseQuery.get();

                    // if case is of type CONTACT
                    if(trackedCase.isContactCase()){
                        List<TrackedCase> originCases = trackedCase
                                .getOriginCases();

                        TrackedCase originCase = null;

                        if(!originCases.isEmpty()){
                            originCase = originCases.get(0);
                        }

                        indexPersons.add(Tuple.of(trackedPerson, originCase));
                    }
                }
            }
        }

        // synchronize persons
        List<TrackedPerson> successPersons = synchronizePersons(sormasClient, indexPersons);
        // synchronize cases
        synchronizeContacts(sormasClient, indexPersons);

        successPersons.forEach(person -> {
            // Delete from backlog
            backlog.deleteAfterSynchronization(UUID.fromString(person.getId().toString()), synchDate);
        });

        newReport.setPersonsNumber(newReportPersonsCount.toString());
    }

    private List<TrackedPerson> synchronizePersons(SormasClient sormasClient, List<Tuple2<TrackedPerson, TrackedCase>> persons){
        List<SormasPerson> sormasPersons = new ArrayList<>();
        List<TrackedPerson> successPersons = new ArrayList<>();

        persons.forEach(person -> {
            if(StringUtils.isBlank(person._1.getSormasUuid())){
                // Create Sormas ID
                person._1.setSormasUuid(UUID.randomUUID().toString());
            }

            // Map TrackedPerson to SormasPerson
            SormasPersonDto personDto = mapper.map(person._1, SormasPersonDto.class);
            SormasPerson sormasPerson = SormasPersonMapper.INSTANCE.map(personDto);

            sormasPersons.add(sormasPerson);
        });

        // Push to Sormas
        String[] response = sormasClient.postPersons(sormasPersons).block();

        for(int i = 0; i < response.length; i++){
            log.debug("Person number" + i + ": " + response[i]);
            if(response[i].equals("OK")){
                trackedPersons.save(persons.get(i)._1);
                successPersons.add(persons.get(i)._1);
            }
        }

        return successPersons;
    }

    private void synchronizeContacts(SormasClient sormasClient, List<Tuple2<TrackedPerson, TrackedCase>> persons){
        List<SormasContact> sormasContacts = new ArrayList<>();

        persons.forEach(person -> {

            LocalDateTime lastContactDate = null;

//            var lastContactDateQuery = person._1.getEncounters()
//                    .stream()
//                    .sorted(new Comparator<Encounter>() {
//                        public int compare(Encounter o1, Encounter o2) {
//                            return o1.getDate().compareTo(o2.getDate());
//                        }
//                    })
//                    .findFirst();
//
//            if(lastContactDateQuery.isPresent()){
//                lastContactDate = lastContactDateQuery.get().getDate();
//            }

            // Map TrackedPerson to SormasContact
            SormasContactDto contactDto = mapper.map(person._1, SormasContactDto.class);
            SormasContact sormasContact = SormasContactMapper.INSTANCE.map(
                    contactDto,
                    properties.getReportingUser(),
                    person._2.getSormasUuid(),
                    properties.getDistrict(),
                    properties.getRegion(),
                    lastContactDate
            );

            sormasContacts.add(sormasContact);
        });

        // Push to Sormas
        String[] response = sormasClient.postContacts(sormasContacts).block();

        for(int i = 0; i < response.length; i++){
            log.debug("Contact number" + i + ": " + response[i]);
        }
    }

    private void updateReport(ContactsSyncReport report, long executionTimeSpan, ContactsSyncReport.ReportStatus status){
        report.setSyncTime(String.valueOf((System.currentTimeMillis() - executionTimeSpan) / 1000));
        report.setStatus(String.valueOf(status));
        reports.save(report);
        log.info("Report saved with status " + status);
    }

}