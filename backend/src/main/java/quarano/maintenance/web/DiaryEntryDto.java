package quarano.maintenance.web;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class DiaryEntryDto extends QuaranoDto {
    private LocalDateTime reportedAt;
    private SlotDto slot;

    private List<ContactPersonDto> contacts;

    private List<SymptomDto> symptoms;
    private List<ActionItemDto> actions;
    private String note;
    private float bodyTemperature;
}
