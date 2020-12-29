package quarano.tracking;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class collects all old data from contacts and anonymizes the personal data to be GDPR compliant.
 *
 * @author Jens Kutzsche
 * @since 1.4
 */
@Component("tracking.GDPRAnonymizationJob")
@Slf4j
@RequiredArgsConstructor
class GDPRAnonymizationJob {

	private final @NonNull ContactPersonRepository contacts;
	private final @NonNull ContactAnonymizationProperties properties;

	@Transactional
	@Scheduled(cron = "0 40 1 * * *")
	void anonymizeContactsAndRelatedData() {

		var refDate = LocalDate.now().minus(properties.getAnonymizationAfter()).atStartOfDay();

		var oldContacts = contacts.findByMetadataCreatedIsBefore(refDate).toList();

		log.debug(oldContacts.size() + " contacts are anonymized!");

		oldContacts.forEach(ContactPerson::anonymize);
		contacts.saveAll(oldContacts);

		log.info("{} contacts were anonymized {} days after their creation!", oldContacts.size(),
				properties.getAnonymizationAfter().getDays());
	}

	@ConstructorBinding
	@RequiredArgsConstructor
	@ConfigurationProperties("quarano.gdpr.contacts")
	public static class ContactAnonymizationProperties {

		private final Period DEFAULT_ANONYMIZATION_AFTER = Period.ofDays(16);

		/**
		 * Defines the {@link Period} after that a contact will be anonymized starting from the creation date.
		 */
		private final Period anonymizationAfter;

		public Period getAnonymizationAfter() {
			return anonymizationAfter == null ? DEFAULT_ANONYMIZATION_AFTER : anonymizationAfter;
		}
	}
}
