package quarano.occasion;

import quarano.core.QuaranoRepository;
import quarano.occasion.Occasion.OccasionIdentifier;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

/**
 * @author David Bauknecht
 * @author Oliver Drotbohm
 * @since 1.4
 */
interface OccasionRepository extends QuaranoRepository<Occasion, OccasionIdentifier> {

	/**
	 * Returns whether the given {@link OccasionCode} is still available, i.e. it is not yet used with any
	 * {@link Occasion} yet.
	 *
	 * @param occasionCode must not be {@literal null}.
	 * @return
	 */
	@Query("select count(1) = 0 from Occasion o where o.occasionCode = :occasionCode")
	boolean isOccasionCodeAvailable(OccasionCode occasionCode);

	/**
	 * Returns the {@link Occasion} with the given {@link OccasionCode} assigned.
	 *
	 * @param occasionCode must not be {@literal null}.
	 * @return
	 */
	@Query("select o from Occasion o where o.occasionCode = :occasionCode")
	Optional<Occasion> findByOccasionCode(OccasionCode occasionCode);
}
