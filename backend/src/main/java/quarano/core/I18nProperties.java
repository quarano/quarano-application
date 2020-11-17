package quarano.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.lang.Nullable;

/**
 * @author Jens Kutzsche
 * @author Oliver Drotbohm
 */
@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "quarano.i18n")
public class I18nProperties {

	private static final List<Locale> DEFAULT_SUPPORTED_LOCALES = List.of(Locale.GERMAN, Locale.ENGLISH);
	private static final Locale FALLBACK_LOCALE = Locale.GERMAN;

	private final Locale defaultLocale;

	/**
	 * The {@link Locale} to be supported by the application.
	 */
	private final List<Locale> supportedLocales;

	/**
	 * Returns the default {@link Locale} to be used if none was selected otherwise.
	 *
	 * @return will never be {@literal null}.
	 */
	public Locale getDefaultLocale() {
		return defaultLocale == null ? FALLBACK_LOCALE : defaultLocale;
	}

	/**
	 * Returns the given {@link Locale} or the default one if the given value is {@literal null}.
	 *
	 * @param locale can be {@literal null}.
	 * @return will never be {@literal null}.
	 * @see #getDefaultLocale()
	 */
	public Locale orDefaultLocale(@Nullable Locale locale) {
		return locale == null ? getDefaultLocale() : locale;
	}

	/**
	 * Returns all supported {@link Locale}s.
	 *
	 * @return will never be {@literal null}.
	 */
	public List<Locale> getSupportedLocales() {
		return supportedLocales == null ? DEFAULT_SUPPORTED_LOCALES : supportedLocales;
	}

	/**
	 * Returns a non-default {@link Locale} from all the supported ones.
	 *
	 * @return will never be {@literal null}.
	 */
	public Locale getNonDefaultLocale() {

		return getSupportedLocales().stream()
				.filter(it -> !getDefaultLocale().equals(it))
				.findFirst()
				.orElseThrow();
	}
}
