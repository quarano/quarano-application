package quarano.auth;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.core.DataInitializer;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Create fix role setup on startup based on role enum {@link RoleType}
 *
 * @author Patrick Otto
 */
@Component
@Order(50)
@Slf4j
@RequiredArgsConstructor
class RoleDataInitializer implements DataInitializer {

	private final @NonNull RoleRepository roleRepository;

	/*
	 * (non-Javadoc)
	 * @see quarano.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		for (RoleType type : RoleType.values()) {

			var role = roleRepository.findByName(type.getCode());

			if (role != null) {
				continue;
			}

			log.info("Adding missing role " + type);
			roleRepository.save(new Role(type));
		}
	}
}
