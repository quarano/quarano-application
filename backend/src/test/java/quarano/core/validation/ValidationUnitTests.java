package quarano.core.validation;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
@QuaranoIntegrationTest
public class ValidationUnitTests {

	private final Validator validator;

	@Test
	void emailValidationIsCaseInsensitive() {

		var sample = new Sample();
		sample.email = "max@Mustermann.de";

		var errors = new BeanPropertyBindingResult(sample, "sample");

		validator.validate(sample, errors);

		assertThat(errors.hasErrors()).isFalse();
	}

	static class Sample {
		@Email String email;
	}
}
