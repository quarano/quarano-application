package quarano.occasion.web;

import io.vavr.collection.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import quarano.core.PhoneNumber;
import quarano.core.validation.Email;
import quarano.core.validation.Strings;
import quarano.core.validation.Textual;
import quarano.core.web.MapperWrapper;
import quarano.occasion.Occasion;
import quarano.occasion.OccasionCode;
import quarano.occasion.VisitorGroup;
import quarano.tracking.ZipCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Component
class OccasionRepresentions {

	private final MapperWrapper mapperWrapper;
	private final ProjectionFactory projections;

	VisitorGroup from(VisitorGroupDto visitorTransmissionDto) {
		return mapperWrapper.map(visitorTransmissionDto, VisitorGroup.class);
	}

	OccasionSummary toSummary(Occasion occasion) {
		return projections.createProjection(OccasionSummary.class, occasion);
	}

	interface OccasionSummary {

		String getTitle();

		LocalDateTime getStart();

		LocalDateTime getEnd();

		String getOccasionCode();

		List<VisitorGroupSummary> getVisitorGroups();

		interface VisitorGroupSummary {

			List<VisitorSummary> getVisitors();

			interface VisitorSummary {

				String getFirstName();

				String getLastName();

				AddressSummary getAddress();

				interface AddressSummary {

					String getStreet();

					String getHouseNumber();

					String getZipCode();

					String getCity();
				}
			}
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class VisitorDto {

		private @NotBlank @Pattern(regexp = Strings.NAMES) String lastName, firstName;
		private @NotBlank @Pattern(regexp = Strings.STREET) String street;
		private @NotBlank @Pattern(regexp = Strings.HOUSE_NUMBER) String houseNumber;
		private @NotBlank @Pattern(regexp = ZipCode.PATTERN) String zipCode;
		private @NotBlank @Pattern(regexp = Strings.CITY) String city;
		/**
		 * If not given, email must be given.
		 **/
		private @Pattern(regexp = PhoneNumber.PATTERN) String phone;
		/**
		 * If not given, phone must be given.
		 **/
		private @Email String email;
		/**
		 * Can be used to give additional seating information (table...)
		 **/
		private @Textual String qualifier;

		VisitorDto validate(Errors errors) {

			return this;
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class VisitorGroupDto {

		private @NotEmpty List<VisitorDto> visitors;
		private @Pattern(regexp = OccasionCode.REGEX) String occcasionCode;
		private @NotBlank @Textual String locationName;
		private @Textual String comment;
		private @NotNull LocalDate startDate;
		private LocalTime startTime;
		private LocalDate endDate;
		private LocalTime endTime;

		VisitorGroupDto validate(Errors errors) {

			if (!getStart().isBefore(getEnd())) {

				Stream.of("startDate", "startTime", "endDate", "endTime")
						.forEach(it -> errors.rejectValue(it, "foobar"));
			}

			return this;
		}

		public LocalDateTime getStart() {

			var timeToUse = startTime == null ? LocalTime.MIDNIGHT : startTime;

			return LocalDateTime.of(startDate, timeToUse);
		}

		public LocalDateTime getEnd() {

			var timeToUse = endTime == null ? LocalTime.MIDNIGHT.minusMinutes(1) : endTime;
			var dateToUse = endDate == null ? startDate : endDate;

			return LocalDateTime.of(dateToUse, timeToUse);
		}
	}

}
