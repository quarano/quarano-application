package quarano.department.activation;

import quarano.core.QuaranoRepository;
import quarano.department.activation.ActivationCode.ActivationCodeIdentifier;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

/**
 * @author Patrick Otto
 */
interface ActivationCodeRepository extends QuaranoRepository<ActivationCode, ActivationCodeIdentifier> {

	ActivationCodes findByTrackedPersonId(TrackedPersonIdentifier id);
}
