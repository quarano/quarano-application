package quarano.core;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Callback interface for components that shall be initialized on application startup.
 *
 * @author Oliver Drotbohm
 */
@Component
@Profile("!prod")
public interface DataInitializer {

	/**
	 * Called on application startup to trigger data initialization. Will run inside a transaction.
	 */
	@Transactional
	void initialize();
}
