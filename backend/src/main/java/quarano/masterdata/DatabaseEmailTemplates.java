package quarano.masterdata;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.DepartmentProperties;
import quarano.core.EmailTemplates;
import quarano.core.I18nProperties;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.lang.Nullable;

/**
 * An {@link EmailTemplates} implementation that maps {@link Key} instances to database entries of the email_texts table
 * via a {@link EmailTextRepository}.
 *
 * @author Jens Kutzsche
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
class DatabaseEmailTemplates implements EmailTemplates {

	private final @NonNull EmailTextRepository texts;
	private final @NonNull I18nProperties i18n;
	private final @NonNull DepartmentProperties depProperties;

	private final Map<Tuple2<Key, Locale>, Templated> CACHE = new ConcurrentHashMap<>();

	/*
	 * (non-Javadoc)
	 * @see quarano.core.EmailTemplates#expandTemplate(quarano.core.EmailTemplates.Key, java.util.Map, java.util.Locale)
	 */
	@Override
	public String expandTemplate(Key key, Map<String, Object> placeholders, @Nullable Locale locale) {

		var defaultedLocale = i18n.orDefaultLocale(locale);

		var text = CACHE.computeIfAbsent(Tuple.of(key, defaultedLocale), it -> it.apply(this::foo));

		var parameters = new HashMap<>(placeholders);
		parameters.put("departmentName", depProperties.getDefaultDepartment().getName());

		return text.expand(parameters);
	}

	private Templated foo(Key key, Locale locale) {

		// CORE-550: Temporary solution to avoid having to implement a language selection in the frontend at the moment.
		// With only two languages, e-mails should always contain both.
		var defaultLocale = i18n.getSupportedLocales().stream()
				.filter(it -> !it.equals(locale))
				.findFirst()
				.orElse(i18n.getDefaultLocale());

		return texts.findByTextKey(key, locale).orElseThrow()
				.withDefault(defaultLocale, it -> texts.findByTextKey(it, defaultLocale));
	}
}
