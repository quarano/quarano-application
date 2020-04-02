package de.wevsvirushackathon.coronareport.symptomes;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class SymptomController {
	
	private SymptomRepository repo;
	private ModelMapper modelMapper;

	@Autowired
	public SymptomController(SymptomRepository repo, ModelMapper modelMapper) {
		this.repo = repo;
		this.modelMapper = modelMapper;
	}

	/**
	 * Returns all symptom entries. Should be used as master-data for other api calls;
	 * @return
	 */
	@GetMapping("/symptoms")
	public List<SymptomDto> getSymptoms() {
		return StreamSupport.stream(repo.findAll().spliterator(), false)
				.map(x -> modelMapper.map(x, SymptomDto.class)).collect(Collectors.toList());
	}

	/**
	 * Stores a new Symptom
	 * @param symptomDto
	 * @return
	 */
	@PostMapping("/symptom")
	public SymptomDto addSymptom(@RequestBody SymptomDto symptomDto) {
		final Symptom symptom = modelMapper.map(symptomDto, Symptom.class);
		return this.modelMapper.map(this.repo.save(symptom), SymptomDto.class);
	}

	/**
	 * Stores an array
	 * @param symptomDtos
	 * @return
	 */
	@PostMapping("/symptoms")
	public Iterable<SymptomDto> addSymptoms(@RequestBody List<SymptomDto> symptomDtos) {
		final List<Symptom> symtoms = symptomDtos.stream()
				.map(x -> modelMapper.map(x, Symptom.class))
				.collect(Collectors.toList());
		final Iterable<Symptom> updatedSymptoms = this.repo.saveAll(symtoms);
		return StreamSupport.stream(repo.findAll().spliterator(), false)
				.map(x -> modelMapper.map(x, SymptomDto.class)).collect(Collectors.toList());
	}

}
