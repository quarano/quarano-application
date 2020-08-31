package quarano;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import quarano.account.Role;
import quarano.core.web.IdentifierProcessor;
import quarano.core.web.MappingCustomizer;
import quarano.core.web.RepositoryMappingModule;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

import org.modelmapper.ModelMapper;
import org.moduliths.Modulithic;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.Banner;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.support.Repositories;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.util.ReflectionUtils;

/**
 * @author Oliver Drotbohm
 */
@Slf4j
@Modulithic(sharedModules = "core")
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

	/**
	 * {@link BeanPostProcessor} to make sure package protected methods annotate with {@code Transactional}. We tweak the
	 * {@code publicMethodsOnly} flag inside {@link AnnotationTransactionAttributeSource} to {@literal false}.
	 *
	 * @author Oliver Drotbohm
	 */
	@Component
	private static final class PackageProtectedTransactionMethodsProcessor implements BeanPostProcessor, PriorityOrdered {

		private static final Field PUBLIC_METHODS_ONLY_FIELD;

		static {

			PUBLIC_METHODS_ONLY_FIELD = ReflectionUtils.findField(AnnotationTransactionAttributeSource.class,
					"publicMethodsOnly");

			if (PUBLIC_METHODS_ONLY_FIELD == null) {
				throw new IllegalStateException(
						"Could not find publicMethodsOnly field on AnnotationTransactionAttributeSource!");
			}

			ReflectionUtils.makeAccessible(PUBLIC_METHODS_ONLY_FIELD);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
		 */
		@Nullable
		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

			if (!AnnotationTransactionAttributeSource.class.isInstance(bean)) {
				return bean;
			}

			ReflectionUtils.setField(PUBLIC_METHODS_ONLY_FIELD, bean, false);

			return bean;
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.core.Ordered#getOrder()
		 */
		@Override
		public int getOrder() {
			return 100;
		}
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
		protected List<PropertyResolver> getPropertyResolvers(@Nullable Environment environment,
				@Nullable Class<?> sourceClass) {

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
	@EnableSchedulerLock(defaultLockAtMostFor = "PT2M")
	static class SchedulingProperties {

	}
}
