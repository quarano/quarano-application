package quarano.department;

import quarano.core.EmailSender.TemplatedEmail;
import quarano.department.activation.ActivationCode;

/**
 * API to lookup the {@link TemplatedEmail} to be sent out for initiated registrations. Exists to not have to expose
 * {@link TrackedCaseEmails} entirely.
 *
 * @author Oliver Drotbohm
 * @since 1.4
 */
public interface RegistrationEmailProvider {

	/**
	 * Returns the {@link TemplatedEmail} to be sent out for a registration for a {@link TrackedCase} and
	 * {@link ActivationCode}.
	 *
	 * @param trackedCase must not be {@literal null}.
	 * @param code must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	TemplatedEmail getRegistrationEmail(TrackedCase trackedCase, ActivationCode code);
}
