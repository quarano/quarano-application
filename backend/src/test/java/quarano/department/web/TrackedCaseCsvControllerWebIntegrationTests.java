package quarano.department.web;

import static java.time.format.DateTimeFormatter.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import quarano.AbstractDocumentation;
import quarano.DocumentationFlow;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.test.web.servlet.ResultHandler;

/**
 * @author Jens Kutzsche
 * @author Oliver Drotbohm
 */
@QuaranoWebIntegrationTest
@WithQuaranoUser("admin")
class TrackedCaseCsvControllerWebIntegrationTests extends AbstractDocumentation {

	private static final String CSV_HEADER = Arrays
			.stream(TrackedCaseCsvRepresentations.CSV_ORDER)
			.collect(Collectors.joining(";"));

	@TestFactory
	Stream<DynamicTest> getQuarantineOrder() throws Exception {

		var now = LocalDate.now();

		var source = Stream.of(
				new Valid(null, null, null, 15, false),
				new Valid("index", null, null, 15, false),
				new Valid("contact", null, null, 0, false),
				new Valid("wrongType", null, null, 15, false),
				new Valid("index", now, null, 15, false),
				new Valid("index", now.plusDays(1), null, 0, false),
				new Valid("index", null, now, 15, false),
				new Valid("index", null, now.minusDays(1), 0, false),
				new Valid("index", now, now, 15, false),
				new Valid("index", now.minusDays(1), now.plusDays(1), 15, true),
				new Valid("index", now.plusDays(1), now.minusDays(1), 0, false));

		return DynamicTest.stream(source.iterator(), Object::toString, bar -> {

			var result = mvc.perform(get("/hd/quarantines")
					.contentType(MediaType.valueOf("text/csv"))
					.param("type", bar.getType())
					.param("from", bar.getFrom())
					.param("to", bar.getTo()))
					.andDo(documentGetQuarantineOrder(bar.isDocument()))
					.andExpect(status().is2xxSuccessful())
					.andExpect(header().string(HttpHeaders.CONTENT_TYPE, is("text/csv")))
					.andReturn().getResponse().getContentAsString();

			assertThat(result.lines()).size().isEqualTo(bar.getResultSize());

			if (bar.getResultSize() != 0) {
				assertThat(result.lines()).first().isEqualTo(CSV_HEADER);
			}
		});
	}

	@TestFactory
	Stream<DynamicTest> getQuarantineOrderWithErrors() throws Exception {

		var sources = Stream.of(
				new Invalid(null, LocalDate.now(), null),
				new Invalid(null, null, LocalDate.now()));

		return DynamicTest.stream(sources.iterator(), Object::toString, foo -> {

			mvc.perform(get("/hd/quarantines")
					.contentType(MediaType.valueOf("text/csv"))
					.param("type", foo.getType())
					.param("from", foo.getFrom())
					.param("to", foo.getTo()))
					.andExpect(status().is4xxClientError());
		});
	}

	private static ResultHandler documentGetQuarantineOrder(boolean createDocs) {

		return createDocs
				? DocumentationFlow.of("csv-import-export").document("quarantine-order")
				: result -> {};
	}

	@Getter
	@AllArgsConstructor
	private static class Invalid {

		@Nullable String type;
		@Nullable LocalDate from, to;

		@Nullable
		String getFrom() {
			return toNullableString(from);
		}

		@Nullable
		String getTo() {
			return toNullableString(to);
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return String.format("Get quarantine order for type %s from %s to %s", type, from, to);
		}

		@Nullable
		protected String toNullableString(@Nullable LocalDate date) {
			return date == null ? null : date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
		}
	}

	@Getter
	private static class Valid extends Invalid {

		int resultSize;
		boolean document;

		public Valid(@Nullable String type, @Nullable LocalDate from, @Nullable LocalDate to, int resultSize,
				boolean document) {

			super(type, from, to);

			this.resultSize = resultSize;
			this.document = document;
		}

		/*
		 * (non-Javadoc)
		 * @see quarano.department.web.TrackedCaseCsvControllerWebIntegrationTests.Foo#toNullableString(java.time.LocalDate)
		 */
		@Override
		@Nullable
		protected String toNullableString(@Nullable LocalDate date) {
			return date == null ? null : date.format(ISO_DATE);
		}
	}
}
