package quarano.masterdata;

import quarano.core.EmailTemplates;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.LocaleUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface EmailTextRepository extends CrudRepository<EmailText, UUID> {

	static final String DEFAULT_SELECT = "select t from EmailText t where t.locale = :locale";

	@Query(DEFAULT_SELECT)
	Streamable<EmailText> findAll(Locale locale);

	@Cacheable
	@Query(DEFAULT_SELECT + " and t.textKey = :textKey")
	Optional<EmailText> findByTextKey(String textKey, Locale locale);

	default Optional<EmailText> findByTextKey(EmailTemplates.Key key, Locale locale) {
		return findByTextKey(key.getKey(), LocaleUtils.toLocale(locale.getLanguage()));
	}
}
