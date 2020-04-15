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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.moduliths.docs.Documenter;
import org.moduliths.docs.Documenter.Options;
import org.moduliths.model.Modules;
import org.slf4j.LoggerFactory;

/**
 * @author Oliver Drotbohm
 */
public class QuaranoModuleTests {

	@Test
	void writeModuleDocumentation() throws IOException {

		Logger logger = (Logger) LoggerFactory.getLogger("com.tngtech.archunit");
		logger.setLevel(Level.WARN);

		Modules modules = Modules.of(Quarano.class);
		// modules.verify();

		Documenter documenter = new Documenter(modules);

		documenter.writeModuleCanvases(null);
		documenter.writeModulesAsPlantUml(Options.defaults());
	}
}
