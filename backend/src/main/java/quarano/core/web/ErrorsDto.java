/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano.core.web;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

/**
 * @author Oliver Drotbohm
 */
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
			fields.put(it.getField(), messages.getMessage(it));
		});

		return fields;
	}
}
