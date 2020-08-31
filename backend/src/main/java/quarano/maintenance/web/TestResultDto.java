package quarano.maintenance.web;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TestResultDto extends QuaranoDto {
    private boolean infected;
    private LocalDate testDate;
}
