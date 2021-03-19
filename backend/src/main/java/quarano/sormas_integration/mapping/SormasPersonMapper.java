package quarano.sormas_integration.mapping;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import quarano.core.Address;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.core.ZipCode;
import quarano.sormas_integration.person.SormasPerson;
import quarano.sormas_integration.person.SormasPersonAddress;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Federico Grasso
 */
@Mapper
public interface SormasPersonMapper {
    SormasPersonMapper INSTANCE = Mappers.getMapper( SormasPersonMapper.class );

    @Mapping(target = "sormasUuid", source = "uuid")
    @Mapping(target = "emailAddress", expression = "java(getEmailAddress(source.getEmailAddress()))")
    @Mapping(target = "phoneNumber", expression = "java(getPhoneNumber(source.getPhone()))")
    @Mapping(target = "mobilePhoneNumber", expression = "java(getPhoneNumber(source.getPhone()))")
    @Mapping(target = "dateOfBirth", expression = "java(getDateOfBirth(source))")
    @Mapping(target = "address", expression = "java(getAddress(source))")
    SormasPersonDto map(SormasPerson source);

    @Mapping(target = "uuid", source = "sormasUuid")
    @Mapping(target = "emailAddress", expression = "java(getStringEmailAddress(source.getEmailAddress()))")
    @Mapping(target = "phone", expression = "java(getStringPhoneNumber(source.getPhoneNumber()))")
    @Mapping(target = "address", expression = "java(getInversAddress(source.getAddress()))")
    @Mapping(target = "birthdateDD", expression = "java(getDay(source.getDateOfBirth()))")
    @Mapping(target = "birthdateMM", expression = "java(getMonth(source.getDateOfBirth()))")
    @Mapping(target = "birthdateYYYY", expression = "java(getYear(source.getDateOfBirth()))")
    SormasPerson map(SormasPersonDto source);

    default String getStringEmailAddress(EmailAddress emailAddress){
        if(emailAddress != null){
            return emailAddress.toString();
        }
        return null;
    }

    default String getStringPhoneNumber(PhoneNumber phoneNumber){
        if(phoneNumber != null){
            return phoneNumber.toString();
        }
        return null;
    }

    default SormasPersonAddress getInversAddress(Address address){
        if(address != null){
            return new SormasPersonAddress(
                    address.getCity(),
                    address.getZipCode() == null ? null : address.getZipCode().toString(),
                    address.getStreet(),
                    address.getHouseNumber().toString(),
                    UUID.randomUUID().toString()
            );
        }

        return null;
    }

    default Integer getDay(LocalDate date){
        if(date != null){
            return date.getDayOfMonth();
        }

        return null;
    }

    default Integer getMonth(LocalDate date){
        if(date != null){
            return date.getMonthValue();
        }

        return null;
    }

    default Integer getYear(LocalDate date){
        if(date != null){
            return date.getYear();
        }

        return null;
    }

    default EmailAddress getEmailAddress(String email) {
        try{
            if(StringUtils.isNotBlank(email)){
                return EmailAddress.of(email);
            }
            return null;
        }
        catch (IllegalArgumentException ex){
            return null;
        }
    }

    default PhoneNumber getPhoneNumber(String phone) {
        try{
            if(StringUtils.isNotBlank(phone)){
                return PhoneNumber.of(phone);
            }
            return null;
        }
        catch (IllegalArgumentException ex){
            return null;
        }
    }

    default LocalDate getDateOfBirth(SormasPerson source){
        if(
                source.getBirthdateYYYY() != null &&
                        source.getBirthdateMM() != null &&
                        source.getBirthdateDD() != null
        ) {
            return LocalDate.of(
                    source.getBirthdateYYYY(),
                    source.getBirthdateMM(),
                    source.getBirthdateDD()
            );
        }
        return null;
    }

    default Address getAddress(SormasPerson source){
        try{
            if(StringUtils.isNoneBlank(
                    source.getAddress().getStreet(),
                    source.getAddress().getCity(),
                    source.getAddress().getPostalCode()
            )
            ) {
                return new Address(
                        source.getAddress().getStreet(),
                        Address.HouseNumber.NONE,
                        source.getAddress().getCity(),
                        ZipCode.of(source.getAddress().getPostalCode())
                );
            }
            return null;
        }
        catch (IllegalArgumentException ex){
            return null;
        }
    }
}
