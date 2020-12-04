package quarano.masterdata;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import quarano.core.QuaranoAggregate;
import quarano.masterdata.EmailText.EmailTextIdentifier;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.jmolecules.ddd.types.Identifier;
import org.springframework.util.Assert;

/**
 * @author Jens Kutzsche
 * @author Oliver Drotbohm
 */
@Entity
@Table(name = "email_texts")
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true, of = {})
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailText extends QuaranoAggregate<EmailText, EmailTextIdentifier> implements Templated {

	private @Getter String textKey;
	private @Getter Locale locale;
	private @Getter @Lob String text;

	/**
	 * Returns an email text potentially augmented with the one provided by the given fallback provider in case the
	 * current one is not in the same language as the given {@link Locale}.
	 *
	 * @param defaultLocale must not be {@literal null}.
	 * @param fallBackProvider must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public Templated withDefault(Locale defaultLocale, Function<String, Optional<EmailText>> fallBackProvider) {

		Assert.notNull(defaultLocale, "Default locale must not be null!");
		Assert.notNull(fallBackProvider, "Fallback provider must not be null!");

		return !isSameLanguageAs(defaultLocale)
				? TemplatedWithFallback.of(this, fallBackProvider.apply(textKey).orElseThrow())
				: this;
	}

	/**
	 * Expands the underlying text with the given parameters. If a parameter is a function then it will be applied with
	 * the {@link #locale} of this {@link EmailText} to create a localized replacement.
	 *
	 * @param parameters must not be {@literal null}.
	 * @since 1.4
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String expand(Map<String, ? extends Object> parameters) {

		Assert.notNull(parameters, "Parameters must not be null!");

		var text = getText();

		for (Entry<String, ? extends Object> replacement : parameters.entrySet()) {

			var key = String.format("{%s}", replacement.getKey());

			var stringValue = replacement.getValue() instanceof Function
					? ((Function) replacement.getValue()).apply(locale).toString()
					: replacement.getValue().toString();

			text = text.replace(key, stringValue);
		}

		return text;
	}

	private boolean isSameLanguageAs(Locale reference) {
		return locale.getLanguage().equals(reference.getLanguage());
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class EmailTextIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = 4282930202833920047L;

		final UUID emailTextId;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return emailTextId.toString();
		}
	}
}
