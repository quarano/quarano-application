package quarano.core;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Callback interface for components that shall be initialized on application startup.
 *
 * @author Oliver Drotbohm
 */
@Component
public interface DataInitializer {

	default void preInitialize() {}

	/**
	 * Called on application startup to trigger data initialization. Will run inside a transaction.
	 */
	@Transactional
	void initialize();

	default void postInitialize() {}
}
