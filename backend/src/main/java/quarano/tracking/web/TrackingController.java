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
package quarano.tracking.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.auth.web.LoggedIn;
import quarano.tracking.EmailAddress;
import quarano.tracking.PhoneNumber;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonRepository;
import quarano.tracking.ZipCode;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Oliver Drotbohm
 */
@RestController
@RequiredArgsConstructor
public class TrackingController {

	private final @NonNull TrackedPersonRepository repository;
	private final @NonNull ModelMapper mapper;

	@GetMapping({ "/api/details", "/api/enrollment/details" })
	HttpEntity<?> overview(@LoggedIn TrackedPerson person) {
		return ResponseEntity.ok(mapper.map(person, ClientDto.class));
	}

	@PutMapping("/api/details")
	public HttpEntity<?> createTrackedPerson(@Validated @RequestBody ClientDto dto, Errors errors,
			@LoggedIn TrackedPerson user) {

		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().build();
		}

		mapper.map(dto, user);
		repository.save(user);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/api/details/form")
	HttpEntity<?> trackedPersonForm() {

		var properties = Map.of("zipCode", Map.of("regex", ZipCode.REGEX.pattern()), //
				"mobilePhone", Map.of("regex", PhoneNumber.REGEX.pattern()), //
				"phone", Map.of("regex", PhoneNumber.REGEX.pattern()), //
				"email", Map.of("regex", EmailAddress.REGEX.pattern()));

		return ResponseEntity.ok(Map.of("properties", properties));
	}

	@Component
	static class ClientDtoValidator implements Validator {

		/*
		 * (non-Javadoc)
		 * @see org.springframework.validation.Validator#supports(java.lang.Class)
		 */
		@Override
		public boolean supports(Class<?> clazz) {
			return ClientDto.class.isAssignableFrom(clazz);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
		 */
		@Override
		public void validate(Object target, Errors errors) {

			ClientDto dto = (ClientDto) target;

			dto.validate(errors);
		}
	}

}
