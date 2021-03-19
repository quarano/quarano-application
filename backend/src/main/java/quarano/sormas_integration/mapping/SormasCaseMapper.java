package quarano.sormas_integration.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import quarano.department.TrackedCase;
import quarano.sormas_integration.indexcase.SormasCase;
import quarano.sormas_integration.indexcase.SormasCaseDistrict;
import quarano.sormas_integration.indexcase.SormasCasePerson;
import quarano.tracking.TrackedPerson;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

/**
 * @author Federico Grasso
 */
@Mapper
public interface SormasCaseMapper {
    SormasCaseMapper INSTANCE = Mappers.getMapper( SormasCaseMapper.class );

    @Mapping(target = "lastName", source = "person.lastName")
    @Mapping(target = "firstName", source = "person.firstName")
    @Mapping(target = "sormasUuid", source = "uuid")
    @Mapping(target = "testDate", source = "reportDate")
    @Mapping(target = "quarantineStartDate", source = "quarantineTo")
    @Mapping(target = "quarantineEndDate", source = "quarantineFrom")
    SormasCaseDto map(SormasCase source);

    @Mapping(target = "uuid", expression = "java(getUUID(source))")
    @Mapping(target = "person", expression = "java(getPerson(person))")
    @Mapping(target = "district", expression = "java(getDistrict(source))")
    @Mapping(target = "reportDate", expression = "java(getReportDate(source))")
    @Mapping(target = "quarantineTo", expression = "java(getQuarantineTo(source))")
    @Mapping(target = "quarantineFrom", expression = "java(getQuarantineFrom(source))")
    SormasCase map(TrackedCase source, TrackedPerson person);

    default String getUUID(TrackedCase source){
        return source.getSormasUuid();
    }

    default SormasCasePerson getPerson(TrackedPerson source){
        return new SormasCasePerson(
                source.getSormasUuid(),
                source.getFirstName(),
                source.getLastName()
        );
    }

    default SormasCaseDistrict getDistrict(TrackedCase source){
        return new SormasCaseDistrict(
                UUID.randomUUID().toString(),
                source.getDepartment().getName()
        );
    }

    default Date getQuarantineTo(TrackedCase source){
        return convertToDateViaInstant(source.getQuarantine().getTo());
    }

    default Date getQuarantineFrom(TrackedCase source){
        return convertToDateViaInstant(source.getQuarantine().getFrom());
    }

    default Date getReportDate(TrackedCase source){
        return convertToDateViaInstant(source.getTestResult().getTestDate());
    }

    default Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}
