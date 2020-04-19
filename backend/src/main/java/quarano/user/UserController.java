package quarano.user;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.auth.Account;
import quarano.auth.web.LoggedIn;
import quarano.core.web.MapperWrapper;
import quarano.department.DepartmentRepository;
import quarano.department.TrackedCaseRepository;
import quarano.department.web.DepartmentDto;
import quarano.department.web.EnrollmentDto;
import quarano.tracking.TrackedPersonRepository;
import quarano.tracking.web.TrackedPersonDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

	private final @NonNull TrackedPersonRepository trackedPersonRepository;
	private final @NonNull DepartmentRepository departments;
	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull MapperWrapper mapper;

	@ApiOperation(value = "Get information about the logged in user", response = UserDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 403, message = "Not authorized, if there is no session"),
			@ApiResponse(code = 404, message = "Bad request"), @ApiResponse(code = 500, message = "Internal Server error") })
	@GetMapping("/me")
	ResponseEntity<?> getMe(@LoggedIn Account account) {

		var userDto = UserDto.of(account);

		departments.findById(account.getDepartmentId()) //
				.map(it -> mapper.map(it, DepartmentDto.class)) //
				.ifPresent(userDto::setHealthDepartment);

		if (account.isTrackedPerson()) {

			var person = trackedPersonRepository.findById(account.getTrackedPersonId()); //

			person.map(it -> mapper.map(it, TrackedPersonDto.class)) //
					.ifPresent(userDto::setClient);

			person.flatMap(cases::findByTrackedPerson) //
					.map(it -> new EnrollmentDto(it.getEnrollment())) //
					.ifPresent(userDto::setEnrollment);
		}

		return ResponseEntity.ok(userDto);
	}
}
