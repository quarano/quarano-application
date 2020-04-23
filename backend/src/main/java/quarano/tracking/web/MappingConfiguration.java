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

import quarano.department.TrackedCase;
import quarano.department.web.TrackedCaseDto;
import quarano.reference.Symptom;
import quarano.reference.SymptomRepository;
import quarano.tracking.*;
import quarano.tracking.Address.HouseNumber;
import quarano.tracking.web.DiaryRepresentations.DiaryEntryInput;

import java.util.ArrayList;
import java.util.List;

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
			= source -> EmailAddress.ofNullable(source.getSource());

	private static final Converter<String, PhoneNumber> STRING_TO_PHONE_NUMBER //
			= source -> PhoneNumber.ofNullable(source.getSource());

	private static final Converter<String, ZipCode> STRING_TO_ZIP_CODE //
			= source -> source.getSource() == null ? null : ZipCode.of(source.getSource());

	private static final Converter<String, HouseNumber> STRING_TO_HOUSE_NUMBER //
			= source -> HouseNumber.of(source.getSource());

	private static final Converter<Float, BodyTemperature> FLOAT_TO_BODY_TEMPERATURE //
			= source -> source.getSource() == null ? null : BodyTemperature.of(source.getSource());

	private static final Converter<BodyTemperature, Float> BODY_TEMPERATURE_TO_FLOAT //
			= source -> source.getSource() == null ? null : source.getSource().getValue();

	public MappingConfiguration(ModelMapper mapper, SymptomRepository symptoms, ContactPersonRepository contacts) {

		mapper.getConfiguration().setMethodAccessLevel(AccessLevel.PACKAGE_PRIVATE);
		mapper.getConfiguration().setCollectionsMergeEnabled(false);

		mapper.addConverter(STRING_TO_EMAIL_ADDRESS, String.class, EmailAddress.class);
		mapper.addConverter(STRING_TO_PHONE_NUMBER, String.class, PhoneNumber.class);
		mapper.addConverter(STRING_TO_ZIP_CODE, String.class, ZipCode.class);
		mapper.addConverter(STRING_TO_HOUSE_NUMBER, String.class, HouseNumber.class);

		mapper.addConverter(BODY_TEMPERATURE_TO_FLOAT, BodyTemperature.class, float.class);
		mapper.addConverter(FLOAT_TO_BODY_TEMPERATURE, float.class, BodyTemperature.class);

		// ContactPerson

		mapper.typeMap(ContactPersonDto.class, ContactWays.class).setProvider(request -> {

			var source = (ContactPersonDto) request.getSource();

			return ContactWays.builder() //
					.emailAddress(EmailAddress.ofNullable(source.getEmail())) //
					.phoneNumber(PhoneNumber.ofNullable(source.getPhone())) //
					.mobilePhoneNumber(PhoneNumber.ofNullable(source.getMobilePhone())) //
					.identificationHint(source.getIdentificationHint()) //
					.build();
		});

		mapper.typeMap(ContactPersonDto.class, ContactPerson.class).setProvider(request -> {

			var dto = (ContactPersonDto) request.getSource();
			var contactWays = mapper.map(dto, ContactWays.class);

			return new ContactPerson(dto.getFirstName(), dto.getLastName(), contactWays);

		}).setPreConverter(it -> {

			return it.getDestination().contactWays(mapper.map(it.getSource(), ContactWays.class));

		}).addMappings(it -> {

			it.<String> map(ContactPersonDto::getStreet, (target, v) -> target.getAddress().setStreet(v));
			it.<HouseNumber> map(ContactPersonDto::getHouseNumber, (target, v) -> target.getAddress().setHouseNumber(v));
			it.<String> map(ContactPersonDto::getCity, (target, v) -> target.getAddress().setCity(v));
			it.<ZipCode> map(ContactPersonDto::getZipCode, (target, v) -> target.getAddress().setZipCode(v));
		});

		mapper.typeMap(ContactPerson.class, ContactPersonDto.class).addMappings(it -> {

			it.map(source -> source.getAddress().getStreet(), ContactPersonDto::setStreet);
			it.map(source -> source.getAddress().getZipCode(), ContactPersonDto::setZipCode);
			it.map(source -> source.getAddress().getCity(), ContactPersonDto::setCity);
			it.map(source -> source.getAddress().getHouseNumber(), ContactPersonDto::setHouseNumber);
		});

		// TrackedPerson

		mapper.typeMap(TrackedPerson.class, TrackedPersonDto.class).setPreConverter(context -> {

			TrackedPersonDto destination = context.getDestination();
			TrackedPerson person = context.getSource();

			var houseNumber = person.getAddress().getHouseNumber();

			if (!houseNumber.equals(HouseNumber.NONE)) {
				destination.setHouseNumber(houseNumber.toString());
			}

			return destination;

		}).addMappings(it -> {

			it.map(source -> source.getAddress().getStreet(), TrackedPersonDto::setStreet);
			it.map(source -> source.getAddress().getZipCode(), TrackedPersonDto::setZipCode);
			it.map(source -> source.getAddress().getCity(), TrackedPersonDto::setCity);

			it.skip(TrackedPersonDto::setHouseNumber);
		});

		mapper.typeMap(TrackedPersonDto.class, TrackedPerson.class).setProvider(it -> {

			var source = (TrackedPersonDto) it.getSource();

			return new TrackedPerson(source.getFirstName(), source.getLastName());

		}).addMappings(it -> {

			it.skip(TrackedPerson::setFirstName);
			it.skip(TrackedPerson::setLastName);

			it.using(STRING_TO_PHONE_NUMBER).map(TrackedPersonDto::getMobilePhone, TrackedPerson::setMobilePhoneNumber);
			it.using(STRING_TO_PHONE_NUMBER).map(TrackedPersonDto::getPhone, TrackedPerson::setPhoneNumber);
			it.using(STRING_TO_EMAIL_ADDRESS).map(TrackedPersonDto::getEmail, TrackedPerson::setEmailAddress);
			it.using(STRING_TO_HOUSE_NUMBER).<HouseNumber> map(TrackedPersonDto::getHouseNumber,
					(target, v) -> target.getAddress().setHouseNumber(v));

			it.<String> map(TrackedPersonDto::getStreet, (target, v) -> target.getAddress().setStreet(v));
			it.<String> map(TrackedPersonDto::getCity, (target, v) -> target.getAddress().setCity(v));
			it.<ZipCode> map(TrackedPersonDto::getZipCode, (target, v) -> target.getAddress().setZipCode(v));
		});

		// DiaryEntry

		mapper.typeMap(DiaryEntryInput.class, DiaryEntry.class).setProvider(request -> {
			var dto = (DiaryEntryInput) request.getSource();

			return DiaryEntry.of(dto.getSlot());

		}).addMappings(it -> {
			it.with(request -> new ArrayList<>()).<List<Symptom>> map(DiaryEntryInput::getSymptoms,
					(target, v) -> target.setSymptoms(v));
		});

		// TrackedCase

		mapper.typeMap(TrackedCase.class, TrackedCaseDto.class).setPreConverter(it -> {

			var source = it.getSource();
			var target = it.getDestination();
			var quarantine = source.getQuarantine();

			if (quarantine != null) {
				target.setQuarantineStartDate(quarantine.getFrom());
				target.setQuarantineEndDate(quarantine.getTo());
			}

			return target;

		}).addMappings(it -> {
			it.skip(TrackedCaseDto::setQuarantineStartDate);
			it.skip(TrackedCaseDto::setQuarantineEndDate);
		});

		mapper.typeMap(TrackedCaseDto.class, TrackedCase.class).setPreConverter(it -> {

			var source = it.getSource();
			var target = it.getDestination();

			return target.setQuarantine(Quarantine.of(source.getQuarantineStartDate(), source.getQuarantineEndDate()));
		});
	}
}
