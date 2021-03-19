package quarano.sormas_integration;

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

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Data
@Setter(AccessLevel.NONE)
@RequiredArgsConstructor
@Component
public class SynchContacts {

    private final @NonNull MapperWrapper mapper;
    private final @NonNull TrackedCaseRepository trackedCases;
    private final @NonNull TrackedPersonRepository trackedPersons;
    private final @NonNull DepartmentRepository departments;
    private final @NonNull ContactsSyncReportRepository reports;
    private final @NonNull ContactsSyncBacklogRepository backlog;

    @org.springframework.beans.factory.annotation.Value("${quarano.sormas-integration.sormasurl:}")
    private String sormasUrl;
    @org.springframework.beans.factory.annotation.Value("${quarano.sormas-integration.sormasuser:}")
    private String sormasUser;
    @Value("${quarano.sormas-integration.sormaspass:}")
    private String sormasPassword;
    @Value("${quarano.sormas-synch.master.contacts:}")
    private String master;

    @Scheduled(cron="${quarano.sormas-synch.interval.contacts:-}")
    public void syncContactCases() {
        if(StringUtils.isNotBlank(sormasUrl)) {
            log.info("Contact cases synchronization started");
            log.info("MASTER: " + master);

            // Store starting date of sync
            log.debug("Creating report instance...");
            ContactsSyncReport newReport = new ContactsSyncReport(
                    0,
                    new Date(),
                    System.currentTimeMillis(),
                    ContactsSyncReport.ReportStatus.STARTED
            );
            log.info("Report instance created");

            // Retrieving reports number...
            long reportsCount = reports.count();

            try{
                log.debug("Getting last report...");
                List<ContactsSyncReport> report = reports.getOrderBySyncDateDesc();

                // if reports table is not empty...
                if(!report.isEmpty()){
                    ContactsSyncReport singleReport = report.get(0);

                    // if is already present an active report quit current synchronization
                    if(singleReport.getStatus().equals(IndexSyncReport.ReportStatus.STARTED)){
                        log.warn("Another schedule is already running... ABORTED");
                        return;
                    }
                }

                // Save current synchronization entry
                reports.save(newReport);

                SormasClient sormasClient = new SormasClient(sormasUrl, sormasUser, sormasPassword);

                // if master is sormas...
                if(master.equals("sormas")) {
                }
                // if master is quarano
                else if(master.equals("quarano")) {
                    // if reports table is empty
                    if(reportsCount == 0){
                        // start an initial synchronization
                        initialSynchFromQuarano(sormasClient);
                    }
                    // else
                    else{
                        // start a standard synchronization
                        syncCasesFromQuarano(sormasClient, newReport.getSyncDate());
                    }
                }

                // Save report with success status
                newReport.setSyncTime(System.nanoTime() - newReport.getSyncTime());
                newReport.setStatus(ContactsSyncReport.ReportStatus.SUCCESS);
                reports.save(newReport);
                log.info("Report saved");
            }
            catch(Exception ex){
                // Save report with failed status
                log.error(ex.getMessage(), ex);
                newReport.setSyncTime(System.nanoTime() - newReport.getSyncTime());
                newReport.setStatus(ContactsSyncReport.ReportStatus.FAILED);
                reports.save(newReport);
            }
        }
        else {
            log.warn("Sormas URL not present: NON-INTEGRATED MODE");
        }
    }

    // Initial synchronization from Quarano
    private void initialSynchFromQuarano(SormasClient sormasClient) {

        // Get first tracked persons page
        Page<TrackedPerson> personsPage = trackedPersons.findAll(PageRequest.of(0, 1000));

        int pages = personsPage.getTotalPages();

        // for every page...
        for(int i = 0; i < pages; i++){

            List<TrackedPerson> persons = personsPage.stream().collect(Collectors.toList());

            List<TrackedPerson> indexPersons = new ArrayList<>();

            // for every tracked person...
            for(int j = 0; j < persons.size(); j++){
                TrackedPerson trackedPerson = persons.get(j);

                // Search Tracked Case related to person
                Optional<TrackedCase> trackedCaseQuery = trackedCases.findByTrackedPerson(trackedPerson);

                if(trackedCaseQuery.isPresent()){
                    TrackedCase trackedCase = trackedCaseQuery.get();
                    // if case is of type CONTACT
                    if(trackedCase.isContactCase()){
                        // synchronize person
                        indexPersons.add(trackedPerson);
                    }
                }
            }

            synchronizePersons(sormasClient, indexPersons);
            synchronizeContacts(sormasClient, indexPersons);

            if(i + 1 < pages){
                personsPage = trackedPersons.findAll(PageRequest.of(i + 1, 1000));
            }
        }
    }

    private void syncCasesFromQuarano(SormasClient sormasClient, Date synchDate) {
        // Determine IDs to sync
        ArrayList<UUID> entities = backlog.findBySyncDate(synchDate);

        List<TrackedPerson> indexPersons = new ArrayList<>();

        // for every tracked person...
        for(int i = 0; i < entities.size(); i++){
            UUID entity = entities.get(i);

            // Fetch person from Database
            Optional<TrackedPerson> trackedPersonQuery = trackedPersons.findById(TrackedPerson.TrackedPersonIdentifier.of(entity));

            if(trackedPersonQuery.isPresent()){

                TrackedPerson trackedPerson = trackedPersonQuery.get();

                // Search Tracked Case related to person
                Optional<TrackedCase> trackedCaseQuery = trackedCases.findByTrackedPerson(trackedPerson);

                if(trackedCaseQuery.isPresent()){

                    TrackedCase trackedCase = trackedCaseQuery.get();

                    // if case is of type CONTACT
                    if(trackedCase.isContactCase()){
                        indexPersons.add(trackedPerson);
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
    }

    private List<TrackedPerson> synchronizePersons(SormasClient sormasClient, List<TrackedPerson> persons){
        List<SormasPerson> sormasPersons = new ArrayList<>();
        List<TrackedPerson> successPersons = new ArrayList<>();

        persons.forEach(person -> {
            if(StringUtils.isBlank(person.getSormasUuid())){
                // Create Sormas ID
                person.setSormasUuid(UUID.randomUUID().toString());
            }

            // Map TrackedPerson to SormasPerson
            SormasPersonDto personDto = mapper.map(person, SormasPersonDto.class);
            SormasPerson sormasPerson = SormasPersonMapper.INSTANCE.map(personDto);

            sormasPersons.add(sormasPerson);
        });

        // Push to Sormas
        String[] response = sormasClient.postPersons(sormasPersons).block();

        for(int i = 0; i < response.length; i++){
            if(response[i].equals("OK")){
                trackedPersons.save(persons.get(i));
                successPersons.add(persons.get(i));
            }
        }

        return successPersons;
    }

    private void synchronizeContacts(SormasClient sormasClient, List<TrackedPerson> persons){
        List<SormasContact> sormasContacts = new ArrayList<>();

        persons.forEach(person -> {
            // Map TrackedPerson to SormasContact
            SormasContactDto contactDto = mapper.map(person, SormasContactDto.class);
            SormasContact sormasContact = SormasContactMapper.INSTANCE.map(contactDto);

            sormasContacts.add(sormasContact);
        });

        // Push to Sormas
        String[] response = sormasClient.postContacts(sormasContacts).block();
    }
}