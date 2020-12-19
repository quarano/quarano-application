package quarano.department.web;

import static org.springframework.util.ObjectUtils.*;
import static quarano.department.TrackedCaseQuerydslFilters.IncludeFilterOptions.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Department;
import quarano.core.AuditingMetadata;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.core.web.LoggedIn;
import quarano.department.CaseType;
import quarano.department.TestResult;
import quarano.department.TrackedCase;
import quarano.department.TrackedCase.Status;
import quarano.department.TrackedCase.TrackedCaseIdentifier;
import quarano.department.TrackedCaseRepository;
import quarano.department.rki.HealthDepartments;
import quarano.department.rki.HealthDepartments.HealthDepartment;
import quarano.tracking.Address;
import quarano.tracking.Address.HouseNumber;
import quarano.tracking.Quarantine;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;
import quarano.tracking.ZipCode;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jens Kutzsche
 */
@RestController
@RequiredArgsConstructor
class TrackedCaseCsvController {

	private final @NonNull TrackedCaseRepository caseRepo;
	private final @NonNull TrackedCaseCsvRepresentations representations;
	private final @NonNull HealthDepartments healthDepartments;
	private final @NonNull MessageSourceAccessor messages;

	/**
	 * <p>
	 * After the login as staff of the health department, the cases with a currently running quarantine can be exported.
	 * This is mainly intended for creating quarantine orders.
	 * </p>
	 * <p>
	 * The quantity of exported cases can be limited by the type and date of the last quarantine modification. For
	 * example, only quarantines created or modified in the last days can be exported.
	 * </p>
	 *
	 * @param department
	 * @param query
	 * @param response
	 * @throws IOException
	 */
	@PostMapping(path = "/hd/export/quarantines", produces = "text/csv")
	public void getQuarantineOrder(@LoggedIn Department department, @RequestBody QuarantineOrderQuery query,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/csv;charset=UTF-8");

		var cases = caseRepo.findFilteredByQuarantine(query.getFrom(), query.getTo(), query.getType(), department.getId());

		representations.writeQuarantineOrderCsv(response.getWriter(), cases.stream());
	}

	/**
	 * <p>
	 * After the login as staff of the health department, the cases with a currently running quarantine can be exported.
	 * This is mainly intended for creating quarantine orders.
	 * </p>
	 * <p>
	 * The cases to be exported must be specified using a list of their Self links.
	 * </p>
	 * 
	 * @param department
	 * @param selfUris The list of Self links whose cases should be exported.
	 * @param response
	 * @throws IOException
	 * @since 1.4
	 */
	@PostMapping(path = "/hd/export/quarantines/by-ids", produces = "text/csv")
	public void getQuarantineOrderByIds(@LoggedIn Department department, @RequestBody List<String> selfUris,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/csv;charset=UTF-8");

		var idList = selfUris.stream()
				.map(this::extractedUUID)
				.map(UUID::fromString)
				.map(TrackedCaseIdentifier::of)
				.collect(Collectors.toList());

		var cases = caseRepo.findAllById(idList);

		representations.writeQuarantineOrderCsv(response.getWriter(), StreamSupport.stream(cases.spliterator(), false));
	}

	/**
	 * <p>
	 * After the login as staff of the health department, the cases can be exported in the import format of Sormas to
	 * import them there.
	 * </p>
	 * <p>
	 * The quantity of exported cases can be limited by date of the creation. For example, only cases created in the last
	 * days can be exported. It can also be determined whether cases with Sormas ID (from an import) are also exported or
	 * not. The selection of the type (Index or Contact) is mandatory!
	 * </p>
	 *
	 * @param department
	 * @param query
	 * @param response
	 * @throws IOException
	 * @since 1.4
	 */
	@PostMapping(path = "/hd/export/sormas", produces = "text/csv")
	public void getCasesForSormas(@LoggedIn Department department, @Validated @RequestBody SormasQuery query,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/csv;charset=UTF-8");

		var sormasId = query.getOnlyWithoutSormasId().map(it -> it ? EXCLUDE_ONLY : IRRELEVANT).orElse(IRRELEVANT);

		var cases = caseRepo.findFiltered(query.getFrom(), query.getTo(), Optional.empty(), Optional.empty(),
				sormasId, EXCLUDE_ONLY, Optional.of(query.getType()), Optional.empty(), department.getId());

		representations.writeSormasCsv(response.getWriter(), cases.stream(), query.getType());
	}

