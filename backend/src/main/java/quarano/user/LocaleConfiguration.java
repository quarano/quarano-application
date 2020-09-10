package quarano.user;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.LocaleUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.AuthenticationManager;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonRepository;

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

	@Override
	public void addInterceptors(InterceptorRegistry interceptorRegistry) {
		interceptorRegistry.addInterceptor(localeChangeInterceptor());
	}

	/**
	 * Resolves the locale from users stored data if set and if not then from requests <code>Accept-Language</code>
	 * header.
	 */
	public class LocaleResolver extends AcceptHeaderLocaleResolver {

		public LocaleResolver() {
			setDefaultLocale(Locale.GERMANY);
			setSupportedLocales(LOCALES);
		}

		@Override
		public Locale resolveLocale(HttpServletRequest request) {
			return getLocaleFromTrackedPerson().orElseGet(() -> super.resolveLocale(request));
		}

		private Optional<Locale> getLocaleFromTrackedPerson() {

			return accounts.getCurrentUser()
					.flatMap(people::findByAccount)
					.map(TrackedPerson::getLocale)
					.filter(it -> !Collections.disjoint(LocaleUtils.localeLookupList(it), LOCALES));
		}

		/**
		 * This method is called by {@link LocaleChangeInterceptor} to set the locale if one was specified by parameter.
		 * Sets the given {@link Locale} to the {@link TrackedPerson} of the current account and saves this
		 * {@link TrackedPerson}.
		 */
		@Override
		public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
			accounts.getCurrentUser()
					.flatMap(people::findByAccount)
					.map(it -> it.setLocale(locale))
					.ifPresent(people::save);
		}
	}

	/**
	 * Sets the used language to the response header <code>Content-Language</code>.
	 */
	@Order(1000)
	@Component
	@RequiredArgsConstructor
	class SetContentHeaderFilter extends OncePerRequestFilter {

		private final @NonNull LocaleResolver localeResolver;

		@Override
		protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
				FilterChain filterChain) throws ServletException, IOException {

			filterChain.doFilter(httpServletRequest, httpServletResponse);

			var locale = localeResolver.resolveLocale(httpServletRequest);
			httpServletResponse.setLocale(locale);
			httpServletResponse.addHeader(HttpHeaders.CONTENT_LANGUAGE, locale.toLanguageTag());
		}
	}

	/**
	 * monitors request URLs for language changing
	 */
	@Order(999)
	@Component
	@RequiredArgsConstructor
	public class AdoptLanguageChangeFilter extends OncePerRequestFilter {

		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
				FilterChain filterChain) throws ServletException, IOException {

			accounts.getCurrentUser().ifPresent(account -> {
				Optional.ofNullable(request.getParameter("locale")).ifPresent(locale -> {

					persons.findByAccount(account).ifPresent(person -> {
						switch (locale.length()) {
							case 2:
								person.setLocale(new Locale(locale));
								persons.save(person);
								break;
							case 5:
								if ('_' == locale.charAt(2)) {
									person.setLocale(new Locale(locale.substring(0, 2), locale.substring(3)));
									persons.save(person);
								}
								break;
							default:
								logger.error("Locale change attempted with unsupported value: " + locale);
								break;
						}
					});
				});
			});

			filterChain.doFilter(request, response);
		}
	}

}
