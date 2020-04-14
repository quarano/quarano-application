package quarano.tracking.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.Data;
import lombok.Getter;
import quarano.tracking.ContactPerson.ContactPersonIdentifier;
import quarano.tracking.EmailAddress;
import quarano.tracking.PhoneNumber;
import quarano.tracking.ZipCode;

import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
class ContactPersonDto {

	@Getter(onMethod = @__(@JsonProperty(access = JsonProperty.Access.READ_ONLY))) //
	private ContactPersonIdentifier id;

	private @NotEmpty String lastName, firstName, street;
	private String houseNumber;
	private @NotEmpty String city;
	private @NotEmpty @Pattern(regexp = ZipCode.PATTERN) String zipCode;
	private @Pattern(regexp = PhoneNumber.PATTERN) String phone;
	private @Pattern(regexp = PhoneNumber.PATTERN) String mobilePhone;
	private @Pattern(regexp = EmailAddress.PATTERN) String email;
	private String remark;
	private String identificationHint;
	private Boolean isHealthStaff;
	private Boolean isSenior;
	private Boolean hasPreExistingConditions;

	@JsonProperty("_links")
	public Map<String, Object> getLinks() {

		var contactResource = on(ContactPersonController.class).getContact(null, id);

		return Map.of("self", Map.of("self", fromMethodCall(contactResource).toUriString()));
	}
}
