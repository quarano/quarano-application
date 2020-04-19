package quarano.auth;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import quarano.auth.Password.UnencryptedPassword;
import quarano.department.Department.DepartmentIdentifier;
import quarano.registration.ActivationCode;
import quarano.registration.ActivationCode.ActivationCodeIdentifier;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.lang.Nullable;

@Data
@Accessors(chain = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountRegistrationDetails {

	private String username;
	private UnencryptedPassword unencryptedPassword;
	private String firstname;
	private String lastname;
	private LocalDate dateOfBirth;
	private UUID activationCodeLiteral;
	private String email;
	private @Nullable TrackedPersonIdentifier trackedPersonId;
	private DepartmentIdentifier departmentId;

	public Try<AccountRegistrationDetails> apply(TrackedPerson person) {

		if (!person.hasBirthdayOf(dateOfBirth)) {
			return Try.failure(new AccountRegistrationException("Given date of birth does not match date of birth of case"));
		}

		this.firstname = person.getFirstName();
		this.lastname = person.getLastName();

		return Try.success(this);
	}

	public Optional<TrackedPersonIdentifier> getTrackedPersonId() {
		return Optional.ofNullable(trackedPersonId);
	}

	public ActivationCodeIdentifier getActivationCodeIdentifier() {
		return ActivationCodeIdentifier.of(activationCodeLiteral);
	}

	public AccountRegistrationDetails apply(ActivationCode code) {

		this.trackedPersonId = code.getTrackedPersonId();
		this.departmentId = code.getDepartmentId();

		return this;
	}
}
