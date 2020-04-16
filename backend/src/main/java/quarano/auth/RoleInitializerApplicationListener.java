package quarano.auth;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
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
class RoleInitializerApplicationListener implements ApplicationListener<ApplicationStartedEvent> {

	private final @NonNull RoleRepository roleRepository;

	/**
	 * adds each role defined in enum {@link RoleType} that is not existing in the database
	 */
	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {

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
