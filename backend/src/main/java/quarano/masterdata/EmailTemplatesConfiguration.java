package quarano.masterdata;

import quarano.account.DepartmentProperties;
import quarano.core.EmailTemplates;
import quarano.core.I18nProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to expose an {@link EmailTemplates} bean.
 *
 * @author Oliver Drotbohm
 * @author Jens Kutzsche
 */
@Configuration(proxyBeanMethods = false)
class EmailTemplatesConfiguration {

	@Bean
	EmailTemplates emailTemplates(EmailTextRepository repository, I18nProperties i18nProperties,
			DepartmentProperties depProperties) {
		return new DatabaseEmailTemplates(repository, i18nProperties, depProperties);
	}
}
