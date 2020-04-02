package de.wevsvirushackathon.coronareport.diary;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import de.wevsvirushackathon.coronareport.client.Client;
import de.wevsvirushackathon.coronareport.client.ClientRepository;
import de.wevsvirushackathon.coronareport.contactperson.ContactPerson;
import de.wevsvirushackathon.coronareport.contactperson.ContactPersonRepository;
import de.wevsvirushackathon.coronareport.healthdepartment.HealthDepartment;
import de.wevsvirushackathon.coronareport.healthdepartment.HealthDepartmentRepository;
import de.wevsvirushackathon.coronareport.infrastructure.errorhandling.ArgumentType;
import de.wevsvirushackathon.coronareport.infrastructure.errorhandling.InvalidArgumentException;
import de.wevsvirushackathon.coronareport.symptomes.Symptom;
import de.wevsvirushackathon.coronareport.symptomes.SymptomRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The controller for creating, updating and reading of diaryEntries by a client
 * 
 * @author Patrick Otto
 *
 */
@RestController
public class DiaryEntryController {

	private DiaryEntryRepository diaryEntryRepository;
	private ClientRepository userRepository;
	private ContactPersonRepository contactPersonRepository;
	private SymptomRepository symptomRepository;
	private ModelMapper modelMapper;
	private HealthDepartmentRepository healthDepartmentRepository;

	@Autowired
	public DiaryEntryController(DiaryEntryRepository diaryEntryRepository, ClientRepository userRepository,
			ContactPersonRepository contactPersonRepository, SymptomRepository symptomRepository,
			ModelMapper modelMapper, HealthDepartmentRepository healthDepartmentRepository) {
		this.diaryEntryRepository = diaryEntryRepository;
		this.userRepository = userRepository;
		this.contactPersonRepository = contactPersonRepository;
		this.symptomRepository = symptomRepository;
		this.modelMapper = modelMapper;
		this.healthDepartmentRepository = healthDepartmentRepository;
	}

	/**
	 * Saves a new diary entry
	 * 
	 * @param clientCode    The code of the client provided by teh Health department
	 *                      or by the application after first registration
	 * @param diaryEntryDto
	 * @return
	 * @throws InvalidArgumentException
	 * @throws ClientNotAuthorizedException
	 * @throws ParseException
	 */
	@ApiOperation(value = "Create a new diary entry", response = DiaryEntryDtoOut.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Entity successfully created"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@PostMapping("/diaryentries")
	public ResponseEntity<DiaryEntryDtoOut> storeEntry(
			@ApiParam(value = "The header variable containing the client-code", required = true) @RequestHeader("client-code") String clientCode,
			@ApiParam(value = "The DiaryEntry to be created", required = true) @RequestBody DiaryEntryDtoIn diaryEntryDto)
			throws InvalidArgumentException, ClientNotAuthorizedException, ParseException {

		DiaryEntry validDiaryEntry;

		validDiaryEntry = prepareAndValidateEntry(diaryEntryDto, clientCode);

		validDiaryEntry = diaryEntryRepository.save(validDiaryEntry);

		return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(validDiaryEntry));
	}

