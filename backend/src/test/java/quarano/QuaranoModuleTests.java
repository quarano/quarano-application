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
class QuaranoModuleTests {

	@Test
	void writeModuleDocumentation() throws IOException {

		Logger logger = (Logger) LoggerFactory.getLogger("com.tngtech.archunit");
		logger.setLevel(Level.WARN);

		Modules modules = Modules.of(Quarano.class);
		modules.verify();

		Documenter documenter = new Documenter(modules);

		documenter.writeModuleCanvases(null);
		documenter.writeModulesAsPlantUml(Options.defaults().withExclusions(it -> it.getName().equals("core")));
	}
}
