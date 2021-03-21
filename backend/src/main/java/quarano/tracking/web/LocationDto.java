package quarano.tracking.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import quarano.core.PhoneNumber;
import quarano.core.ZipCode;
import quarano.core.validation.Email;
import quarano.core.validation.Strings;
import quarano.tracking.ContactPerson;
import quarano.tracking.Location;

import java.net.URI;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

	@Getter(onMethod = @__(@JsonProperty(access = JsonProperty.Access.READ_ONLY)))
	private Location.LocationIdentifier id;

	private @NotEmpty String name;
	private LocationContactDto contactPerson;
	private @Pattern(regexp = Strings.STREET) String street;
	private @Pattern(regexp = Strings.HOUSE_NUMBER) String houseNumber;
	private @Pattern(regexp = ZipCode.PATTERN) @NotEmpty String zipCode;
	private @Pattern(regexp = Strings.CITY) @NotEmpty String city;
	private String comment;


	public LocationDto validate(Errors errors) {

		return this;
	}

	@JsonProperty("_links")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public Map<String, Object> getLinks() {

		if (id == null) {
			return Collections.emptyMap();
		}

		@SuppressWarnings("null")
		var contactResource = on(LocationController.class).getLocation(null, id);

		return Map.of("self", Map.of("href", fromMethodCall(contactResource).toUriString()));
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LocationContactDto{
		private String contactPersonName;
		private @Pattern(regexp = PhoneNumber.PATTERN) String contactPersonPhone;
		private @Email String contactPersonEmail;
	}
}
