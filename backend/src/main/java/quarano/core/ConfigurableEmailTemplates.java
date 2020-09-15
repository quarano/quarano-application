package quarano.core;

import io.vavr.Tuple;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

/**
 * An {@link EmailTemplates} implementation that maps {@link Key} instances to files obtained via a
 * {@link ResourceLoader}.
 *
 * @author Oliver Drotbohm
 * @author Jens Kutzsche
 */
class ConfigurableEmailTemplates implements EmailTemplates {

	static final Locale DEFAULT_LOCALE = Locale.GERMANY;

	private final ResourceLoader resources;
	private final Map<Key, String> templates;
	private final Map<Tuple, String> cache;
	private final String pathTemplate;

	/**
	 * Creates a new {@link ConfigurableEmailTemplates} for the given {@link ResourceLoader}, {@link Stream} of template
	 * {@link Key}s and a path template to turn the keys into actual files accessible for the {@link ResourceLoader}. In
	 * the path template use <b>%1$s for file name</b> and <b>%2$s for language tage</b>.
	 *
	 * @param resources must not be {@literal null}.
	 * @param keys must not be {@literal null}.
	 * @param pathTemplate must not be {@literal null} or empty.
	 */
	public ConfigurableEmailTemplates(ResourceLoader resources, Stream<Key> keys, String pathTemplate) {

		Assert.notNull(resources, "ResourceLoader must not be null!");
		Assert.notNull(keys, "Keys must not be null!");
		Assert.hasText(pathTemplate, "File template must not be null or empty!");

		this.resources = resources;
		this.templates = keys.collect(Collectors.toMap(Function.identity(), Key::toFileName));
		this.pathTemplate = pathTemplate;
		this.cache = new ConcurrentHashMap<>(templates.size());
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.core.EmailTemplates#expandTemplate(quarano.core.EmailTemplates.Key, java.util.Map)
	 */
	@Override
	public String expandTemplate(Key key, Map<String, Object> placeholders, Locale locale) {

		var cacheKey = Tuple.of(key, locale);
		var text = cache.computeIfAbsent(cacheKey, __ -> {

			var localeTags = createLocaleTags(locale);
			var resource = getFirstExistingResource(templates.get(key), localeTags);
			var template = readTemplate(resource);

			String localeTag = resource.getLocaleTag();

			if (StringUtils.isNotBlank(localeTag) && !localeTag.startsWith(DEFAULT_LOCALE.getLanguage())) {

				var resourceDefault = getFirstExistingResource(templates.get(key), List.of(""));
				var templateDefault = readTemplate(resourceDefault);

				return String.join("\r\n\r\n==========\r\n\r\n", template, templateDefault);

			} else {
				return template;
			}
		});

		for (Entry<String, Object> replacement : placeholders.entrySet()) {
			text = text.replace(String.format("{%s}", replacement.getKey()), replacement.getValue().toString());
		}

		return text;
	}

	private ArrayList<String> createLocaleTags(Locale locale) {

		var localeTags = new ArrayList<String>(4);

		localeTags.add(""); // fallback

		if (locale != null) {

			var joiner = new StringJoiner("_");

			var language = locale.getLanguage();
			if (StringUtils.isNoneBlank(language)) {

				joiner.add(language);
				localeTags.add(0, joiner.toString());
			}

			var country = locale.getCountry();
			if (StringUtils.isNoneBlank(country)) {

				joiner.add(country);
				localeTags.add(0, joiner.toString());
			}

			var variant = locale.getVariant();
			if (StringUtils.isNoneBlank(variant)) {

				joiner.add(variant);
				localeTags.add(0, joiner.toString());
			}
		}

		return localeTags;
	}

	private LocaleResource getFirstExistingResource(String templateName, List<String> localeTags) {

		return localeTags.stream()
				.map(LocaleResource::of)
				.map(it -> it.withPath(String.format(pathTemplate, templateName, it.getLocaleTag())))
				.map(it -> it.withResource(resources.getResource(it.getPath())))
				.filter(it -> it.getResource().exists())
				.findFirst()
				.orElseThrow(() -> {

					var localeTagsStr = "[" + String.join("|", localeTags) + "]";
					return new IllegalStateException("Could not find any of the template files "
							+ String.format(pathTemplate, templateName, localeTagsStr) + "!");
				});
	}

	private String readTemplate(LocaleResource resource) {

		return Try.of(() -> resource.getResource().getFile().getPath())
				.map(Path::of)
				.mapTry(Files::readString)
				.getOrElseThrow(o_O -> new IllegalStateException("Could not read file " + resource.getPath() + "!", o_O));
	}

	@RequiredArgsConstructor(staticName = "of")
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	private static class LocaleResource {
		@Getter
		final String localeTag;
		@With
		@Getter
		String path;
		@With
		@Getter
		Resource resource;
	}
}
