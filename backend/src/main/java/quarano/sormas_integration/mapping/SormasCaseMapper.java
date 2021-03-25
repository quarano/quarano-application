package quarano.sormas_integration.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import quarano.department.TrackedCase;
import quarano.sormas_integration.common.SormasReportingUser;
import quarano.sormas_integration.indexcase.*;
import quarano.tracking.TrackedPerson;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Mapping(target = "district", expression = "java(getDistrict(district))")
    @Mapping(target = "region", expression = "java(getRegion(district))")
    @Mapping(target = "healthFacility", expression = "java(getHealthFacility(district))")
    @Mapping(target = "reportDate", expression = "java(getReportDate(source))")
    @Mapping(target = "quarantineTo", expression = "java(getQuarantineTo(source))")
    @Mapping(target = "quarantineFrom", expression = "java(getQuarantineFrom(source))")
    @Mapping(target = "reportingUser", expression = "java(getReportingUser(reportingUser))")
    SormasCase map(TrackedCase source, TrackedPerson person, String reportingUser, String district, String region, String healthFacility);

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

    default SormasCaseDistrict getDistrict(String district){
        return new SormasCaseDistrict(
                district
        );
    }

    default SormasCaseRegion getRegion(String region){
        return new SormasCaseRegion(
                region
        );
    }

    default SormasCaseHealthFacility getHealthFacility(String healthFacility){
        return new SormasCaseHealthFacility(
                healthFacility
        );
    }

    default LocalDateTime getQuarantineTo(TrackedCase source){
        return convertToDateViaInstant(source.getQuarantine().getTo());
    }

    default LocalDateTime getQuarantineFrom(TrackedCase source){
        return convertToDateViaInstant(source.getQuarantine().getFrom());
    }

    default LocalDateTime getReportDate(TrackedCase source){
        return convertToDateViaInstant(source.getTestResult().getTestDate());
    }

    default LocalDateTime convertToDateViaInstant(LocalDate dateToConvert) {
        return LocalDateTime.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    default SormasReportingUser getReportingUser(String reportingUser){
        return new SormasReportingUser(reportingUser);
    }
}
