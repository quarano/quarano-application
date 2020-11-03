package quarano.user;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Account;
import quarano.account.Account.AccountIdentifier;
import quarano.account.AuthenticationManager;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonRepository;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.LocaleUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * Configuration class for locale and message related matters.
 *
 * @author Jens Kutzsche
 * @author Paul Guhl
 * @author Oliver Drotbohm
 */
@EnableAspectJAutoProxy
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class LocaleConfiguration implements WebMvcConfigurer {

	public static final List<Locale> LOCALES = List.of(
			Locale.GERMAN,
			Locale.ENGLISH);

	@Bean
	LocaleResolver localeResolver(TrackedPersonLocaleLookup lookup, AuthenticationManager accounts) {
		return new LocaleResolver(lookup, accounts);
	}

	@Bean
	MessageSourceAccessor messageSourceAccessor(MessageSource source) {
		return new MessageSourceAccessor(source);
	}

	@Bean
	LocaleChangeInterceptor localeChangeInterceptor() {
		return new LocaleChangeInterceptor();
	}

	@Bean
	HandlerInterceptor getHandlerInterceptor() {
		return new SetContentHeaderInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry interceptorRegistry) {
		interceptorRegistry.addInterceptor(localeChangeInterceptor());
		interceptorRegistry.addInterceptor(getHandlerInterceptor());
	}

	@Component
	@RequestScope
	@RequiredArgsConstructor
	static class TrackedPersonLocaleLookup {

		private final @NonNull TrackedPersonRepository people;

		private Optional<Locale> CACHE;

		Optional<Locale> lookupLocale(AccountIdentifier identifier) {

			if (CACHE != null) {
				return CACHE;
			}

			CACHE = people.findLocaleByAccount(identifier)
					.filter(locale -> !Collections.disjoint(LocaleUtils.localeLookupList(locale), LOCALES));

			return CACHE;
		}

		void setLocale(Locale locale, Optional<Account> account) {

			CACHE = Optional.ofNullable(locale);

			account.ifPresent(it -> {

				people.findByAccount(it)
						.map(person -> person.setLocale(locale))
						.ifPresent(people::save);
			});
		}
	}

	/**
	 * Resolves the locale from users stored data if set and if not then from requests <code>Accept-Language</code>
	 * header.
	 */

	class LocaleResolver extends AcceptHeaderLocaleResolver {

		private final TrackedPersonLocaleLookup lookup;
		private final AuthenticationManager accounts;

		public LocaleResolver(TrackedPersonLocaleLookup lookup, AuthenticationManager accounts) {

			this.lookup = lookup;
			this.accounts = accounts;

			setDefaultLocale(Locale.GERMANY);
			setSupportedLocales(LOCALES);
			Locale.setDefault(getDefaultLocale());
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver#resolveLocale(javax.servlet.http.HttpServletRequest)
		 */
		@Override
		public Locale resolveLocale(HttpServletRequest request) {

			Optional<Account> currentUser = accounts.getCurrentUser();

			// For users who are not tracked persons, German should always be used.
			if (currentUser.filter(Account::isTrackedPerson).isEmpty()) {
				return getDefaultLocale();
			}

			return currentUser
					.map(Account::getId)
					.flatMap(lookup::lookupLocale)
					.orElseGet(() -> super.resolveLocale(request));
		}

		/**
		 * This method is called by {@link LocaleChangeInterceptor} to set the locale if one was specified by parameter.
		 * Sets the given {@link Locale} to the {@link TrackedPerson} of the current account and saves this
		 * {@link TrackedPerson}.
		 */
		@Override
		public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
			lookup.setLocale(locale, accounts.getCurrentUser());
		}
	}

	/**
	 * Sets the used language to the response header <code>Content-Language</code>.
	 */
	class SetContentHeaderInterceptor extends HandlerInterceptorAdapter {

		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
				throws Exception {

			response.setLocale(LocaleContextHolder.getLocale());

			return super.preHandle(request, response, handler);
		}
	}
}
