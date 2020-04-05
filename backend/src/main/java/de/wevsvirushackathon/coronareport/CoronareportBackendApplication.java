package de.wevsvirushackathon.coronareport;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaRepositories
public class CoronareportBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoronareportBackendApplication.class, args);
	}
	
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	
	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}

}
