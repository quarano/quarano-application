package quarano.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * @author Oliver Drotbohm
 */
public class EmailTemplates {

	private final ResourceLoader resources;
	private final Map<Keys, String> templates;
	private final Map<Keys, String> cache;

	public EmailTemplates(ResourceLoader resources, Map<Keys, String> templates) {

		this.resources = resources;
		this.templates = templates;
		this.cache = new ConcurrentHashMap<>(templates.size());
	}

	public String getTemplate(Keys name, Map<String, Object> placeholders) {

		String template = cache.computeIfAbsent(name, it -> {

			String location = templates.get(name);

			Resource resource = resources.getResource(location);

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

	public enum Keys {
		REGISTRATION_INDEX, REGISTRATION_CONTACT, NEW_CONTACT_CASE, DIARY_REMINDER;
	}
}
