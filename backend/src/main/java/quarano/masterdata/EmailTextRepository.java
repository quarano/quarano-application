package quarano.masterdata;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface EmailTextRepository extends BaseTextRepository<EmailText>, CrudRepository<EmailText, UUID> {

	static final String DEFAULT_SELECT = "select t from EmailText t where t.locale in :localeCandidates";

	@Override
	@Query(DEFAULT_SELECT)
	Streamable<EmailText> findAll(List<Locale> localeCandidates);

	@Override
	@Query(DEFAULT_SELECT + " and t.textKey = :textKey")
	Streamable<EmailText> findByTextKey(String textKey, List<Locale> localeCandidates);

	@Override
	default Function<EmailText, String> getKeyMapper() {
		return EmailText::getTextKey;
	}

	@Override
	default Function<EmailText, Locale> getLocaleMapper() {
		return EmailText::getLocale;
	}
}
