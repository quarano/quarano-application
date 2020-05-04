package quarano.auth.web;

import quarano.auth.Account;
import quarano.auth.Role;
import quarano.auth.RoleType;

import java.util.List;
import java.util.stream.Collectors;

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

			it. map( Account::getRoles, StaffAccountDto::setRoles);
		});
			
//			it.<List<Role>> map( Account::getRoles, 
//					(dest, v) -> dest.setRoles(	v.stream()//
//							.<RoleType>map( r -> RoleType.valueOf(r.toString()))
//							.collect(Collectors.toList())
//					));

	}
}
