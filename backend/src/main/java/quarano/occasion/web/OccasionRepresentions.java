package quarano.occasion.web;

import io.vavr.collection.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import quarano.core.PhoneNumber;
import quarano.core.validation.Email;
import quarano.core.validation.Strings;
import quarano.core.validation.Textual;
import quarano.core.web.MapperWrapper;
import quarano.occasion.Occasion;
import quarano.occasion.VisitorGroup;
import quarano.tracking.Address;
import quarano.tracking.ZipCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

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

		private @Pattern(regexp = Strings.NAMES) String lastName, firstName;
		private @Pattern(regexp = Strings.STREET) String street;
		private @Pattern(regexp = Strings.HOUSE_NUMBER) String houseNumber;
		private @Pattern(regexp = ZipCode.PATTERN) String zipCode;
		private @Pattern(regexp = Strings.CITY) String city;
		private @Pattern(regexp = PhoneNumber.PATTERN) String phone;
		private @Email String email;
		private @Textual String qualifier;

		VisitorDto validate(Errors errors) {

			return this;
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class VisitorGroupDto {

		private List<VisitorDto> visitors;
		private @Textual String eventCode;
		private @Textual String locationName;
		private @Textual String comment;
		private LocalDate startDate;
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
