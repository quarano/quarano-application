package quarano.maintenance.web;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ActivationCodeDto extends QuaranoDto {
	private LocalDateTime expirationTime;
	private String status;
	private int activationTries;
	private String departmentId;

}
