package quarano.masterdata;

import io.vavr.Tuple;
import lombok.RequiredArgsConstructor;
import quarano.account.DepartmentProperties;
import quarano.core.EmailTemplates;
import quarano.core.I18nProperties;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.springframework.util.Assert;

/**
 * An {@link EmailTemplates} implementation that maps {@link Key} instances to database entries of the email_texts table
 * via a {@link EmailTextRepository}.
 *
 * @author Jens Kutzsche
 */
@RequiredArgsConstructor
class DatabasedEmailTemplates implements EmailTemplates {

	private final EmailTextRepository texts;
	private final Map<Tuple, String> cache;
	private I18nProperties i18nProperties;
	private DepartmentProperties depProperties;

	/**
	 * Creates a new {@link DatabasedEmailTemplates} for the given {@link EmailTextRepository}, {@link Stream} of template
	 * {@link Key}s, {@link I18nProperties} and {@link DepartmentProperties}.
	 *
	 * @param resources must not be {@literal null}.
	 * @param keys must not be {@literal null}.
	 * @param i18nProperties must not be {@literal null}.
	 * @param depProperties must not be {@literal null}.
	 */
	public DatabasedEmailTemplates(EmailTextRepository emailTextRepository, Stream<Key> keys,
			I18nProperties i18nProperties, DepartmentProperties depProperties) {

		Assert.notNull(emailTextRepository, "EmailTextRepository must not be null!");
		Assert.notNull(keys, "Keys must not be null!");

		this.texts = emailTextRepository;
		this.cache = new ConcurrentHashMap<>();
		this.i18nProperties = i18nProperties;
		this.depProperties = depProperties;
	}

	@Override
	public String expandTemplate(Key key, Map<String, Object> placeholders, Locale locale) {

		var cacheKey = Tuple.of(key, locale);
		var text = cache.computeIfAbsent(cacheKey, __ -> {

			var template = texts.findByTextKey(key.getKey(), locale).get();

			if (!template.getLocale().getLanguage().equals(i18nProperties.getDefaultLocale().getLanguage())) {

				var templateDefault = texts.findByTextKey(key.getKey(), i18nProperties.getDefaultLocale());

				return String.join("\r\n\r\n==========\r\n\r\n", template.getText(), templateDefault.get().getText());

			} else {
				return template.getText();
			}
		});

		placeholders.put("departmentName", depProperties.getDefaultDepartment().getName());

		for (Entry<String, Object> replacement : placeholders.entrySet()) {
			text = text.replace(String.format("{%s}", replacement.getKey()), replacement.getValue().toString());
		}

		return text;
	}
}
