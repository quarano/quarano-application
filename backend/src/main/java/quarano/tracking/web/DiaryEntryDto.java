package quarano.tracking.web;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class DiaryEntryDto {

	private LocalDateTime date;
	private float bodyTemperature;
	private List<UUID> symptoms = new ArrayList<>();
	private List<UUID> contacts = new ArrayList<>();
}
