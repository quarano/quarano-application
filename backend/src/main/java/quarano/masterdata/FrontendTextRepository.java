package quarano.masterdata;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface FrontendTextRepository extends BaseTextRepository<FrontendText>, CrudRepository<FrontendText, UUID> {

	static final String DEFAULT_SELECT = "select t from FrontendText t where t.locale in :localeCandidates";

	@Override
	@Query(DEFAULT_SELECT)
	Streamable<FrontendText> findAll(List<Locale> localeCandidates);

	@Override
	@Query(DEFAULT_SELECT + " and t.textKey = :textKey")
	Streamable<FrontendText> findByTextKey(String textKey, List<Locale> localeCandidates);

	@Override
	default Function<FrontendText, String> getKeyMapper() {
		return FrontendText::getTextKey;
	}

	@Override
	default Function<FrontendText, Locale> getLocaleMapper() {
		return FrontendText::getLocale;
	}
}
