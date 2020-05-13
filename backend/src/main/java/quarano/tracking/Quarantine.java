package quarano.tracking;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Oliver Drotbohm
 */
@Data
@Embeddable
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(staticName = "of")
public class Quarantine {

	@Column(name = "quarantine_from") //
	private final LocalDate from;

	@Column(name = "quarantine_to") //
	private final LocalDate to;
}
