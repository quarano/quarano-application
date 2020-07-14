package quarano;

import lombok.extern.slf4j.Slf4j;
import quarano.account.Role;
import quarano.core.EmailTemplates;
import quarano.core.web.IdentifierProcessor;
import quarano.core.web.MappingCustomizer;
import quarano.core.web.RepositoryMappingModule;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.modelmapper.ModelMapper;
import org.springframework.boot.Banner;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.support.Repositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Oliver Drotbohm
 */
@Slf4j
@SpringBootApplication
@EnableJpaAuditing(dateTimeProviderRef = "quaranoDateTimeProvider")
@ConfigurationPropertiesScan
public class Quarano {

	public static void main(String... args) throws Exception {

		ClassPathResource resource = new ClassPathResource("git.properties");
		Properties properties = PropertiesLoaderUtils.loadProperties(resource);

		SpringApplication application = new SpringApplication(Quarano.class);
		application.setBanner(new QuaranoBanner(properties));
		application.run(args);
	}

	@Bean
	EmailTemplates emailTemplates(ResourceLoader loader) {

		var templates = Map.of(//
				EmailTemplates.Keys.REGISTRATION_INDEX, "classpath:masterdata/templates/registration-index.txt",
				EmailTemplates.Keys.REGISTRATION_CONTACT, "classpath:masterdata/templates/registration-contact.txt");

		return new EmailTemplates(loader, templates);
	}

	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	ModelMapper modelMapper(List<MappingCustomizer> customizers, ApplicationContext context,
			ConversionService conversionService, List<IdentifierProcessor> processors) {

		Repositories repositories = new Repositories(context);

		var module = new RepositoryMappingModule(repositories, conversionService)
				.exclude(Role.class);

		processors.forEach(module::register);

		var mapper = new ModelMapper();
		mapper.registerModule(module);

		customizers.stream()
				.peek(it -> log.debug("Applying {} for model mapping.", it.getClass().getName()))
				.forEach(it -> it.customize(mapper));

		return mapper;
	}

	@Bean
	MessageSourceAccessor messageSourceAccessor(MessageSource source) {
		return new MessageSourceAccessor(source);
	}

	/**
	 * {@link Banner} implementation to include additional properties in the banner output.
	 *
	 * @author Oliver Drotbohm
	 */
	private static final class QuaranoBanner extends ResourceBanner {

		private final Properties properties;

		public QuaranoBanner(Properties properties) {

			super(new ClassPathResource("quarano-banner.txt"));

			this.properties = properties;
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.boot.ResourceBanner#getPropertyResolvers(org.springframework.core.env.Environment, java.lang.Class)
		 */
		@Override
		protected List<PropertyResolver> getPropertyResolvers(Environment environment, Class<?> sourceClass) {

			List<PropertyResolver> resolvers = super.getPropertyResolvers(environment, sourceClass);

			var mutablePropertySources = new MutablePropertySources();
			mutablePropertySources.addLast(new PropertiesPropertySource("git", properties));

			resolvers.add(new PropertySourcesPropertyResolver(mutablePropertySources));

			return resolvers;
		}
	}

	@Configuration
	@EnableScheduling
	@Profile("!integrationtest")
	static class SchedulingProperties {

	}
}
