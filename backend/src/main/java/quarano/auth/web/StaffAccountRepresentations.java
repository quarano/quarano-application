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
package quarano.auth.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.auth.Account;
import quarano.core.EnumMessageSourceResolvable;
import quarano.core.web.MapperWrapper;

import java.util.stream.Collectors;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/**
 * @author Patrick Otto
 */
@Component
@RequiredArgsConstructor
class StaffAccountRepresentations {

	private final @NonNull MapperWrapper mapper;
	private final MessageSourceAccessor messages;

	StaffAccountDto toRepresentation(Account account) {

		StaffAccountDto dto = new StaffAccountDto();
		dto = mapper.map(account, dto);

		dto.setRoles(account.getRoles().stream() //
				.map(x -> messages.getMessage(EnumMessageSourceResolvable.of(x.getRoleType()))) //
				.collect(Collectors.toList()));

		return dto;
	}

	// StaffAccountDto toInputRepresentation(TrackedCase trackedCase) {
	//
	// var personDto = mapper.map(trackedCase.getTrackedPerson(), TrackedPersonDto.class);
	// var caseDto = mapper.map(trackedCase, TrackedCaseDto.class);
	//
	// return mapper.map(personDto, caseDto);
	// }

	Account from(Account existing, StaffAccountDto source) {
		Account account =  mapper.map(source, existing);
		
		return account;
	}


	public Class StaffAccountInput() {
		
	}
}
