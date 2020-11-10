package quarano.department;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import quarano.account.Department.DepartmentIdentifier;
import quarano.account.Password.UnencryptedPassword;
import quarano.department.activation.ActivationCode;
import quarano.department.activation.ActivationCode.ActivationCodeIdentifier;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPerson.TrackedPersonIdentifier;

import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class RegistrationDetails {

	private String username;
	private UnencryptedPassword unencryptedPassword;
	private String firstname;
	private String lastname;
	private LocalDate dateOfBirth;
	private UUID activationCodeLiteral;
	private TrackedPersonIdentifier trackedPersonId;
	private DepartmentIdentifier departmentId;
	private Locale locale;

	public Try<RegistrationDetails> apply(TrackedPerson person) {

		if (person.getDateOfBirth() != null && !person.hasBirthdayOf(dateOfBirth)) {
			return Try.failure(new RegistrationException("Given date of birth does not match date of birth of case"));
		}

		this.firstname = person.getFirstName();
		this.lastname = person.getLastName();

		return Try.success(this);
	}

	public ActivationCodeIdentifier getActivationCodeIdentifier() {
		return ActivationCodeIdentifier.of(activationCodeLiteral);
	}

	public RegistrationDetails apply(ActivationCode code) {

		this.trackedPersonId = code.getTrackedPersonId();
		this.departmentId = code.getDepartmentId();

		return this;
	}
}
