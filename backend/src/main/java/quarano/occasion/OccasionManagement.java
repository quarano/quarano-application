package quarano.occasion;

import lombok.RequiredArgsConstructor;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.TrackedCaseRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.constraints.NotNull;

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
	private final @NotNull TrackedCaseRepository trackedCaseRepository;

	/**
	 * Creates a new {@link Occasion} with the given title, start and end date.
	 *
	 * @param title must not be {@literal null} or empty.
	 * @param start must not be {@literal null}.
	 * @param end must not be {@literal null}.
	 * @param trackedCaseId the {@link TrackedCaseIdentifier} for the case which the {@link Occasion} to be created shall
	 *          be associated with. Must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public Optional<Occasion> createOccasion(String title, LocalDateTime start, LocalDateTime end,
			TrackedCaseIdentifier trackedCaseId) {

		return !trackedCaseRepository.existsById(trackedCaseId)
				? Optional.empty()
				: Optional.of(occasions.save(new Occasion(title, start, end, findValidOccasionCode(), trackedCaseId)));
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
	public Optional<Occasion> findOccasionBy(OccasionCode eventCode) {
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
