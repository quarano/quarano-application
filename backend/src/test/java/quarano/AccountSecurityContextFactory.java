package quarano;

import lombok.RequiredArgsConstructor;
import quarano.account.Account;
import quarano.account.AccountService;
import quarano.account.Role;

import java.util.stream.Collectors;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
class AccountSecurityContextFactory implements WithSecurityContextFactory<WithQuaranoUser> {

	private final AccountService accounts;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.test.context.support.WithSecurityContextFactory#createSecurityContext(java.lang.annotation.Annotation)
	 */
	@Override
	public SecurityContext createSecurityContext(WithQuaranoUser annotation) {

		return accounts.findByUsername(annotation.value()) //
				.map(AccountAuthentication::new) //
				.map(it -> {

					SecurityContext context = SecurityContextHolder.createEmptyContext();
					context.setAuthentication(it);
					return context;

				})//
				.orElseThrow(() -> new IllegalStateException("Invalid account name!"));
	}

	static class AccountAuthentication extends AbstractAuthenticationToken {

		private static final long serialVersionUID = 9146882696560437595L;

		private final Account account;

		public AccountAuthentication(Account account) {

			super(account.getRoles().stream() //
					.map(Role::toString) //
					.map(SimpleGrantedAuthority::new) //
					.collect(Collectors.toUnmodifiableList()));

			this.account = account;

			setAuthenticated(true);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.security.core.Authentication#getCredentials()
		 */
		@Override
		public Object getCredentials() {
			return account.getUsername();
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.security.core.Authentication#getPrincipal()
		 */
		@Override
		public Object getPrincipal() {
			return account;
		}
	}
}
