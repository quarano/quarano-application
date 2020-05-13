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
