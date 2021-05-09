package quarano.sormas_integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import quarano.QuaranoIntegrationTest;
import quarano.QuaranoUnitTest;
import quarano.QuaranoWebIntegrationTest;
import quarano.account.Department;
import quarano.account.DepartmentDataInitializer;
import quarano.core.web.MapperWrapper;
import quarano.department.CaseType;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseDataInitializer;
import quarano.department.TrackedCaseRepository;
import quarano.sormas_integration.common.SormasReportingUser;
import quarano.sormas_integration.indexcase.*;
import quarano.sormas_integration.mapping.*;
import quarano.sormas_integration.person.*;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.web.TrackedPersonDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@QuaranoUnitTest
@QuaranoWebIntegrationTest
@QuaranoIntegrationTest
@RequiredArgsConstructor
public class SormasIntegrationMappingUnitTests {

    private final MapperWrapper mapper;
    private final TrackedCaseRepository cases;

    SormasCaseMapper caseMapper;
    SormasPersonMapper personMapper;
    SormasContactMapper contactMapper;

    @BeforeEach
    void setup() {
        caseMapper = Mappers.getMapper(SormasCaseMapper.class);
        personMapper = Mappers.getMapper(SormasPersonMapper.class);
        contactMapper = Mappers.getMapper(SormasContactMapper.class);
    }

    @Test
    void SormasPersonIsConvertedInTrackedPerson() {

        SormasPerson sormasPerson = new SormasPerson(
                "ABBA-FGGF-VDDV-GHGL",
                "Albert",
                "Einstein",
                "alberteinstein@mockmail.com",
                "123456789",
                new SormasPersonAddress(
                        "London",
                        "95014",
                        "St Mary Street",
                        "45",
                        "XDDX-FFGG-HHLL-MNNM"
                ),
                19,
                9,
                1995
        );

        TrackedPersonDto personDto = mapper.map(personMapper.map(sormasPerson), TrackedPersonDto.class);
        TrackedPerson trackedPerson = mapper.map(personDto, TrackedPerson.class);

        assertThat(trackedPerson.getSormasUuid()).isEqualTo(sormasPerson.getUuid());
        assertThat(trackedPerson.getFirstName()).isEqualTo(sormasPerson.getFirstName());
        assertThat(trackedPerson.getLastName()).isEqualTo(sormasPerson.getLastName());
        assertThat(trackedPerson.getEmailAddress().toString()).isEqualTo(sormasPerson.getEmailAddress());
        assertThat(trackedPerson.getPhoneNumber().toString()).isEqualTo(sormasPerson.getPhone());
        assertThat(trackedPerson.getDateOfBirth()).isEqualTo(LocalDate.of(
                sormasPerson.getBirthdateYYYY(),
                sormasPerson.getBirthdateMM(),
                sormasPerson.getBirthdateDD()
        ));
        assertThat(trackedPerson.getAddress().getStreet()).isEqualTo(sormasPerson.getAddress().getStreet());
        assertThat(trackedPerson.getAddress().getCity()).isEqualTo(sormasPerson.getAddress().getCity());
        assertThat(trackedPerson.getAddress().getZipCode().toString()).isEqualTo(sormasPerson.getAddress().getPostalCode());
        assertThat(trackedPerson.getAddress().getHouseNumber().toString()).isEqualTo(sormasPerson.getAddress().getHouseNumber());
    }

    @Test
    void TrackedPersonIsConvertedInSormasPerson() {

        TrackedPerson trackedPerson = TrackedPersonDataInitializer.createTanja();

        SormasPersonDto personDto = mapper.map(trackedPerson, SormasPersonDto.class);
        SormasPerson sormasPerson = personMapper.map(personDto);

        assertThat(sormasPerson.getFirstName()).isEqualTo(trackedPerson.getFirstName());
        assertThat(sormasPerson.getLastName()).isEqualTo(trackedPerson.getLastName());
        assertThat(sormasPerson.getPhone()).isEqualTo(trackedPerson.getPhoneNumber().toString());
        assertThat(sormasPerson.getEmailAddress()).isEqualTo(trackedPerson.getEmailAddress().toString());
        assertThat(sormasPerson.getUuid()).isEqualTo(trackedPerson.getSormasUuid());

        assertThat(sormasPerson.getBirthdateDD()).isEqualTo(trackedPerson.getDateOfBirth().getDayOfMonth());
        assertThat(sormasPerson.getBirthdateMM()).isEqualTo(trackedPerson.getDateOfBirth().getMonthValue());
        assertThat(sormasPerson.getBirthdateYYYY()).isEqualTo(trackedPerson.getDateOfBirth().getYear());

        assertThat(sormasPerson.getAddress().getCity()).isEqualTo(trackedPerson.getAddress().getCity());
        assertThat(sormasPerson.getAddress().getStreet()).isEqualTo(trackedPerson.getAddress().getStreet());
        assertThat(sormasPerson.getAddress().getPostalCode()).isEqualTo(trackedPerson.getAddress().getZipCode().toString());
        assertThat(sormasPerson.getAddress().getHouseNumber()).isEqualTo(trackedPerson.getAddress().getHouseNumber().toString());
    }
    @Test
    void TrackedPersonIsConvertedInSormasContact() {

        TrackedPerson trackedPerson = TrackedPersonDataInitializer.createTanja();

        SormasContactDto contactDto = mapper.map(trackedPerson, SormasContactDto.class);
        SormasContact sormasContact = contactMapper.map(contactDto, "XXXX-YYYY-WWWW", "XXXX-YYYY-WWWW");

        assertThat(sormasContact.getUuid()).isEqualTo(trackedPerson.getSormasUuid());
        assertThat(sormasContact.getPerson().getUuid()).isEqualTo(trackedPerson.getSormasUuid());
        assertThat(sormasContact.getDisease()).isEqualTo("CORONAVIRUS");
        assertThat(sormasContact.getReportingUser().getUuid()).isEqualTo("XXXX-YYYY-WWWW");
        assertThat(sormasContact.getCaze().getUuid()).isEqualTo("XXXX-YYYY-WWWW");
        assertThat(sormasContact.getHealthConditions().getUuid()).isEqualTo(trackedPerson.getSormasUuid());
    }

