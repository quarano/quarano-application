package quarano.sormas_integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import quarano.QuaranoUnitTest;
import quarano.QuaranoWebIntegrationTest;
import quarano.core.web.MapperWrapper;
import quarano.sormas_integration.mapping.SormasCaseMapper;
import quarano.sormas_integration.mapping.SormasContactMapper;
import quarano.sormas_integration.mapping.SormasPersonDto;
import quarano.sormas_integration.mapping.SormasPersonMapper;
import quarano.sormas_integration.person.SormasPerson;
import quarano.sormas_integration.person.SormasPersonAddress;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.web.TrackedPersonDto;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@QuaranoUnitTest
@QuaranoWebIntegrationTest
@RequiredArgsConstructor
public class SormasIntegrationMappingUnitTests {

    private final MapperWrapper mapper;

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

    }

    @Test
    void SormasCaseIsConvertedInTrackedCase() {

    }

    @Test
    void TrackedCaseIsConvertedInSormasCase() {

    }
}
