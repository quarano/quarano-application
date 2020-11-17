package quarano.util;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

import javax.mail.Message;

import org.junit.jupiter.api.function.ThrowingConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;

/**
 * Wrapper around a {@link GreenMail} instance to simplify testing assertions.
 *
 * @author Oliver Drotbohm
 * @since 1.4
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TestEmailServer {

	private final GreenMail server;
	private final @With Duration timeout;

	@Autowired
	public TestEmailServer(GreenMail server) {
		this(server, Duration.ofMillis(500));
	}

	/**
	 * Resets the underlying test server.
	 *
	 * @return will never be {@literal null}.
	 */
	public TestEmailServer reset() {

		try {
			server.purgeEmailFromAllMailboxes();
		} catch (FolderException e) {
			throw new RuntimeException(e);
		}

		return this;
	}

	/**
	 * Executes the given {@link ThrowingConsumer} for the message received.
	 *
	 * @param callback must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public TestEmailServer assertEmailSent(ThrowingConsumer<Message> callback) {

		Assert.notNull(callback, "Message callback must not be null!");

		assertThat(server.waitForIncomingEmail(timeout.toMillis(), 1)).isTrue();

		var message = server.getReceivedMessages()[0];

		log.info("Received email:\n\n{}\n", message);

		assertThatCode(() -> callback.accept(message)).doesNotThrowAnyException();

		return this;
	}

	/**
	 * Executes the given {@link ThrowingConsumer} for the message body received.
	 *
	 * @param callback must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public TestEmailServer assertEmailSentWithBody(ThrowingConsumer<String> callback) {

		assertThat(server.waitForIncomingEmail(timeout.toMillis(), 1)).isTrue();

		var message = server.getReceivedMessages()[0];
		var content = GreenMailUtil.getBody(message);

		log.info("Received email with content:\n\n{}\n", content);

		assertThatCode(() -> callback.accept(content)).doesNotThrowAnyException();

		return this;
	}

	/**
	 * Verifies that no emails were sent within the configured timeout.
	 *
	 * @return will never be {@literal null}.
	 * @see #withTimeout(Duration)
	 */
	public TestEmailServer assertNoEmailSent() {

		assertThat(server.waitForIncomingEmail(timeout.toMillis(), 1)).isFalse();
		assertThat(server.getReceivedMessages()).hasSize(0);

		return this;
	}
}
