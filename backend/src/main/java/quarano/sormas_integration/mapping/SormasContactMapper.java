package quarano.sormas_integration.mapping;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import quarano.department.TrackedCase;
import quarano.sormas_integration.common.SormasReportingUser;
import quarano.sormas_integration.indexcase.SormasCaseDistrict;
import quarano.sormas_integration.indexcase.SormasCaseRegion;
import quarano.sormas_integration.person.SormasContact;
import quarano.sormas_integration.person.SormasContactCase;
import quarano.sormas_integration.person.SormasContactHealthConditions;
import quarano.sormas_integration.person.SormasContactPerson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Federico Grasso
 */
@Mapper
public interface SormasContactMapper {
    SormasContactMapper INSTANCE = Mappers.getMapper( SormasContactMapper.class );

    @Mapping(target = "uuid", expression = "java(getUUID(contact))")
    @Mapping(target = "person", expression = "java(getContactPerson(contact))")
    @Mapping(target = "reportDateTime", expression = "java(getReportDateTime())")
    @Mapping(target = "healthConditions", expression = "java(getHealthConditions(contact))")
    @Mapping(target = "caze", expression = "java(getContactCase(contact, originContact))")
    @Mapping(target = "reportingUser", expression = "java(getReportingUser(reportingUser))")
    @Mapping(target = "contactClassification", expression = "java(getOriginCase())")
    @Mapping(target = "district", expression = "java(getDistrict(district))")
    @Mapping(target = "region", expression = "java(getRegion(region))")
    @Mapping(target = "lastContactDate", expression = "java(getLastContactDate(lastContactDate))")
    SormasContact map(SormasContactDto contact, String reportingUser, String originContact, String district, String region, LocalDateTime lastContactDate);

    default String getUUID(SormasContactDto contact){
        return contact.getSormasUuid();
    }

    default SormasContactPerson getContactPerson(SormasContactDto contact){
        return new SormasContactPerson(contact.getSormasUuid());
    }

    default String getReportDateTime(){
        return LocalDateTime.now().toString();
    }

    default SormasContactHealthConditions getHealthConditions(SormasContactDto contact){
        return new SormasContactHealthConditions(contact.getSormasUuid());
    }
    default SormasContactCase getContactCase(SormasContactDto contact, String originContact){

        String value = contact.getSormasUuid();

        if(StringUtils.isNotEmpty(originContact)){
            value = originContact;
        }

        return new SormasContactCase(value);
    }
    default SormasReportingUser getReportingUser(String reportingUser){
        return new SormasReportingUser(reportingUser);
    }

    default String getOriginCase(){

        return "UNCONFIRMED";
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

    default String getLastContactDate(LocalDateTime date){
        return date == null ? null : date.toString();
    }

}

