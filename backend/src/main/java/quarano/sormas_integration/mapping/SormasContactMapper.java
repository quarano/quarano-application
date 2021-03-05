package quarano.sormas_integration.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import quarano.core.EmailAddress;
import quarano.sormas_integration.person.SormasContact;
import quarano.sormas_integration.person.SormasContactPerson;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Federico Grasso
 */
@Mapper
public interface SormasContactMapper {
    SormasContactMapper INSTANCE = Mappers.getMapper( SormasContactMapper.class );

    @Mapping(target = "uuid", expression = "java(createNewUUID())")
    @Mapping(target = "person", expression = "java(getContactPerson(contact))")
    SormasContact map(SormasContactDto contact);

    default String createNewUUID(){
        return UUID.randomUUID().toString();
    }

    default SormasContactPerson getContactPerson(SormasContactDto contact){
        return new SormasContactPerson(contact.getSormasUuid(), contact.getFirstName(), contact.getLastName());
    }
}

