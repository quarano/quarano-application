package quarano.maintenance.web;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EncounterDto extends QuaranoDto {
    private ContactPersonDto contact;
    private LocalDate date;
}
