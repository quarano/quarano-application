package quarano.tracking;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import quarano.account.Account;
import quarano.account.Account.AccountIdentifier;
import quarano.account.AccountService;
import quarano.account.Password.UnencryptedPassword;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * High-level services for {@link TrackedPerson}.
 *
 * @author Oliver Drotbohm
 * @since 1.4
 */
@Transactional
@Service
@RequiredArgsConstructor
public class TrackedPersonManagement {

	private final AccountService accounts;
	private final TrackedPersonRepository people;

	/**
	 * Returns the preferred {@link Locale} for the given {@link AccountIdentifier}.
	 *
	 * @param identifier must not be {@literal null}.
	 * @return
	 */
	public Optional<Locale> findPreferredLocale(AccountIdentifier identifier) {

		Assert.notNull(identifier, "Account identifier must not be null!");

		return people.findLocaleByAccount(identifier);
	}

	/**
	 * Updates the preferred {@link Locale} for the {@link TrackedPerson} identified by the given {@link Account}.
	 *
	 * @param locale must not be {@literal null}.
	 * @param account must not be {@literal null}.
	 */
	public void initializePreferredLocale(Locale locale, Account account) {

		Assert.notNull(locale, "Locale must not be null!");
		Assert.notNull(account, "Account must not be null!");

		people.findByAccount(account)
				.map(it -> it.initializeLocale(locale))
				.ifPresent(people::save);
	}

	/**
	 * Returns the {@link TrackedPerson} for the given {@link Account}.
	 *
	 * @param account must not be {@literal null}.
	 * @return
	 */
	public Optional<TrackedPerson> findByAccount(Account account) {

		Assert.notNull(account, "Account must not be null!");

		return people.findByAccount(account);
	}

	/**
	 * Resets the password for the account identified by the given {@link PasswordReset}.
	 *
	 * @param reset must not be {@literal null}.
	 * @return
	 */
	public Optional<Account> resetPassword(PasswordReset reset) {

		Assert.notNull(reset, "PasswordReset must not be null!");

		return accounts.resetPasswort(reset.getPassword(), reset.getToken(),
				it -> people.findByAccount(it)
						.filter(person -> person.getDateOfBirth().equals(reset.getDateOfBirth()))
						.isPresent());
	}

	/**
	 * Password reset information.
	 *
	 * @author Oliver Drotbohm
	 */
	@Value
	public static class PasswordReset {

		private final @NonNull UUID token;
		private final @NonNull UnencryptedPassword password;
		private final @NonNull LocalDate dateOfBirth;
	}
}
