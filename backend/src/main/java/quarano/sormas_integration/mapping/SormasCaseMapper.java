package quarano.sormas_integration.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import quarano.department.TrackedCase;
import quarano.sormas_integration.common.SormasReportingUser;
import quarano.sormas_integration.indexcase.*;
import quarano.tracking.TrackedPerson;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
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
    @Mapping(target = "testDate", expression = "java(getDateFromInstant(source.getReportDate()))")
    @Mapping(target = "quarantineStartDate", expression = "java(getDateFromInstant(source.getQuarantineFrom()))")
    @Mapping(target = "quarantineEndDate", expression = "java(getDateFromInstant(source.getQuarantineTo()))")
    SormasCaseDto map(SormasCase source);

    @Mapping(target = "uuid", expression = "java(getUUID(source))")
    @Mapping(target = "person", expression = "java(getPerson(person))")
    @Mapping(target = "district", expression = "java(getDistrict(district))")
    @Mapping(target = "region", expression = "java(getRegion(region))")
    @Mapping(target = "healthFacility", expression = "java(getHealthFacility(healthFacility))")
    @Mapping(target = "reportDate", expression = "java(getReportDate(source))")
    @Mapping(target = "quarantineTo", expression = "java(getQuarantineTo(source))")
    @Mapping(target = "quarantineFrom", expression = "java(getQuarantineFrom(source))")
    @Mapping(target = "reportingUser", expression = "java(getReportingUser(reportingUser))")
    @Mapping(target = "caseOrigin", expression = "java(getOriginCase(source))")
    @Mapping(target = "pointOfEntry", expression = "java(getPointOfEntry(source))")
    @Mapping(target = "externalID", expression = "java(getExternalID(source))")
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

    default String getQuarantineTo(TrackedCase source){
        if(source.getQuarantine() != null){
            return convertToDateViaInstant(source.getQuarantine().getTo()).toString();
        }

        return null;
    }

    default String getQuarantineFrom(TrackedCase source){
        if(source.getQuarantine() != null){
            return convertToDateViaInstant(source.getQuarantine().getFrom()).toString();
        }

        return null;
    }

    default String getReportDate(TrackedCase source){

        if(source.getTestResult() != null){
            return convertToDateViaInstant(source.getTestResult().getTestDate()).toString();
        }

        return null;
    }

    default LocalDate convertToDateViaInstant(LocalDate dateToConvert) {
        return dateToConvert;
    }

    default LocalDate getDateFromInstant(String date) {

        if(date != null){
            return Instant.ofEpochMilli(Long.parseLong(date)).atZone(ZoneId.systemDefault()).toLocalDate();
        }

        return null;
    }

    default SormasReportingUser getReportingUser(String reportingUser){

        return new SormasReportingUser(reportingUser);
    }

    default String getOriginCase(TrackedCase source){

        return "POINT_OF_ENTRY";
    }

    default SormasCaseOrigin getPointOfEntry(TrackedCase source){

        List<TrackedCase> originCases = source.getOriginCases();

        return new SormasCaseOrigin(
                originCases.isEmpty() ? null : originCases.get(0).getId().toString()
        );
    }
    default String getExternalID(TrackedCase source){

        return source.getId().toString();
    }
}
