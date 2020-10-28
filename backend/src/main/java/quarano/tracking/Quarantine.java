package quarano.tracking;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Oliver Drotbohm
 */
@Data
@Embeddable
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Quarantine {

	private static final ZoneId ZONE_BERLIN = ZoneId.of("Europe/Berlin");

	@Column(name = "quarantine_from")
	private final LocalDate from;

	@Column(name = "quarantine_to")
	private final LocalDate to;

	@Column(name = "quarantine_last_modified")
	private final LocalDateTime lastModified;

	public static Quarantine of(LocalDate from, LocalDate to) {
		return new Quarantine(from, to, LocalDateTime.now());
	}

	public boolean isOver() {
		return LocalDate.now(ZONE_BERLIN).isAfter(to);
	}
}
