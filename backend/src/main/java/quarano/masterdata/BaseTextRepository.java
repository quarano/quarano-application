package quarano.masterdata;

import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.util.Streamable;
import org.springframework.lang.Nullable;

@NoRepositoryBean
public interface BaseTextRepository<T> {

	Streamable<T> findAll(List<Locale> localeCandidates);

	Streamable<T> findByTextKey(String textKey, List<Locale> localeCandidates);

	Function<T, String> getKeyMapper();

	Function<T, Locale> getLocaleMapper();

	default Streamable<T> findAll(@Nullable Locale locale) {
		var localeCandidates = createLocalesCandidates(locale);

		return Streamable.of(findAll(localeCandidates)
				.stream()
				.sorted(createLocalePrioComparator(localeCandidates))
				.collect(Collectors.toMap(getKeyMapper(), Function.identity(), (a, b) -> a))
				.values());
	}

	default Try<T> findByTextKey(String textKey, @Nullable Locale locale) {
		var localeCandidates = createLocalesCandidates(locale);

		Optional<T> text = findByTextKey(textKey, localeCandidates)
				.stream()
				.sorted(createLocalePrioComparator(localeCandidates))
				.reduce((a, b) -> a);

		return Option.ofOptional(text)
				.toTry(() -> {

					var localeTagsStr = "[" + String.join("|", localeCandidates.toString()) + "]";
					return new IllegalStateException("Could not find any of the template entries "
							+ String.format("text key = %s and locale = %s", textKey, localeTagsStr) + "!");
				});
	}

	private List<Locale> createLocalesCandidates(Locale locale) {

		var locales = new ArrayList<Locale>(6);

		Stream.of(Locale.getDefault(), locale)
				.filter(Objects::nonNull)
				.forEach(it -> {
					var language = it.getLanguage();
					if (StringUtils.isNoneBlank(language)) {
						locales.add(0, new Locale(language));
					}

					var country = it.getCountry();
					if (StringUtils.isNoneBlank(country)) {
						locales.add(0, new Locale(language, country));
					}

					var variant = it.getVariant();
					if (StringUtils.isNoneBlank(variant)) {
						locales.add(0, new Locale(language, country, variant));
					}
				});

		return locales;
	}

	private Comparator<T> createLocalePrioComparator(List<Locale> localeCandidates) {
		return Comparator.<T, String> comparing(getKeyMapper())
				.thenComparing(getLocaleMapper(), Comparator.comparingInt(localeCandidates::indexOf));
	}
}
