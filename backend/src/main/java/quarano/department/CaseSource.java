package quarano.department;

import lombok.Value;
import quarano.tracking.ContactPerson;
import quarano.tracking.Encounter;

import java.util.stream.Stream;

import org.springframework.util.Assert;

/**
 * The source of a {@link TrackedCase}.
 *
 * @author Oliver Drotbohm
 * @soundtrack The Chicks - March March (Gaslighter)
 */
@Value(staticConstructor = "of")
class CaseSource {

	private final ContactPerson person;
	private final TrackedCase trackedCase;

	/**
	 * Returns all {@link CaseSource}s for all the {@link Encounter}s registered for the given origin case.
	 *
	 * @param origin must not be {@literal null}.
	 * @return must not be {@literal null}.
	 */
	public static Stream<CaseSource> forAllContacts(TrackedCase origin) {

		Assert.notNull(origin, "Origin case must not be null!");

		return origin.getTrackedPerson().getEncounters().stream()
				.map(Encounter::getContact)
				.distinct()
				.map(it -> CaseSource.of(it, origin));
	}
}
