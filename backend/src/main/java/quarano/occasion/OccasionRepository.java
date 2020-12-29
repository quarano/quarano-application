package quarano.occasion;

import quarano.core.QuaranoRepository;
import quarano.occasion.Occasion.OccasionIdentifier;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

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

	/**
	 * Returns the {@link Occasion}s created before the given {@link LocalDateTime}.
	 * 
	 * @param refDate must not be {@literal null}.
	 * @return
	 */
	Streamable<Occasion> findByMetadataCreatedIsBefore(LocalDateTime refDate);

	/**
	 * Deletes all visitor groups with the given occasion codes.
	 *
	 * @param entities must not be {@literal null}. Must not contain {@literal null} elements.
	 * @throws IllegalArgumentException in case the given {@literal occasionCode} or one of its entities is
	 *           {@literal null}.
	 */
	@Modifying
	@Query("delete from VisitorGroup vg where vg.occasionCode in :occasionCodes")
	void deleteVisitorGroupsByOccasionCodeIn(Iterable<OccasionCode> occasionCodes);

	@Modifying
	@Query("delete from Visitor v where v in :visitors")
	void deleteAllVisitors(Iterable<Visitor> visitors);
}
