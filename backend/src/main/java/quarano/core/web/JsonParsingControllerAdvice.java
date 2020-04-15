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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

/**
 * @author Oliver Drotbohm
 */
@ControllerAdvice
@RequiredArgsConstructor
public class JsonParsingControllerAdvice {

	private final @NonNull MessageSourceAccessor messages;

	@ExceptionHandler
	public HttpEntity<?> handle(HttpMessageNotReadableException o_O) {

		var cause = o_O.getCause();

		if (InvalidFormatException.class.isInstance(cause)) {

			var invalidFormat = (InvalidFormatException) cause;

			Errors errors = new MapBindingResult(new HashMap<>(), "bean");

			String path = invalidFormat.getPath().stream() //
					.map(it -> it.getFieldName()) //
					.collect(Collectors.joining("."));

			errors.rejectValue(path, "Invalid." + path);

			return ResponseEntity.badRequest().body(ErrorsDto.of(errors, messages));
		}

		return ResponseEntity.badRequest().build();
	}
}
