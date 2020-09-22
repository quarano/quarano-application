package quarano.core.web;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

/**
 * A model object to allow replacing the error messages rendered for field errors with custom details.
 *
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
public class ErrorsWithDetails {

	private final Errors errors;
	private final Map<String, Object> details = new HashMap<>();

	/**
	 * Registers the given details object to be used for the given key, i.e. field name.
	 *
	 * @param key must not be {@literal null} or empty.
	 * @param details can be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public ErrorsWithDetails addDetails(String key, Object details) {

		Assert.hasText(key, "Key must not be null or empty!");

		this.details.put(key, details);

		return this;
	}

	@JsonAnyGetter
	public Map<String, Object> toMap() {

		Map<String, Object> fields = new HashMap<>();

		// Add field errors first
		errors.getFieldErrors().forEach(it -> {
			fields.put(it.getField(), it);
		});

		// Add and potentially override errors with custom details
		details.forEach((key, value) -> fields.put(key, value));

		return fields;
	}
}