    @Test
    void SormasCaseIsConvertedInTrackedCase() {

        TrackedPerson trackedPerson = TrackedPersonDataInitializer.createTanja();
        Department department = new Department(DepartmentDataInitializer.DEPARTMENT_ID_DEP1.toString());

        SormasCase sormasCase = new SormasCase(
                UUID.randomUUID().toString(),
                "1620139921000",
                "1620139921000",
                "1620139921000",
                new SormasCasePerson(
                        trackedPerson.getSormasUuid(),
                        trackedPerson.getFirstName(),
                        trackedPerson.getLastName()
                ),
                new SormasCaseDistrict(
                        department.getName()
                ),
                new SormasCaseRegion(
                        UUID.randomUUID().toString()
                ),
                "CORONAVIRUS",
                "PROBABLE",
                "PENDING",
                "XXXX-MMMM-RRRR",
                new SormasReportingUser("XXXX-MMMM-RRRR"),
                new SormasCaseHealthFacility("XXXX-MMMM-RRRR"),
                "POINT_OF_ENTRY",
                new SormasCaseOrigin("XXXX-MMMM-RRRR"),
                "XXX-MMMM-RRRR"
        );

        SormasCaseDto caseDto = mapper.map(caseMapper.map(sormasCase), SormasCaseDto.class);
        TrackedCase trackedCase = mapper.map(caseDto, new TrackedCase(trackedPerson, CaseType.INDEX, department));

        assertThat(trackedCase.getSormasUuid()).isEqualTo(sormasCase.getUuid());
        assertThat(trackedCase.getTrackedPerson().getFirstName()).isEqualTo(trackedPerson.getFirstName());
        assertThat(trackedCase.getTrackedPerson().getLastName()).isEqualTo(trackedPerson.getLastName());
        assertThat(trackedCase.getDepartment().getName()).isEqualTo(department.getName());
    }

    @Test
    void TrackedCaseIsConvertedInSormasCase() {

        TrackedPerson trackedPerson = TrackedPersonDataInitializer.createTanja();
        TrackedCase trackedCase = cases.findById(TrackedCaseDataInitializer.TRACKED_CASE_TANJA).get();

        SormasCase sormasCase = caseMapper.map(
                trackedCase,
                trackedPerson,
                "XXXX-MMMM-YYYY-EEEE",
                "XXXX-MMMM-YYYY-EEEE",
                "XXXX-MMMM-YYYY-EEEE",
                "XXXX-MMMM-YYYY-EEEE"
        );

        assertThat(sormasCase.getUuid()).isEqualTo(trackedCase.getSormasUuid());
        assertThat(sormasCase.getDisease()).isEqualTo("CORONAVIRUS");
        assertThat(sormasCase.getFacilityType()).isEqualTo("LABORATORY");
        assertThat(sormasCase.getCaseClassification()).isEqualTo("PROBABLE");
        assertThat(sormasCase.getInvestigationStatus()).isEqualTo("PENDING");
        assertThat(sormasCase.getQuarantineFrom()).isNull();
        assertThat(sormasCase.getQuarantineTo()).isNull();
        assertThat(sormasCase.getHealthFacility().getUuid()).isEqualTo("XXXX-MMMM-YYYY-EEEE");
        assertThat(sormasCase.getRegion().getUuid()).isEqualTo("XXXX-MMMM-YYYY-EEEE");
        assertThat(sormasCase.getDistrict().getUuid()).isEqualTo("XXXX-MMMM-YYYY-EEEE");
        assertThat(sormasCase.getReportingUser().getUuid()).isEqualTo("XXXX-MMMM-YYYY-EEEE");

        assertThat(sormasCase.getPerson().getFirstName()).isEqualTo(trackedPerson.getFirstName());
        assertThat(sormasCase.getPerson().getLastName()).isEqualTo(trackedPerson.getLastName());
        assertThat(sormasCase.getPerson().getUuid()).isEqualTo(trackedPerson.getSormasUuid());
    }
}
