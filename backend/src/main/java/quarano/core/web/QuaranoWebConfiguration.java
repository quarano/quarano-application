package quarano.core.web;

import lombok.RequiredArgsConstructor;

import org.jddd.core.types.Identifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * @author Oliver Drotbohm
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
class QuaranoWebConfiguration implements WebMvcConfigurer {

	@Bean
	ErrorsHttpMessageConverter errorsConverter(ObjectMapper jackson, MessageSourceAccessor messages) {
		return new ErrorsHttpMessageConverter(jackson, messages);
	}

	@Bean
	Module quaranoModule() {

		var module = new SimpleModule();
		module.setMixInAnnotation(Identifier.class, IdentifierMixin.class);
		module.addDeserializer(String.class, new EmptyStringDeserializer());

		return module;
	}

	@JsonSerialize(using = ToStringSerializer.class)
	static abstract class IdentifierMixin {}
}
