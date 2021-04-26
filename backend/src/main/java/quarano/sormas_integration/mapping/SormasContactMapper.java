package quarano.sormas_integration.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import quarano.sormas_integration.common.SormasReportingUser;
import quarano.sormas_integration.person.SormasContact;
import quarano.sormas_integration.person.SormasContactCase;
import quarano.sormas_integration.person.SormasContactHealthConditions;
import quarano.sormas_integration.person.SormasContactPerson;

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
    @Mapping(target = "caze", expression = "java(getContactCase(contact))")
    @Mapping(target = "reportingUser", expression = "java(getReportingUser(reportingUser))")
    SormasContact map(SormasContactDto contact, String reportingUser);

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
    default SormasContactCase getContactCase(SormasContactDto contact){
        return new SormasContactCase(contact.getSormasUuid());
    }
    default SormasReportingUser getReportingUser(String reportingUser){
        return new SormasReportingUser(reportingUser);
    }
}

