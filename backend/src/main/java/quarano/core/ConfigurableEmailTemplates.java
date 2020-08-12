package quarano.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

/**
 * An {@link EmailTemplates} implementation that maps {@link Key} instances to files obtained via a
 * {@link ResourceLoader}.
 *
 * @author Oliver Drotbohm
 */
class ConfigurableEmailTemplates implements EmailTemplates {

	private final ResourceLoader resources;
	private final Map<Key, String> templates;
	private final Map<Key, String> cache;

	/**
	 * Creates a new {@link ConfigurableEmailTemplates} for the given {@link ResourceLoader}, {@link Stream} of template
	 * {@link Key}s and a file template to turn the keys into actual files accessible for the {@link ResourceLoader}.
	 *
	 * @param resources must not be {@literal null}.
	 * @param keys must not be {@literal null}.
	 * @param fileTemplate must not be {@literal null} or empty.
	 */
	public ConfigurableEmailTemplates(ResourceLoader resources, Stream<Key> keys, String fileTemplate) {

		Assert.notNull(resources, "ResourceLoader must not be null!");
		Assert.notNull(keys, "Keys must not be null!");
		Assert.hasText(fileTemplate, "File template must not be null or empty!");

		this.resources = resources;
		this.templates = keys.collect(Collectors.toMap(Function.identity(), it -> it.toFileName(fileTemplate)));
		this.cache = new ConcurrentHashMap<>(templates.size());
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.core.EmailTemplates#expandTemplate(quarano.core.EmailTemplates.Key, java.util.Map)
	 */
	@Override
	public String expandTemplate(Key key, Map<String, Object> placeholders) {

		var template = cache.computeIfAbsent(key, it -> {

			var location = templates.get(key);
			var resource = resources.getResource(location);

			try {
				var path = resource.getFile().getPath();
				return Files.readString(Path.of(path));
			} catch (IOException o_O) {
				throw new IllegalStateException("Could not read file " + location + "!", o_O);
			}
		});

		for (Entry<String, Object> replacement : placeholders.entrySet()) {
			template = template.replace(String.format("{%s}", replacement.getKey()), replacement.getValue().toString());
		}

		return template;
	}
}
