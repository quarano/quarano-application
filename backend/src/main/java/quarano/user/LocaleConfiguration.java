package quarano.user;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Account.AccountIdentifier;
import quarano.account.AuthenticationManager;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonRepository;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.LocaleUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.scheduling.annotation.Scheduled;
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
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class LocaleConfiguration implements WebMvcConfigurer {

	public static final List<Locale> LOCALES = List.of(
			Locale.GERMAN,
			Locale.ENGLISH,
			new Locale("tr"));

	private final @NonNull AuthenticationManager accounts;
	private final @NonNull TrackedPersonRepository people;

	@Bean
	LocaleResolver localeResolver() {
		return new LocaleResolver();
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

	/**
	 * Resolves the locale from users stored data if set and if not then from requests <code>Accept-Language</code>
	 * header.
	 */
	class LocaleResolver extends AcceptHeaderLocaleResolver {

		// TODO: Replace with ConcurrentLruCache once we upgrade to Spring 5.3 / Boot 2.4.
		private final Map<AccountIdentifier, Optional<Locale>> CACHE = new ConcurrentHashMap<>();

		public LocaleResolver() {
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
			return getLocaleFromTrackedPerson().orElseGet(() -> super.resolveLocale(request));
		}

		/**
		 * This method is called by {@link LocaleChangeInterceptor} to set the locale if one was specified by parameter.
		 * Sets the given {@link Locale} to the {@link TrackedPerson} of the current account and saves this
		 * {@link TrackedPerson}.
		 */
		@Override
		public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

			accounts.getCurrentUser().ifPresent(it -> {

				CACHE.put(it.getId(), Optional.ofNullable(locale));

				people.findByAccount(it)
						.map(person -> person.setLocale(locale))
						.ifPresent(people::save);
			});
		}

		/**
		 * Wipes the {@link AccountIdentifier} to {@link Locale} cache in case it has outgrown 100 elements.
		 * <p>
		 * TODO: Replace with ConcurrentLruCache once we upgrade to Spring 5.3 / Boot 2.4.
		 */
		@Scheduled(fixedRate = 60 * 1000)
		void wipeCache() {

			if (CACHE.size() > 100) {
				CACHE.clear();
			}
		}

		private Optional<Locale> getLocaleFromTrackedPerson() {

			return accounts.getCurrentUser()
					.flatMap(it -> {

						var identifier = it.getId();
						var cached = CACHE.get(identifier);

						if (cached == null) {
							cached = people.findLocaleByAccount(it)
									.filter(locale -> !Collections.disjoint(LocaleUtils.localeLookupList(locale), LOCALES));
							CACHE.put(identifier, cached);
						}

						return cached;
					});
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
