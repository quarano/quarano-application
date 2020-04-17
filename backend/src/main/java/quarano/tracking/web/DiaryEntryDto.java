package quarano.tracking.web;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

@Data
public class DiaryEntryDto {

	private UUID id;
	private @NotNull LocalDateTime date;
	private float bodyTemperature;
	private List<UUID> symptoms = new ArrayList<>();
	private List<UUID> contacts = new ArrayList<>();
}
