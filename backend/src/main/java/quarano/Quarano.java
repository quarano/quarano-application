package quarano;

import quarano.core.EmailTemplates;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Oliver Drotbohm
 */
@SpringBootApplication
@EnableJpaAuditing(dateTimeProviderRef = "quaranoDateTimeProvider")
@ConfigurationPropertiesScan
public class Quarano {

	public static void main(String... args) {
		SpringApplication.run(Quarano.class, args);
	}

	@Bean
	EmailTemplates emailTemplates(ResourceLoader loader) {

		var templates = Map.of(//
				EmailTemplates.Keys.REGISTRATION_INDEX, "classpath:masterdata/templates/registration-index.txt", //
				EmailTemplates.Keys.REGISTRATION_CONTACT, "classpath:masterdata/templates/registration-contact.txt");

		return new EmailTemplates(loader, templates);
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	MessageSourceAccessor messageSourceAccessor(MessageSource source) {
		return new MessageSourceAccessor(source);
	}

	@Configuration
	@EnableScheduling
	@Profile("!integrationtest")
	public static class SchedulingProperties {

	}
}
