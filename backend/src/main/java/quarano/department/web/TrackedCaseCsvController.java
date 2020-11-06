package quarano.department.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Department;
import quarano.core.web.LoggedIn;
import quarano.department.TrackedCaseRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
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
	 * @param quarantineFrom The date (YYYY-MM-DD) of the last quarantine modification from which (inclusive) the cases
	 *          should be included in the export.
	 * @param quarantineTo The date (YYYY-MM-DD) of the last quarantine modification up to which (inclusive) the cases
	 *          should be included in the export.
	 * @param type The case type (INDEX or CONTACT) of the cases to be exported.
	 * @param response
	 * @throws IOException
	 */
	@GetMapping(path = "/hd/quarantines", produces = "text/csv")
	public void getQuarantineOrder(@LoggedIn Department department,
			@RequestParam("from") @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> quarantineFrom,
			@RequestParam("to") @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> quarantineTo,
			@RequestParam("type") Optional<String> type,
			HttpServletResponse response) throws IOException {

		response.setContentType("text/csv");

		var cases = caseRepo.findFilteredByQuarantine(quarantineFrom, quarantineTo, type, department.getId());

		representations.writeQuarantineOrderCsv(response.getWriter(), cases.stream());
	}
}
