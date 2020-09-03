package quarano.user;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.AuthenticationManager;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Locale.LanguageRange;
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
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

/**
 * Configuration class for locale and message related matters.
 *
 * @author Jens Kutzsche
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class LocaleConfiguration {

	public static final List<Locale> LOCALES = List.of(
			new Locale("en"),
			new Locale("tr"));

	private final @NonNull AuthenticationManager accounts;
	private final @NonNull TrackedPersonRepository persons;

	@Bean
	LocaleResolver localeResolver() {
		return new LocaleResolver();
	}

	@Bean
	MessageSourceAccessor messageSourceAccessor(MessageSource source) {
		return new MessageSourceAccessor(source);
	}

	/**
	 * Resolves the locale from users stored data if set and if not then from requests <code>Accept-Language</code>
	 * header.
	 */
	class LocaleResolver extends AcceptHeaderLocaleResolver {

		@Override
		public Locale resolveLocale(HttpServletRequest request) {
			return getLocaleFromTrackedPerson().or(() -> resolveLocaleFrom(request)).orElse(Locale.GERMANY);
		}

		private Optional<Locale> getLocaleFromTrackedPerson() {

			return accounts.getCurrentUser()
					.flatMap(persons::findByAccount)
					.map(TrackedPerson::getLocale)
					.filter(it -> !Collections.disjoint(LocaleUtils.localeLookupList(it), LOCALES));
		}

		private Optional<Locale> resolveLocaleFrom(HttpServletRequest request) {

			var headerLang = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);

			return Optional.ofNullable(headerLang)
					.filter(StringUtils::hasText)
					.map(LanguageRange::parse)
					.map(it -> Locale.lookup(it, LOCALES));
		}
	}

	/**
	 * Sets the used language to the response header <code>Content-Language</code>.
	 */
	@Component
	@RequiredArgsConstructor
	public class SetContentHeaderFilter extends OncePerRequestFilter {

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
}
