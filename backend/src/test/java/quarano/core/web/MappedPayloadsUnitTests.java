package quarano.core.web;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

/**
 * Unit tests for {@link MappedPayloads}.
 *
 * @author Oliver Drotbohm
 */
@ExtendWith(MockitoExtension.class)
public class MappedPayloadsUnitTests {

	@Mock
	Errors errors;

	@Test
	void appliesErrorHandlerOnMappedError() {

		when(errors.hasErrors()).thenReturn(true);
		var reference = ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();

		assertThat(MappedPayloads.of(errors)
				.onErrors(it -> reference)
				.onValidGet(() -> ResponseEntity.ok().build()))
						.isEqualTo(reference);
	}
}
