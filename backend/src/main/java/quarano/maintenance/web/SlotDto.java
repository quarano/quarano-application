package quarano.maintenance.web;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SlotDto {
    private LocalTime from;
    private LocalTime to;
    private LocalTime end;
    private LocalDate date;
}
