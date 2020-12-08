package quarano.department;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.vavr.api.VavrAssertions.assertThat;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import quarano.QuaranoIntegrationTest;
import quarano.department.TrackedCaseEmails.ResultHandlers;
import quarano.department.activation.ActivationCode;
import quarano.department.activation.ActivationCodeService;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.util.TestEmailServer;

import java.util.function.BiFunction;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;

/**
 * @author Oliver Drotbohm
 */
@QuaranoIntegrationTest
@RequiredArgsConstructor
public class TrackedCaseEmailsIntegrationTests {

	private final TrackedCaseEmails emails;
	private final TrackedCaseRepository cases;
	private final ActivationCodeService codes;
	private final TestEmailServer server;
	private final EntityManager em;

	@Test // CORE-585
	void addsCommentOnSuccessfulEmailSubmission() {

		var tanja = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1)
				.orElseThrow();

		var before = tanja.getComments().size();

		var result = emails.sendContactCaseInformationEmail(tanja, ResultHandlers.none());

		assertThat(result).isSuccess();
		server.assertEmailSent(__ -> {});
		assertThat(tanja.getComments()).hasSize(before + 1);
	}

	@Test // CORE-585
	void addsCommentOnSuccessfulEmailSubmission1() {

		var tanja = cases.findByTrackedPerson(TrackedPersonDataInitializer.VALID_TRACKED_PERSON1_ID_DEP1)
				.orElseThrow();
		var code = codes.createActivationCode(tanja.getTrackedPerson().getId(), tanja.getDepartment().getId()).get();
		var before = tanja.getComments().size();

		var result = emails.sendRegistrationEmail(tanja, code, ResultHandlers.none());

		assertThat(result).isSuccess();
		server.assertEmailSent(__ -> {});
		assertThat(tanja.getComments()).hasSize(before + 1);
	}

	@Value(staticConstructor = "of")
	static class Named {

		String name;
		BiFunction<TrackedCase, ActivationCode, Try<Void>> invocation;
	}
}
