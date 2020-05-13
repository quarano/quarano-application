package quarano.core.web;

import org.jddd.core.types.Identifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * @author Oliver Drotbohm
 */
@Configuration
class QuaranoWebConfiguration {

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
