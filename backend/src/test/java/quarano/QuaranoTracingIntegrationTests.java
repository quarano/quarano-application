/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.boot.test.context.assertj.AssertableApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration test to verify the setup of the tracing profile.
 *
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
@ActiveProfiles({ "tracing", "develop", "integrationtest" })
@QuaranoIntegrationTest
public class QuaranoTracingIntegrationTests {

	private final ConfigurableApplicationContext context;

	@Test
	void registersTracingInterceptor() {

		var assertable = AssertableApplicationContext.get(() -> context);

		assertThat(assertable).hasSingleBean(CustomizableTraceInterceptor.class);
	}
}
