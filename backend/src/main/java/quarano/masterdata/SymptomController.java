package quarano.masterdata;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.stream.Stream;

import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequiredArgsConstructor
class SymptomController {

	private static final Sort BY_NAME_ASCENDING = Sort.sort(Symptom.class).by(Symptom::getName).ascending();

	private final @NonNull SymptomRepository symptoms;
	private final @NonNull SymptomRepresentations representations;

	/**
	 * Returns all symptom entries. Should be used as master-data for other api calls;
	 *
	 * @return
	 */
	@GetMapping("/api/symptoms")
	public Stream<SymptomDto> getSymptoms() {

		return symptoms.findAll(BY_NAME_ASCENDING)
				.map(it -> representations.toRepresentation(it))
				.stream()
				.sorted(Comparator.comparing(SymptomDto::getName));
	}
}
