package quarano.department;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Account;
import quarano.account.AuthenticationManager;

import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * A factory to create system {@link Comment} instances to abstract away the lookup of the comment text via a
 * {@link MessageSourceAccessor} and determining the comment author with the system name as fallback. The comment texts
 * will be resolved via the given {@link CommentKey} extended by {@code .success} or {@code .failure}.
 *
 * @author Oliver Drotbohm
 * @since 1.4
 */
@Component
@RequiredArgsConstructor
class CommentFactory {

	private final @NonNull MessageSourceAccessor messages;
	private final @NonNull AuthenticationManager authentication;
	private final @NonNull ApplicationContext context;

	/**
	 * Creates a new {@link Comment} for the success message resolved for the given key.
	 *
	 * @param key must not be {@literal null}.
	 * @param args must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public Comment successComment(CommentKey key, Object... args) {

		Assert.notNull(key, "Comment key must not be null!");
		Assert.notNull(args, "Arguments must not be null!");

		return createComment(key.toSuccessKey(), args);
	}

	/**
	 * Creates a new {@link Comment} for the falure message resolved for the given key.
	 *
	 * @param key must not be {@literal null}.
	 * @param args must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public Comment failureComment(CommentKey key, Object... args) {

		Assert.notNull(key, "Comment key must not be null!");
		Assert.notNull(args, "Arguments must not be null!");

		return createComment(key.toFailureKey(), args);
	}

	private Comment createComment(String key, Object... args) {
		return new Comment(messages.getMessage(key, args), getAuthor());
	}

	enum CommentKey {

		/**
		 * No registration email was sent out.
		 */
		REGISTRATION__NO_EMAIL,

		/**
		 * Registration was initiated.
		 */
		REGISTRATION__INITIATED,

		/**
		 * A welcome email was sent out.
		 */
		REGISTRATION__WELCOME_EMAIL_SENT,

		/**
		 * The email containing registration information was sent out.
		 */
		REGISTRATION__EMAIL_SENT;

		/**
		 * Returns the key base with an appended {@code .success}.
		 *
		 * @return will never be {@literal null}.
		 */
		String toSuccessKey() {
			return getKeyBase() + ".success";
		}

		/**
		 * Returns the key base with an appended {@code .failure}.
		 *
		 * @return will never be {@literal null}.
		 */
		String toFailureKey() {
			return getKeyBase() + ".failure";
		}

		private String getKeyBase() {
			return "quarano." + name()
					.replace("__", ".")
					.replace("_", "-")
					.toLowerCase(Locale.ENGLISH);
		}
	}

	private String getAuthor() {

		return authentication.getCurrentUser()
				.map(Account::getFullName)
				.orElse(context.getId()); // Application name
	}
}
