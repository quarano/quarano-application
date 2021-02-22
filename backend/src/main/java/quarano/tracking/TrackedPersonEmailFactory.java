package quarano.tracking;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import quarano.account.Account;
import quarano.account.DepartmentRepository;
import quarano.core.EmailTemplates.Key;

import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * A factory to simplify creating a {@link TrackedPersonEmail} instance.
 *
 * @author Oliver Drotbohm
 * @since 1.4
 */
@Component
@RequiredArgsConstructor
public class TrackedPersonEmailFactory {

	private final TrackedPersonRepository people;
	private final DepartmentRepository departments;
	private final ContactTypeLookup contactTypes;
	private final @NonNull MessageSourceAccessor messages;

	public TrackedPersonEmail getEmailFor(Account account, String subjectKey, Key templateKey,
			Map<String, ? extends Object> placeholders) {


		var person = people.findByAccount(account).orElseThrow();
		var department = departments.findById(account.getDepartmentId()).orElseThrow();
		var contactType = contactTypes.getBy(person);
		var subject = messages.getMessage(subjectKey, person.getLocale());

		return new TrackedPersonEmail(person, department, contactType, subject, templateKey, placeholders);
	}

	public TrackedPersonEmail getEmailFor(TrackedPerson person, String subject, Key templateKey,
			Map<String, ? extends Object> placeholders) {

		var contactType = contactTypes.getBy(person);
		var department = person.getAccount()
				.map(Account::getDepartmentId)
				.flatMap(departments::findById)
				.orElseThrow();

		return new TrackedPersonEmail(person, department, contactType, subject, templateKey, placeholders);
	}
}
