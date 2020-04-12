package quarano.tracking.web;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import quarano.tracking.ContactPerson.ContactPersonIdentifier;
import quarano.tracking.ContactPerson.TypeOfContract;
import quarano.tracking.ContactPerson.TypeOfProtection;
import quarano.tracking.EmailAddress;
import quarano.tracking.PhoneNumber;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Data
public class ContactPersonDto {

	@Setter(onMethod = @__(@JsonIgnore)) //
	@Getter(onMethod = @__(@JsonProperty)) //
	@JsonSerialize(using = ToStringSerializer.class) //
	ContactPersonIdentifier id;

	@NotEmpty String lastName, firstName;
	@Pattern(regexp = PhoneNumber.PATTERN) String phone;
	@Pattern(regexp = EmailAddress.PATTERN) String email;
	TypeOfContract typeOfContract;
	TypeOfProtection typeOfProtection;
}
