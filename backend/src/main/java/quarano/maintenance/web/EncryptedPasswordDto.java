package quarano.maintenance.web;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EncryptedPasswordDto {
	private String value;
	private LocalDateTime expiryDate;
}
