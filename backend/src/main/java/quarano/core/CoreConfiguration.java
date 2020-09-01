package quarano.core;

import quarano.core.EmailTemplates.Keys;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

/**
 * Configuration class to expose an {@link EmailTemplates} bean.
 *
 * @author Oliver Drotbohm
 */
@Configuration(proxyBeanMethods = false)
class CoreConfiguration {

	@Bean
	ConfigurableEmailTemplates emailTemplates(ResourceLoader loader) {
		return new ConfigurableEmailTemplates(loader, Keys.stream(), "classpath:masterdata/templates/%s");
	}
}
