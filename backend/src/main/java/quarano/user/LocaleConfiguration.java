package quarano.user;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
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

	public static final Locale TURKISH = new Locale("tr");

	public static final List<Locale> LOCALES = List.of(
			Locale.GERMAN,
			Locale.ENGLISH,
			TURKISH);

	private final @NonNull AuthenticationManager accounts;
	private final @NonNull TrackedPersonRepository people;

	@Bean
	LocaleResolver localeResolver() {
		return new LocaleResolver();
	}

	@Bean
	MessageSourceAccessor messageSourceAccessor(MessageSource source) {
		return new MessageSourceAccessor(source, Locale.GERMANY);
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

		public LocaleResolver() {
			setDefaultLocale(Locale.GERMANY);
			setSupportedLocales(LOCALES);
			Locale.setDefault(getDefaultLocale());
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
	class SetContentHeaderInterceptor extends HandlerInterceptorAdapter {

		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
				throws Exception {

			response.setLocale(LocaleContextHolder.getLocale());

			return super.preHandle(request, response, handler);
		}
	}
}
