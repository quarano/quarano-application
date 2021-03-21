package quarano.tracking.web;

import quarano.core.Address;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.core.web.MappingCustomizer;
import quarano.core.Address.HouseNumber;
import quarano.tracking.BodyTemperature;
import quarano.tracking.ContactPerson;
import quarano.tracking.ContactWays;
import quarano.tracking.Location;
import quarano.tracking.TrackedPerson;
import quarano.core.ZipCode;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Customizations for {@link ModelMapper}.
 *
 * @author Oliver Drotbohm
 */
@Component
@Order(10)
public class TrackingMappingConfiguration implements MappingCustomizer {

	private static final Converter<String, EmailAddress> STRING_TO_EMAIL_ADDRESS
			= source -> EmailAddress.ofNullable(source.getSource());

	private static final Converter<String, PhoneNumber> STRING_TO_PHONE_NUMBER
			= source -> PhoneNumber.ofNullable(source.getSource());

	private static final Converter<String, ZipCode> STRING_TO_ZIP_CODE
			= source -> source.getSource() == null ? null : ZipCode.of(source.getSource());

	private static final Converter<String, HouseNumber> STRING_TO_HOUSE_NUMBER
			= source -> HouseNumber.of(source.getSource());

	private static final Converter<Float, BodyTemperature> FLOAT_TO_BODY_TEMPERATURE
			= source -> source.getSource() == null ? null : BodyTemperature.of(source.getSource());

	private static final Converter<BodyTemperature, Float> BODY_TEMPERATURE_TO_FLOAT
			= source -> source.getSource() == null ? null : source.getSource().getValue();

	/*
	 * (non-Javadoc)
	 * @see quarano.core.web.MappingCustomizer#customize(org.modelmapper.ModelMapper)
	 */
	@Override
	public void customize(ModelMapper mapper) {

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

			return ContactWays.builder()
					.emailAddress(EmailAddress.ofNullable(source.getEmail()))
					.phoneNumber(PhoneNumber.ofNullable(source.getPhone()))
					.mobilePhoneNumber(PhoneNumber.ofNullable(source.getMobilePhone()))
					.identificationHint(source.getIdentificationHint())
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

			it.using(STRING_TO_PHONE_NUMBER).map(TrackedPersonDto::getMobilePhone, TrackedPerson::setMobilePhoneNumber);
			it.using(STRING_TO_PHONE_NUMBER).map(TrackedPersonDto::getPhone, TrackedPerson::setPhoneNumber);
			it.using(STRING_TO_EMAIL_ADDRESS).map(TrackedPersonDto::getEmail, TrackedPerson::setEmailAddress);
			it.using(STRING_TO_HOUSE_NUMBER).<HouseNumber> map(TrackedPersonDto::getHouseNumber,
					(target, v) -> target.getAddress().setHouseNumber(v));

			it.<String> map(TrackedPersonDto::getStreet, (target, v) -> target.getAddress().setStreet(v));
			it.<String> map(TrackedPersonDto::getCity, (target, v) -> target.getAddress().setCity(v));
			it.<ZipCode> map(TrackedPersonDto::getZipCode, (target, v) -> target.getAddress().setZipCode(v));
		});

		//Location

		mapper.typeMap(LocationDto.class, Location.class).setProvider(request -> {

			var dto = (LocationDto) request.getSource();
			Location location = new Location(dto.getName());
			if(dto.getContactPerson() != null){
				location.setContactPersonName(dto.getContactPerson().getContactPersonName());
				location.setContactPersonEmail(EmailAddress.ofNullable(dto.getContactPerson().getContactPersonEmail()));
				location.setContactPersonPhone(PhoneNumber.ofNullable(dto.getContactPerson().getContactPersonPhone()));
			}
			location.setComment(dto.getComment());

			return location;

		}).addMappings(it -> {

			it.<String> map(LocationDto::getStreet, (target, v) -> target.getAddress().setStreet(v));
			it.<HouseNumber> map(LocationDto::getHouseNumber, (target, v) -> target.getAddress().setHouseNumber(v));
			it.<String> map(LocationDto::getCity, (target, v) -> target.getAddress().setCity(v));
			it.<ZipCode> map(LocationDto::getZipCode, (target, v) -> target.getAddress().setZipCode(v));
		});

		mapper.typeMap(Location.class, LocationDto.class).setPreConverter(context -> {

			LocationDto destination = context.getDestination();
			Location location = context.getSource();
			destination.setName(location.getName());
			destination.setComment(location.getComment());

			LocationDto.LocationContactDto contactPersonDto = new LocationDto.LocationContactDto();
			contactPersonDto.setContactPersonName(location.getContactPersonName());
			if(location.getContactPersonEmail() != null){
				contactPersonDto.setContactPersonEmail(location.getContactPersonEmail().toString());
			}

			if(location.getContactPersonPhone() != null){
				contactPersonDto.setContactPersonPhone(location.getContactPersonPhone().toString());
			}
			destination.setContactPerson(contactPersonDto);

			return destination;

		}).addMappings(it -> {

			it.map(source -> source.getAddress().getStreet(), LocationDto::setStreet);
			it.map(source -> source.getAddress().getZipCode(), LocationDto::setZipCode);
			it.map(source -> source.getAddress().getCity(), LocationDto::setCity);
			it.map(source -> source.getAddress().getHouseNumber(), LocationDto::setHouseNumber);
		});

	}
}
