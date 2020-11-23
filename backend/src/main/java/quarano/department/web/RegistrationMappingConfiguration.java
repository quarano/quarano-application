package quarano.department.web;

import quarano.account.Password.UnencryptedPassword;
import quarano.core.web.MappingCustomizer;
import quarano.department.RegistrationDetails;
import quarano.department.web.RegistrationRepresentations.RegistrationDto;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Customizations for {@link ModelMapper}.
 *
 * @author Patrick Otto
 */
@Component
class RegistrationMappingConfiguration implements MappingCustomizer {

	/*
	 * (non-Javadoc)
	 * @see quarano.core.web.MappingCustomizer#customize(org.modelmapper.ModelMapper)
	 */
	@Override
	public void customize(ModelMapper mapper) {

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
