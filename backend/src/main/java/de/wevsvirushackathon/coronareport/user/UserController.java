package de.wevsvirushackathon.coronareport.user;

import java.text.ParseException;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.wevsvirushackathon.coronareport.infrastructure.errorhandling.InconsistentDataException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.auth.Account;
import quarano.auth.AccountRepository;
import quarano.auth.web.LoggedIn;
import quarano.department.Department;
import quarano.department.DepartmentRepository;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;
import quarano.department.web.EnrollmentDto;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonRepository;
import quarano.tracking.web.TrackedPersonDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final @NonNull TrackedPersonRepository trackedPersonRepository;
	private final @NonNull AccountRepository accountRepository;
	private final @NonNull DepartmentRepository hdRepository;
	private final @NonNull TrackedCaseRepository caseRepository;
	private final @NonNull ModelMapper modelMapper;


	/**
	 * Retrieves information of the tracked case, that is currently logged in
	 * 
	 * @return a clientDto of the authenticated user
	 * @throws UserNotFoundException
	 * @throws InconsistentDataException
	 * @throws ParseException
	 */
	@ApiOperation(value = "Get information about the logged in user", response = UserDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 403, message = "Not authorized, if there is no session"),
			@ApiResponse(code = 404, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@GetMapping("/me")
	public ResponseEntity<UserDto> getMe(@LoggedIn Account user)
			throws UserNotFoundException, InconsistentDataException {
		
		UserDto userDto = new UserDto().withUsername(user.getUsername());
		
		if(user.isTrackedPerson()) 
		{
			TrackedPerson person = trackedPersonRepository.findById(user.getTrackedPersonId()).orElse(null);
			TrackedCase trackedCase = caseRepository.findByTrackedPerson(person).orElse(null);
			
			
			// fetch client of authenticated account
			
			
			userDto.setClient(modelMapper.map(person, TrackedPersonDto.class));
			userDto.setHealthDepartment(trackedCase.getDepartment());
			userDto.setEnrollment(new EnrollmentDto(trackedCase.getEnrollment()));
		}
		else {
			// user is no client, it is a employee of the health department

			Department hd = hdRepository.findById(user.getDepartmentId())
					.orElseThrow(() -> new InconsistentDataException(
							"No healthdepartment found for hdid from account : '" + user.getDepartmentId() + "'"));
			userDto.setHealthDepartment(hd);
		}
		
		userDto.setFirstName(user.getFirstname());
		userDto.setLastName(user.getLastname());

		return ResponseEntity.ok(userDto);
	}

}
