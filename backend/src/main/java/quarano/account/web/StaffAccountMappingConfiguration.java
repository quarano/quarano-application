package quarano.account.web;

import lombok.RequiredArgsConstructor;
import quarano.account.Role;
import quarano.account.RoleRepository;
import quarano.core.web.MappingCustomizer;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Customizations for {@link ModelMapper}.
 *
 * @author Patrick Otto
 */
@Component
@RequiredArgsConstructor
class StaffAccountMappingConfiguration implements MappingCustomizer {

	private final RoleRepository roles;

	/*
	 * (non-Javadoc)
	 * @see quarano.core.web.MappingCustomizer#customize(org.modelmapper.ModelMapper)
	 */
	@Override
	public void customize(ModelMapper mapper) {

		mapper.addConverter(context -> roles.findByName(context.getSource()).orElse(null), String.class, Role.class);
		mapper.addConverter(context -> context.getSource().getRoleType().getCode(), Role.class, String.class);
	}
}
