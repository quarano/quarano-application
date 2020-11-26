package quarano.occasion.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import quarano.core.validation.Textual;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
class OccasionsDto {

	@Textual String title;
	LocalDateTime start;
	LocalDateTime end;
}