	/**
	 * Updates an existing new diary entry
	 * 
	 * @param clientCode    The code of the client provided by teh Health department
	 *                      or by the application after first registration
	 * @param diaryEntryDto
	 * @return
	 * @throws InvalidArgumentException
	 * @throws ParseException
	 * @throws ClientNotAuthorizedException
	 */
	@ApiOperation(value = "Updates an existing  diary entry")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Entity successfully updated"),
			@ApiResponse(code = 400, message = "If DiaryEntry could not be parsed or path id and id in body differs."),
			@ApiResponse(code = 403, message = "If client-id does not exist or has no authorization to modify the data."),
			@ApiResponse(code = 404, message = "If DiaryEntry with given Id does not exist"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@PutMapping("/diaryentries/{id}")
	public ResponseEntity<?> updateEntry(
			@ApiParam(value = "The header variable containing the client-code", required = true) @RequestHeader("client-code") String clientCode,
			@ApiParam(value = "The DiaryEntry to be updated", required = true) @RequestBody DiaryEntryDtoIn diaryEntryDto,
			@ApiParam(value = "The id of the entity that should be updated", required = true) @PathVariable String id)
			throws InvalidArgumentException, ParseException, ClientNotAuthorizedException {

		int diaryEntryId = parseIdFromPath(id);

		// check if pathId and body id match
		if (diaryEntryId != diaryEntryDto.getId()) {
			throw new InvalidArgumentException("id", "Path id and entity id in body do not match.",
					ArgumentType.BODY_PARAMETER, id.toString());
		}

		// check if diaryEntry exists
		if (!diaryEntryRepository.existsById((long) diaryEntryId)) {
			throw new EntityNotFoundException("DiaryEntry with id '" + diaryEntryId + "' does not exist");
		}

		DiaryEntry validDiaryEntry;
		validDiaryEntry = prepareAndValidateEntry(diaryEntryDto, clientCode);

		diaryEntryRepository.save(validDiaryEntry);

		return ResponseEntity.ok().build();
	}

	/**
	 * Parses a given string-id to a int or throws special InvalidArgumentException
	 * for nicer error-handling
	 * 
	 * @param id
	 * @return
	 * @throws InvalidArgumentException
	 */
	private int parseIdFromPath(String id) throws InvalidArgumentException {
		try {
			return (int) Integer.parseInt(id);
		} catch (NumberFormatException ex) {
			throw new InvalidArgumentException("id", "Given id is no integer", ArgumentType.PATH_VARIABLE,
					id.toString());
		}
	}

	/**
	 * Validates if all mandatory information is given an transforms the dto into
	 * the model object
	 * 
	 * @param diaryEntryDto
	 * @return
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws ClientNotAuthorizedException
	 */
	private DiaryEntry prepareAndValidateEntry(DiaryEntryDtoIn diaryEntryDto, String clientCode)
			throws ParseException, InvalidArgumentException, ClientNotAuthorizedException {
		DiaryEntry diaryEntry = convertToEntity(diaryEntryDto, clientCode);

		// check if date is set
		if (diaryEntry.getDateTime() == null) {
			throw new InvalidArgumentException("dateTime", "Attribute DateTime must not be null",
					ArgumentType.BODY_PARAMETER, null);
		}

		return diaryEntry;

	}

	/**
	 * Returns all diary entries of a client
	 * 
	 * @param clientCode the identifier of the client
	 * @return
	 * @throws ParseException
	 */
	@ApiOperation(value = "Retrieves all diaryEntries of a client ordered by their date descending")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Entities successfully fetched"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@GetMapping("/diaryentries")
	public Iterable<DiaryEntryDtoOut> getDiaryEntries(
			@ApiParam(value = "The header variable containing the client-code", required = true) @RequestHeader("client-code") String clientCode)
			throws ParseException {

		Client client = userRepository.findByClientCode(clientCode);

		Iterable<DiaryEntry> entries = diaryEntryRepository.findAllByClientOrderByDateTimeDesc(client);

		ArrayList<DiaryEntryDtoOut> dtos = new ArrayList<>();

		entries.forEach(x -> dtos.add(convertToDto(x)));

		return dtos;
	}

	/**
	 * Returns a single diaryentry identified by its id
	 * 
	 * @param clientCode the identifier of the client
	 * @param id         the identifier of the diaryentry
	 * @return
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 */
	@ApiOperation(value = "Returns a single diary entry")
	@GetMapping("/diaryentries/{id}")
	public ResponseEntity<DiaryEntryDtoOut> getDiaryEntry(
			@ApiParam(value = "The header variable containing the client-code", required = true) @RequestHeader("client-code") String clientCode,
			@ApiParam(value = "The id of the requested diaryEntry element", required = true) @PathVariable String id)
			throws ParseException, InvalidArgumentException {

		int diaryEntryId = parseIdFromPath(id);

		Optional<DiaryEntry> diaryEntryOptional = diaryEntryRepository.findById((long) diaryEntryId);

		return diaryEntryOptional.map(diaryEntry -> ResponseEntity.ok(convertToDto(diaryEntry))).orElseGet(() -> {
			throw new EntityNotFoundException("Diary Entry with id '" + diaryEntryId + "' does not exist");
		});

	}

	@GetMapping("/diaryentries/export/csv/{healthDepartmentId}/{healthDepartmentPassCode}")
	public void exportCSV(@PathVariable("healthDepartmentId") String healthDepartmentId,
			@PathVariable UUID healthDepartmentPassCode, HttpServletResponse response) throws Exception {

		// set file name and content type
		String filename = String.format("diary_entry_%s.csv", healthDepartmentId);

		// rudimentary security setting TODO replace after POC
		Optional<HealthDepartment> healthDepartment = this.healthDepartmentRepository.findById(healthDepartmentId);
		if (healthDepartment.isEmpty() || !healthDepartment.get().getPassCode().equals(healthDepartmentPassCode)) {
			// TODO replacy by proper exception handling
			throw new IllegalAccessException("Wrong credentials for health department");
		}
		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		final Collection<DiaryEntry> diaryEntryCollection = diaryEntryRepository
				.findAllByHealthDepartmentId(healthDepartmentId);
		response.getWriter().println(
				"clientId;firstname;surename;dateTime;bodyTemperature;symptoms;contactFirstname;contactSurename;typeOfContract;typeOfProtection");

		final String valueSep = ";";

		for (final DiaryEntry d : diaryEntryCollection) {
			final Collection<ContactPerson> contactPersonCollection = d.getContactPersons().size() == 0
					? Collections.singletonList(new ContactPerson())
					: d.getContactPersons();
			for (final ContactPerson cp : contactPersonCollection) {
				response.getWriter().print(d.getClient().getClientId());
				response.getWriter().print(valueSep);
				response.getWriter().print(d.getClient().getFirstname());
				response.getWriter().print(valueSep);
				response.getWriter().print(d.getClient().getSurename());
				response.getWriter().print(valueSep);
				response.getWriter().print(d.getDateTime());
				response.getWriter().print(valueSep);
				response.getWriter().print(d.getBodyTemperature());
				response.getWriter().print(valueSep);
				response.getWriter().print(d.getSymptoms() == null ? "[]"
						: d.getSymptoms().stream().map(Symptom::getName).collect(Collectors.toList()).toString());
				response.getWriter().print(valueSep);
				response.getWriter().print(cp.getFirstname() == null ? "" : cp.getFirstname());
				response.getWriter().print(valueSep);
				response.getWriter().print(cp.getSurename() == null ? "" : cp.getSurename());
				response.getWriter().print(valueSep);
				response.getWriter().print(cp.getTypeOfContract() == null ? "" : cp.getTypeOfContract().getLabel());
				response.getWriter().print(valueSep);
				response.getWriter().print(cp.getTypeOfProtection() == null ? "" : cp.getTypeOfProtection().getLabel());
				response.getWriter().println();
			}
		}
	}

	/**
	 * Converts a model object into its DTO representation
	 * 
	 * @param diaryEntry
	 * @return
	 */
	private DiaryEntryDtoOut convertToDto(DiaryEntry diaryEntry) {
		DiaryEntryDtoOut diaryEntryDto = modelMapper.map(diaryEntry, DiaryEntryDtoOut.class);

		// contacts attribute has different names in dto and model, so map it manually
		diaryEntryDto.setContactPersonList(diaryEntry.getContactPersons());

		// set Id expicitly because Long is not mapped automatically
		diaryEntryDto.setId(diaryEntry.getId().intValue());

		if (diaryEntry.getDateTime() != null) {
			diaryEntryDto.setDateTime(diaryEntry.getDateTime().toLocalDateTime());
		}

		return diaryEntryDto;
	}

	/**
	 * Converts a Dto object into the model object
	 * 
	 * @param diaryEntryDto
	 * @return
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 * @throws ClientNotAuthorizedException
	 */
	private DiaryEntry convertToEntity(DiaryEntryDtoIn diaryEntryDto, String clientCode)
			throws ParseException, InvalidArgumentException, ClientNotAuthorizedException {
		DiaryEntry entry = modelMapper.map(diaryEntryDto, DiaryEntry.class);

		// map id explicitly, because datatype Long is not mapped automatically
		if (diaryEntryDto.getId() == 0) {
			entry.setId(null);
		} else {
			entry.setId((long) diaryEntryDto.getId());
		}

		// lookup client by clientcode
		Client client = userRepository.findByClientCode(clientCode);
		if (client == null) {
			throw new ClientNotAuthorizedException(clientCode, "No matching client for given client-code");
		}
		entry.setClient(client);

		// lookup symptoms
		ArrayList<Symptom> resolvedSymptoms = new ArrayList<>();

		for (long symptomId : diaryEntryDto.getSymptoms()) {

			symptomRepository.findById(symptomId).ifPresent(resolvedSymptoms::add);
		}
		entry.setSymptoms(resolvedSymptoms);

		// lookup ContactPersons
		ArrayList<ContactPerson> resolvedContactPerson = new ArrayList<>();

		for (long contactPersonId : diaryEntryDto.getContactPersonList()) {

			contactPersonRepository.findById(contactPersonId).ifPresent(resolvedContactPerson::add);
		}
		entry.setContactPersons(resolvedContactPerson);

		return entry;
	}

}
