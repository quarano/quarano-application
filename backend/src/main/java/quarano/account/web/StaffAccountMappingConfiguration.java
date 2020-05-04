package quarano.account.web;

import quarano.account.Account;

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

	public StaffAccountMappingConfiguration(ModelMapper mapper) {

		mapper.getConfiguration().setMethodAccessLevel(AccessLevel.PACKAGE_PRIVATE);
		mapper.typeMap(Account.class, StaffAccountDto.class).addMappings(it -> {
			it.map(Account::getRoles, StaffAccountDto::setRoles);
		});
	}
}
