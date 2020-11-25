package quarano.occasion;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

/**
 * Application service to manage {@link Occasion}s.
 *
 * @author David Bauknecht
 * @author Oliver Drotbohm
 * @since 1.4
 */
@Service
@RequiredArgsConstructor
public class OccasionManagement {

	private final OccasionRepository occasions;

	/**
	 * Creates a new {@link Occasion} with the given title, start and end date.
	 *
	 * @param title must not be {@literal null} or empty.
	 * @param start must not be {@literal null}.
	 * @param end must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public Occasion createOccasion(String title, LocalDateTime start, LocalDateTime end) {
		return occasions.save(new Occasion(title, start, end, findValidOccasionCode()));
	}

	/**
	 * Registers the given {@link VisitorGroup} for the {@link Occasion} for which the given {@link OccasionCode} was
	 * registered.
	 *
	 * @param eventCode must not be {@literal null}.
	 * @param visitorGroup must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public Optional<Occasion> registerVisitorGroupForEvent(OccasionCode eventCode, VisitorGroup visitorGroup) {

		return occasions.findByOccasionCode(eventCode)
				.map(e -> e.registerVisitorGroup(visitorGroup))
				.map(occasions::save);
	}

	/**
	 * Returns the {@link Occasion} that has the given {@link OccasionCode} assigned.
	 *
	 * @param eventCode must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public Optional<Occasion> findEventBy(OccasionCode eventCode) {
		return occasions.findByOccasionCode(eventCode);
	}

	/**
	 * Returns all {@link Occasion}s.
	 *
	 * @return will never be {@literal null}.
	 */
	public Streamable<Occasion> findAll() {
		return occasions.findAll();
	}

	private OccasionCode findValidOccasionCode() {

		OccasionCode occasionCode = OccasionCode.generate();

		return occasions.isOccasionCodeAvailable(occasionCode)
				? occasionCode
				: findValidOccasionCode();
	}
}
