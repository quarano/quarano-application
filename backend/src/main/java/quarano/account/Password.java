package quarano.account;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * @author Oliver Gierke
 */
public abstract class Password {

	/**
	 * Returns the password's raw {@link String} value.
	 *
	 * @return
	 */
	abstract String asString();

	@Embeddable
	@EqualsAndHashCode(callSuper = false)
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE, onConstructor = @__(@Deprecated))
	public static class EncryptedPassword extends Password {

		private final @Column(name = "password") String value;
		private final @Nullable LocalDateTime expiryDate;

		/**
		 * Creates a new {@link EncryptedPassword} from the given value and no expiry date. I.e. the password will never
		 * indicate it's expired.
		 *
		 * @param value must not be {@literal null} or empty.
		 * @return will never be {@literal null}.
		 */
		static EncryptedPassword of(String value) {

			Assert.hasText(value, "Password must not be null or empty!");

			return new EncryptedPassword(value, null);
		}

		/**
		 * Creates a new {@link EncryptedPassword} with the given value and expiry date.
		 *
		 * @param value must not be {@literal null} or empty.
		 * @param expiryDate must not be {@literal null}.
		 * @return will never be {@literal null}.
		 */
		static EncryptedPassword of(String value, LocalDateTime expiryDate) {

			Assert.hasText(value, "Password must not be null or empty!");
			Assert.notNull(expiryDate, "Expiry date must not be null!");

			return new EncryptedPassword(value, expiryDate);
		}

		public boolean isExpired() {
			return expiryDate != null && expiryDate.isBefore(LocalDateTime.now());
		}

		/*
		 * (non-Javadoc)
		 * @see quarano.account.Password#asString()
		 */
		@Override
		String asString() {
			return value;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return value;
		}
	}

	/**
	 * An unencrypted password.
	 *
	 * @author Oliver Drotbohm
	 */
	@EqualsAndHashCode(callSuper = false)
	public static class UnencryptedPassword extends Password {

		private final String value;

		/**
		 * Creates a new {@link UnencryptedPassword} for the given raw {@link String} value.
		 *
		 * @param password must not be {@literal null} or empty.
		 */
		private UnencryptedPassword(String password) {

			Assert.hasText(password, "Password must not be null or empty!");

			this.value = password;
		}

		public static UnencryptedPassword of(String password) {
			return new UnencryptedPassword(password);
		}

		/**
		 * Returns the length of the password.
		 *
		 * @return
		 */
		public int getLength() {
			return value.length();
		}

		/*
		 * (non-Javadoc)
		 * @see quarano.account.Password#asString()
		 */
		@Override
		String asString() {
			return value;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "********";
		}
	}
}
