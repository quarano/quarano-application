package quarano.account.web;

import quarano.account.Account;
import quarano.account.Role;
import quarano.account.RoleRepository;
import quarano.account.web.StaffAccountRepresentations.StaffAccountSummaryDto;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.spi.MappingContext;
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
		mapper.typeMap(Account.class, StaffAccountSummaryDto.class).addMappings(it -> {
			it.map(Account::getRoles, StaffAccountSummaryDto::setRoles);
		});
		
		mapper.addConverter(new Converter<String, Role>() {
			@Override
			public Role convert(MappingContext<String, Role> context) {
				return roles.findByName(context.getSource());
			}
			
		});
		

	}

}
