package quarano.event.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.Errors;
import quarano.core.validation.Textual;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitorTransmissionDto {

	private List<VisitorDto> visitors;
	private @Textual String eventCode;
	private @Textual String locationName;
	private @Textual String comment;
	private LocalDate startDate;
	private LocalTime startTime;
	private LocalDate endDate;
	private LocalTime endTime;

	VisitorTransmissionDto validate(Errors errors) {
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
