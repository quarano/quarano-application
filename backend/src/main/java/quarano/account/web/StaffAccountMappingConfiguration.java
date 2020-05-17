package quarano.account.web;

import quarano.account.Role;
import quarano.account.RoleRepository;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.stereotype.Component;

/**
 * Customizations for {@link ModelMapper}.
 *
 * @author Patrick Otto
 */
@Component
public class StaffAccountMappingConfiguration {

	public StaffAccountMappingConfiguration(ModelMapper mapper, RoleRepository roles) {

		mapper.getConfiguration().setMethodAccessLevel(AccessLevel.PACKAGE_PRIVATE);

		mapper.addConverter(context -> roles.findByName(context.getSource()), String.class, Role.class);
		mapper.addConverter(context -> context.getSource().getRoleType().getCode(), Role.class, String.class);
	}
}
