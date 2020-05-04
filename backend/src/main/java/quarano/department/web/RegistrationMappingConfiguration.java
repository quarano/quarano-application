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
package quarano.department.web;

import quarano.account.Password.UnencryptedPassword;
import quarano.department.RegistrationDetails;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.stereotype.Component;

/**
 * Customizations for {@link ModelMapper}.
 *
 * @author Patrick Otto
 */
@Component
public class RegistrationMappingConfiguration {

	public RegistrationMappingConfiguration(ModelMapper mapper) {

		mapper.getConfiguration().setMethodAccessLevel(AccessLevel.PACKAGE_PRIVATE);

		mapper.addConverter(context -> context.getSource() == null ? null : UnencryptedPassword.of(context.getSource()),
				String.class, UnencryptedPassword.class);

		mapper.typeMap(RegistrationDto.class, RegistrationDetails.class).addMappings(it -> {

			it.map(RegistrationDto::getPassword, RegistrationDetails::setUnencryptedPassword);
			it.<UUID> map(RegistrationDto::getClientCode, (target, v) -> target.setActivationCodeLiteral(v));
			it.<UUID> map(RegistrationDto::getClientId,
					(target, v) -> target.setTrackedPersonId(TrackedPersonIdentifier.of(v)));
		});
	}
}
