package quarano.tracking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import quarano.core.EmailAddress;
import quarano.core.EmailSender.AbstractTemplatedEmail.Recipient;

import java.util.Optional;

/**
 * A {@link Recipient} based on a {@link TrackedPerson}.
 *
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TrackedPersonReceipient implements Recipient {

	private static final String NO_RECEIPIENT_ADDRESS = "Tracked person %s does not have an email address registered!";

	private final TrackedPerson person;
	private final EmailAddress email;

	/**
	 * Creates a new {@link Recipient} for the given {@link TrackedPerson}.
	 *
	 * @param person must not be {@literal null} and have an {@link EmailAddress} set.
	 * @return
	 */
	public static Recipient of(TrackedPerson person) {

		var emailAddress = Optional.ofNullable(person.getEmailAddress())
				.orElseThrow(() -> new IllegalArgumentException(String.format(NO_RECEIPIENT_ADDRESS, person)));

		return new TrackedPersonReceipient(person, emailAddress);
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.core.EmailSender.TemplatedEmail.Receipient#getEmailAddress()
	 */
	@Override
	public EmailAddress getEmailAddress() {
		return email;
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.core.EmailSender.TemplatedEmail.Receipient#getFullName()
	 */
	@Override
	public String getFullName() {
		return person.getFullName();
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.core.EmailSender.TemplatedEmail.Receipient#getLastName()
	 */
	@Override
	public String getLastName() {
		return person.getLastName();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s <%s>", getFullName(), email);
	}
}