	/**
	 * <p>
	 * After the login as staff of the health department, the cases can be exported in the import format of Sormas to
	 * import them there.
	 * </p>
	 * <p>
	 * The cases to be exported must be specified using a list of their Self links.
	 * </p>
	 * 
	 * @param department
	 * @param selfUris The list of Self links whose cases should be exported. All cases must be of the same type (index or
	 *          contact).
	 * @param response
	 * @throws IOException
	 * @since 1.4
	 */
	@PostMapping(path = "/hd/export/sormas/by-ids", produces = "text/csv")
	public void getCasesForSormasByIds(@LoggedIn Department department, @RequestBody List<String> selfUris,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/csv;charset=UTF-8");

		var idList = selfUris.stream()
				.map(this::extractedUUID)
				.map(UUID::fromString)
				.map(TrackedCaseIdentifier::of)
				.collect(Collectors.toList());

		var cases = caseRepo.findAllById(idList);

		var differentTypes = StreamSupport.stream(cases.spliterator(), false)
				.collect(Collectors.groupingBy(it -> it.getType().getPrimaryCaseType()))
				.keySet();

		if (differentTypes.size() > 1) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), messages.getMessage("csv.export.sormas.notUniqueType"));
		}

		var caseType = differentTypes.stream().findAny();
		if (caseType.isPresent()) {
			representations.writeSormasCsv(response.getWriter(), StreamSupport.stream(cases.spliterator(), false),
					caseType.get());
		}
	}

	/**
	 * <p>
	 * After the login as staff of the health department, the external cases (status = EXTERNAL_ZIP) can be exported in a
	 * Quarano own CSV format. This is mainly intended to inform outer health departments and to import this cases if this
	 * department use also Quarano.
	 * </p>
	 * <p>
	 * The quantity of exported cases can be limited by the date of the last modification. For example, only cases created
	 * or modified in the last days can be exported.
	 * </p>
	 * <p>
	 * For each office for which there are external cases in the return, a separate CSV is created, which is returned
	 * together with information about the office.
	 * </p>
	 * 
	 * @param department
	 * @param query
	 * @return
	 * @throws IOException
	 * @since 1.4
	 */
	@PostMapping(path = "/hd/export/externalcases")
	public RepresentationModel<?> getExternalCases(@LoggedIn Department department, @RequestBody ExternalCasesQuery query)
			throws IOException {

		var cases = caseRepo.findFiltered(Optional.empty(), Optional.empty(), query.getFrom(), query.getTo(),
				IRRELEVANT, INCLUDE_ONLY, Optional.empty(), Optional.empty(), department.getId());

		var groups = cases.stream().collect(Collectors.groupingBy(this::determineHealthDepartment));

		var departmentGroups = groups.entrySet().stream()
				.map(it -> getDepartmentGroupForEntry(it, true));

		return HalModelBuilder.emptyHalModel()
				.embed(departmentGroups, DepartmentGroup.class)
				.build();
	}

	/**
	 * <p>
	 * After the login as staff of the health department, the cases can be exported in a Quarano own CSV format. This is
	 * intended e.g. for third-party applications and also Excel evaluations.
	 * </p>
	 * <p>
	 * The quantity of exported cases can be limited by type, status and date of the creation of a case. For example, only
	 * cases created in the last days can be exported. It can also be determined whether external cases are also exported
	 * or not.
	 * </p>
	 * <p>
	 * It can be specified whether the CSV contains data on the original case or not.
	 * </p>
	 * 
	 * @param department
	 * @param query
	 * @param withOriginCase True includes data on the original case and false does not do this.
	 * @param response
	 * @throws IOException
	 * @since 1.4
	 */
	@PostMapping(path = "/hd/export/cases", produces = "text/csv")
	public void getCases(@LoggedIn Department department, @RequestBody CasesQuery query,
			@RequestParam("withorigincase") Optional<Boolean> withOriginCase, HttpServletResponse response)
			throws IOException {

		response.setContentType("text/csv;charset=UTF-8");

		var externalCases = query.getCasesRealm().map(it -> {
			switch (it) {
				case EXTERNAL:
					return INCLUDE_ONLY;
				case INTERNAL:
					return EXCLUDE_ONLY;
				default:
					return IRRELEVANT;
			}
		}).orElse(EXCLUDE_ONLY);

		var cases = caseRepo.findFiltered(query.getFrom(), query.getTo(), Optional.empty(), Optional.empty(),
				IRRELEVANT, externalCases, query.getType(), query.getStatus(), department.getId());

		representations.writeCaseTransferCsv(response.getWriter(), cases.stream(), withOriginCase.orElse(Boolean.FALSE));
	}

	/**
	 * <p>
	 * After the login as staff of the health department, the cases can be exported in a Quarano own CSV format. This is
	 * intended e.g. for third-party applications and also Excel evaluations.
	 * </p>
	 * <p>
	 * The cases to be exported must be specified using a list of their Self links.
	 * </p>
	 * <p>
	 * It can be specified whether the CSV contains data on the original case or not.
	 * </p>
	 * 
	 * @param department
	 * @param selfUris The list of Self links whose cases should be exported.
	 * @param withOriginCase True includes data on the original case and false does not do this.
	 * @param response
	 * @throws IOException
	 * @since 1.4
	 */
	@PostMapping(path = "/hd/export/cases/by-ids", produces = "text/csv")
	public void getCasesByIds(@LoggedIn Department department, @RequestBody List<String> selfUris,
			@RequestParam("withorigincase") Optional<Boolean> withOriginCase, HttpServletResponse response)
			throws IOException {

		response.setContentType("text/csv;charset=UTF-8");

		var idList = selfUris.stream()
				.map(this::extractedUUID)
				.map(UUID::fromString)
				.map(TrackedCaseIdentifier::of)
				.collect(Collectors.toList());

		var cases = caseRepo.findAllById(idList);

		representations.writeCaseTransferCsv(response.getWriter(), StreamSupport.stream(cases.spliterator(), false),
				withOriginCase.orElse(Boolean.FALSE));
	}

	/**
	 * <p>
	 * After the login as administrator, a template of the importable CSV format can be exported.
	 * </p>
	 * 
	 * @param response
	 * @throws IOException
	 * @since 1.4
	 */
	@PostMapping(path = "/admin/cases/template", produces = "text/csv")
	public void getTemplate(HttpServletResponse response) throws IOException {

		response.setContentType("text/csv;charset=UTF-8");

		representations.writeCaseTransferCsv(response.getWriter(), createExcampleCase(), false);
	}

	private String extractedUUID(String uri) {
		return uri.substring(uri.lastIndexOf("/") + 1);
	}

	private Optional<HealthDepartment> determineHealthDepartment(TrackedCase trackedCase) {

		var address = trackedCase.getTrackedPerson().getAddress();

		if (address == null || address.getZipCode() == null) {
			return Optional.empty();
		}

		return healthDepartments.findDepartmentWithExact(address.getZipCode().toString());
	}

	private DepartmentGroup getDepartmentGroupForEntry(Entry<Optional<HealthDepartment>, List<TrackedCase>> it,
			boolean withOriginCase) {

		var writer = new StringWriter();

		representations.writeCaseTransferCsv(writer, it.getValue().stream(), withOriginCase);

		return it.getKey().map(hd -> {

			return DepartmentGroup.of(hd.getCode(),
					hd.getName() + " - " + hd.getDepartment(),
					hd.getCovid19EMail() != null
							? hd.getCovid19EMail().toString()
							: nullSafeToString(hd.getEmail()),
					writer.toString());

		}).orElseGet(() -> DepartmentGroup.of(null, null, null, writer.toString()));
	}

	private Stream<TrackedCase> createExcampleCase() {
		var address = new Address("Street", HouseNumber.of("1"), "Meißen", ZipCode.of("01665"));
		var person = new TrackedPerson(TrackedPersonIdentifier.of(UUID.randomUUID()), "Max", "Muster",
				EmailAddress.of("max@muster.de"), PhoneNumber.of("0123 456789"),
				LocalDate.now())
						.setAddress(address)
						.setMobilePhoneNumber(PhoneNumber.of("9876 543210"))
						.setLocale(Locale.GERMANY);

		var department = new Department("HD", "1.1.1.1.1.", "State", "District");

		TrackedCase trackedCase = new TrackedCase(person, CaseType.INDEX, department);
		trackedCase.addOriginCase(new TrackedCase(person, CaseType.INDEX, department));
		trackedCase.setQuarantine(Quarantine.of(LocalDate.now().minusDays(7), LocalDate.now().plusDays(7)));
		trackedCase.report(TestResult.infected(LocalDate.now().minusDays(4)));

		try {
			var lastModField = AuditingMetadata.class.getDeclaredField("lastModified");
			lastModField.setAccessible(true);
			lastModField.set(trackedCase.getMetadata(), LocalDateTime.now());
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}

		return Stream.of(trackedCase);
	}

	@Getter
	@AllArgsConstructor
	static class QuarantineOrderQuery {

		/**
		 * The date (YYYY-MM-DD) of the last quarantine modification from which (inclusive) the cases should be included in
		 * the export.
		 */
		private @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> from;

		/**
		 * The date (YYYY-MM-DD) of the last quarantine modification up to which (inclusive) the cases should be included in
		 * the export.
		 */
		private @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> to;

		/**
		 * The case type of the cases to be exported. CONTACT includes all types of contact.
		 */
		private Optional<CaseType> type;
	}

	@Getter
	@AllArgsConstructor
	static class SormasQuery {

		/**
		 * The case type of the cases to be exported. CONTACT includes all types of contact.
		 */
		private @NotNull CaseType type;

		/**
		 * The date (YYYY-MM-DD) of the creation from which (inclusive) the cases should be included in the export.
		 */
		private @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> from;

		/**
		 * The date (YYYY-MM-DD) of the creation up to which (inclusive) the cases should be included in the export.
		 */
		private @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> to;

		/**
		 * True ignores cases with Sormas ID and false does not do this. False is default.
		 */
		private Optional<Boolean> onlyWithoutSormasId;
	}

	@Getter
	@AllArgsConstructor
	static class CasesQuery {

		/**
		 * The date (YYYY-MM-DD) of the creation from which (inclusive) the cases should be included in the export.
		 */
		private @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> from;

		/**
		 * The date (YYYY-MM-DD) of the creation up to which (inclusive) the cases should be included in the export.
		 */
		private @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> to;

		/**
		 * Specifies whether EXTERNAL or INTERNAL cases or both should be considered. INTERNAL is default.
		 */
		private Optional<CasesRealm> casesRealm;

		/**
		 * The case type of the cases to be exported. CONTACT includes all types of contact.
		 */
		private Optional<CaseType> type;

		/**
		 * The status of the cases to be exported.
		 */
		private Optional<Status> status;
	}

	@Getter
	@AllArgsConstructor
	static class ExternalCasesQuery {

		/**
		 * The date (YYYY-MM-DD) of the last modification from which (inclusive) the cases should be included in the export.
		 */
		private @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> from;

		/**
		 * The date (YYYY-MM-DD) of the last modification up to which (inclusive) the cases should be included in the
		 * export.
		 */
		private @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> to;
	}

	static enum CasesRealm {
		EXTERNAL, INTERNAL, INTERNAL_AND_EXTERNAL
	}

	@Getter
	@AllArgsConstructor(staticName = "of")
	@Relation(collectionRelation = "departmentCsvGroups")
	static class DepartmentGroup {
		private String rkiCode;
		private String healtDepartment;
		private String healthDepartmentMail;
		private String casesCsv;
	}
}
