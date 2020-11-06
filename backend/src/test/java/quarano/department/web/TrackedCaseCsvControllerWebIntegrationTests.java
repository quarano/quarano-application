package quarano.department.web;

import static java.time.format.DateTimeFormatter.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import quarano.AbstractDocumentation;
import quarano.DocumentationFlow;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultHandler;

/**
 * @author Jens Kutzsche
 */
@QuaranoWebIntegrationTest
@WithQuaranoUser("admin")
class TrackedCaseCsvControllerWebIntegrationTests extends AbstractDocumentation {

	private String header;

	@BeforeAll
	void beforeAll() {
		header = Arrays.stream(TrackedCaseCsvRepresentations.CSV_ORDER).collect(Collectors.joining(";"));
	}

	@ParameterizedTest(name = "{index} get quarantine order for type {0}; from {1}; to {2}")
	@MethodSource("quarantineParams")
	void getQuarantineOrder(String type, String from, String to, int resultSize, boolean createDoku) throws Exception {

		var result = performGetAndExpect200(type, from, to, createDoku);

		assertThat(result.lines()).size().isEqualTo(resultSize);

		if (resultSize != 0) {
			assertThat(result.lines()).first().isEqualTo(header);
		}
	}

	@ParameterizedTest(name = "{index} get quarantine order for type {0}; from {1}; to {2}")
	@MethodSource("wrongParams")
	void getQuarantineOrderWithErrors(String type, String from, String to) throws Exception {
		performGetAndExpect400(type, from, to);
	}

	private String performGetAndExpect200(String type, String from, String to, boolean createDoku)
			throws UnsupportedEncodingException, Exception {

		return mvc.perform(get("/hd/quarantines")
				.contentType(MediaType.valueOf("text/csv"))
				.param("type", type)
				.param("from", from)
				.param("to", to))
				.andDo(documentGetQuarantineOrder(createDoku))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();
	}

	private void performGetAndExpect400(String type, String from, String to)
			throws UnsupportedEncodingException, Exception {

		mvc.perform(get("/hd/quarantines")
				.contentType(MediaType.valueOf("text/csv"))
				.param("type", type)
				.param("from", from)
				.param("to", to))
				.andExpect(status().is4xxClientError());
	}

	private static ResultHandler documentGetQuarantineOrder(boolean createDocs) {

		return createDocs
				? DocumentationFlow.of("csv-import-export").document("quarantine-order")
				: result -> {};
	}

	private static Stream<Arguments> quarantineParams() {

		return Stream.of(
				arguments(null, null, null, 15, false),
				arguments("index", null, null, 15, false),
				arguments("contact", null, null, 0, false),
				arguments("wrongType", null, null, 15, false),
				arguments("index", LocalDate.now().format(ISO_DATE), null, 15, false),
				arguments("index", LocalDate.now().plusDays(1).format(ISO_DATE), null, 0, false),
				arguments("index", null, LocalDate.now().format(ISO_DATE), 15, false),
				arguments("index", null, LocalDate.now().minusDays(1).format(ISO_DATE), 0, false),
				arguments("index", LocalDate.now().format(ISO_DATE), LocalDate.now().format(ISO_DATE), 15, false),
				arguments("index", LocalDate.now().minusDays(1).format(ISO_DATE), LocalDate.now().plusDays(1).format(ISO_DATE),
						15, true),
				arguments("index", LocalDate.now().plusDays(1).format(ISO_DATE), LocalDate.now().minusDays(1).format(ISO_DATE),
						0, false));
	}

	private static Stream<Arguments> wrongParams() {

		return Stream.of(
				arguments(null, LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)), null),
				arguments(null, null, LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))));
	}
}
