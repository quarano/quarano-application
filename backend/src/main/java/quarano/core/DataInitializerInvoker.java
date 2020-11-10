package quarano.core;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.aop.support.AopUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Internal component triggering the initialization of all {@link DataInitializer} instances registered in the
 * {@link ApplicationContext}.
 *
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!prod")
@Order(100)
class DataInitializerInvoker implements ApplicationRunner {

	private final @NonNull List<DataInitializer> initializers;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)
	 */
	@Override
	public void run(@Nullable ApplicationArguments args) throws Exception {

		initializers.stream()
				.peek(it -> log.info("Invoking " + AopUtils.getTargetClass(it)))
				.forEach(it -> {

					it.preInitialize();
					it.initialize();
					it.postInitialize();
				});
	}

	/**
	 * Fallback {@link DataInitializer} to make sure we always have a t least one {@link DataInitializer} in the
	 * Application context so that the autowiring of {@link DataInitializer} into {@link DataInitializerInvoker} works.
	 *
	 * @author Oliver Gierke
	 */
	@Component
	static class NoOpDataInitializer implements DataInitializer {

		/*
		 * (non-Javadoc)
		 * @see org.salespointframework.core.DataInitializer#initialize()
		 */
		@Override
		public void initialize() {}
	}
}
