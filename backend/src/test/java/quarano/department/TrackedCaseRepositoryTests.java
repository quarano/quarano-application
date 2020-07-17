package quarano.department;

import static org.assertj.core.api.Assertions.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.account.DepartmentDataInitializer;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.data.util.Streamable;

/**
 * Integration tests for {@link TrackedCaseRepository}.
 *
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor
@QuaranoIntegrationTest
public class TrackedCaseRepositoryTests {

	private final TrackedCaseRepository cases;

	@Test // CORE-346
	void filtersIndexCasesByType() {

		Streamable<TrackedCase> result = cases.findFiltered(Optional.empty(), Optional.of("index"),
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1);

		assertThat(result).isNotEmpty();
		assertThat(result).allMatch(it -> it.getType().equals(CaseType.INDEX));
	}

	@Test // CORE-346
	void filtersContactCasesByType() {

		var contactCaseTypes = CaseType.CONTACT.getAllTypes();
		var result = cases.findFiltered(Optional.empty(), Optional.of("contact"),
				DepartmentDataInitializer.DEPARTMENT_ID_DEP1);

		assertThat(result).isNotEmpty();
		assertThat(result).allMatch(it -> contactCaseTypes.contains(it.getType()));
	}
}
