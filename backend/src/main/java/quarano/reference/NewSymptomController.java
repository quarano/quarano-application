package quarano.reference;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequiredArgsConstructor
public class NewSymptomController {

	private static final Sort BY_NAME_ASCENDING = Sort.sort(NewSymptom.class).by(NewSymptom::getName).ascending();

	private final @NonNull NewSymptomRepository symptoms;
	private final @NonNull ModelMapper modelMapper;

	/**
	 * Returns all symptom entries. Should be used as master-data for other api calls;
	 *
	 * @return
	 */
	@GetMapping("/api/symptoms")
	public Stream<SymptomDto> getSymptoms() {

		return symptoms.findAll(BY_NAME_ASCENDING) //
				.map(it -> modelMapper.map(it, SymptomDto.class)) //
				.stream();
	}

	/**
	 * Stores a new Symptom
	 *
	 * @param symptomDto
	 * @return
	 */
	@PostMapping("/api/symptoms")
	public SymptomDto addSymptom(@RequestBody SymptomDto symptomDto) {
		var symptom = modelMapper.map(symptomDto, NewSymptom.class);
		return modelMapper.map(symptoms.save(symptom), SymptomDto.class);
	}

	/**
	 * Stores an array
	 *
	 * @param symptomDtos
	 * @return
	 */
	@PostMapping("/api/allsymptoms")
	public Stream<SymptomDto> addSymptoms(@RequestBody List<SymptomDto> symptomDtos) {

		symptomDtos.stream() //
				.map(x -> modelMapper.map(x, NewSymptom.class)) //
				.forEach(symptoms::save);

		return symptoms.findAll() //
				.map(x -> modelMapper.map(x, SymptomDto.class)) //
				.stream();
	}
}
