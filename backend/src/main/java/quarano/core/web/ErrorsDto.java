package quarano.core.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

/**
 * @author Oliver Drotbohm
 */
@Slf4j
@RequiredArgsConstructor(staticName = "of")
public class ErrorsDto {

	private final Errors errors;
	private final MessageSourceAccessor messages;

	public boolean hasErrors() {
		return errors.hasErrors();
	}

	public ErrorsDto doWith(Consumer<Errors> errors) {
		errors.accept(this.errors);
		return this;
	}

	public ErrorsDto rejectField(boolean condition, String field, String errorCode) {
		return condition ? rejectField(field, errorCode) : this;
	}

	public ErrorsDto rejectField(String field, String errorCode) {
		errors.rejectValue(field, errorCode);
		return this;
	}

	public ErrorsDto rejectField(String field, String errorCode, String defaultMessage) {
		errors.rejectValue(field, errorCode, defaultMessage);
		return this;
	}

	public HttpEntity<?> toBadRequest() {
		return ResponseEntity.badRequest().body(this);
	}

	public HttpEntity<?> toBadRequestOrElse(Supplier<HttpEntity<?>> response) {
		return errors.hasErrors() ? ResponseEntity.badRequest().body(this) : response.get();
	}

	@JsonAnyGetter
	Map<String, String> toMap() {

		Map<String, String> fields = new HashMap<>();

		errors.getFieldErrors().forEach(it -> {

			try {
				fields.put(it.getField(), messages.getMessage(it));
			} catch (NoSuchMessageException o_O) {
				log.warn(o_O.getMessage());
			}
		});

		return fields;
	}
}
