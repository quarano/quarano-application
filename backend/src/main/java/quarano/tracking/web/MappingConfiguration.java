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

import de.wevsvirushackathon.coronareport.client.Client;
import quarano.tracking.EmailAddress;
import quarano.tracking.PhoneNumber;
import quarano.tracking.TrackedPerson;
import quarano.tracking.ZipCode;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.stereotype.Component;

/**
 * Customizations for {@link ModelMapper}.
 *
 * @author Oliver Drotbohm
 */
@Component
public class MappingConfiguration {

	private static final Converter<String, EmailAddress> STRING_TO_EMAIL_ADDRESS //
			= source -> source == null ? null : EmailAddress.of(source.getSource());

	private static final Converter<String, PhoneNumber> STRING_TO_PHONE_NUMBER //
			= source -> source == null ? null : PhoneNumber.of(source.getSource());

	private static final Converter<String, ZipCode> STRING_TO_ZIP_CODE //
			= source -> source == null ? null : ZipCode.of(source.getSource());

	public MappingConfiguration(ModelMapper mapper) {

		mapper.getConfiguration().setMethodAccessLevel(AccessLevel.PACKAGE_PRIVATE);

		mapper.addConverter(STRING_TO_EMAIL_ADDRESS);
		mapper.addConverter(STRING_TO_PHONE_NUMBER);
		mapper.addConverter(STRING_TO_ZIP_CODE);

		mapper.typeMap(Client.class, TrackedPerson.class).setPreConverter(context -> {

			var source = context.getSource();
			return new TrackedPerson(source.getFirstname(), source.getSurename());

		}).addMappings(it -> {
			it.map(Client::getClientId, TrackedPerson::setLegacyClientId);
		});

		mapper.typeMap(TrackedPerson.class, ClientDto.class).addMappings(it -> {

			it.map(TrackedPerson::getLastname, ClientDto::setSurename);

			it.map(source -> source.getAddress().getStreet(), ClientDto::setStreet);
			it.map(source -> source.getAddress().getZipCode(), ClientDto::setZipCode);
			it.map(source -> source.getAddress().getCity(), ClientDto::setCity);
		});

		mapper.typeMap(ClientDto.class, TrackedPerson.class).addMappings(it -> {

			it.using(STRING_TO_PHONE_NUMBER).map(ClientDto::getMobilephone, TrackedPerson::setMobilePhoneNumber);
			it.using(STRING_TO_PHONE_NUMBER).map(ClientDto::getPhone, TrackedPerson::setPhoneNumber);
			it.using(STRING_TO_EMAIL_ADDRESS).map(ClientDto::getEmail, TrackedPerson::setEmailAddress);

			it.<String> map(ClientDto::getStreet, (target, v) -> target.getAddress().setStreet(v));
			it.<String> map(ClientDto::getCity, (target, v) -> target.getAddress().setCity(v));
			it.<ZipCode> map(ClientDto::getZipCode, (target, v) -> target.getAddress().setZipCode(v));
		});
	}
}
