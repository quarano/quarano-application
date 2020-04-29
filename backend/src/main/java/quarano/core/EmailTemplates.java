/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

	public String getTemplate(Keys name, Map<String, String> placeholders) {

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

		for (Entry<String, String> replacement : placeholders.entrySet()) {
			template = template.replace(String.format("{%s}", replacement.getKey()), replacement.getValue());
		}

		return template;
	}

	public enum Keys {
		REGISTRATION;
	}
}
