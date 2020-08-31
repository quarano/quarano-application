package quarano.maintenance.web;

import java.time.LocalDate;

import lombok.Data;

@Data
public class QuarantineDto extends QuaranoDto {

	private LocalDate from;
	private LocalDate to;
}
