package quarano.masterdata;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface FrontendTextRepository extends CrudRepository<FrontendText, UUID> {

	static final String DEFAULT_SELECT = "select t from FrontendText t where t.locale = :locale";

	@Query(DEFAULT_SELECT)
	Streamable<FrontendText> findAll(Locale locale);

	/**
	 * Finds a {@link FrontendText} by the given key and {@link Locale}. Prefer
	 * {@link #findByTextKey(quarano.masterdata.FrontendText.Keys, Locale)} using one of the constants.
	 *
	 * @param textKey
	 * @param locale
	 * @return
	 * @see #findByTextKey(quarano.masterdata.FrontendText.Keys, Locale)
	 */
	@Query(DEFAULT_SELECT + " and t.textKey = :textKey")
	Optional<FrontendText> findByTextKey(String textKey, Locale locale);

	default Optional<FrontendText> findByTextKey(FrontendText.Keys key, Locale locale) {
		return findByTextKey(key.toString(), locale);
	}
}
