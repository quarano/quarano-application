package quarano;

import static org.moduliths.docs.Documenter.CanvasOptions.Grouping.*;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import quarano.core.DataInitializer;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.moduliths.docs.Documenter;
import org.moduliths.docs.Documenter.CanvasOptions;
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

		var options = Options.defaults().withExclusions(it -> it.getName().equals("core"));

		var dataInitializers = of("Data initializers",
				"Components to initialize dummy data on application startup.",
				subtypeOf(DataInitializer.class));

		var representations = of("Representations",
				"Components to map from domain types to DTOs and vice versa.",
				nameMatching(".*Representations"));

		var documenter = new Documenter(modules)
				.writeModuleCanvases(CanvasOptions.defaults()
						.groupingBy(representations, dataInitializers));

		documenter.writeModulesAsPlantUml(options);
		modules.forEach(it -> documenter.writeModuleAsPlantUml(it, options));
	}
}
