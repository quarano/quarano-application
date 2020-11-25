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
 */
@Entity
@Table(name = "email_texts")
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true, of = {})
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailText extends QuaranoAggregate<EmailText, EmailTextIdentifier> implements Templated {

	static final String SEPARATOR = "\r\n\r\n==========\r\n\r\n";

	private @Getter String textKey;
	private @Getter Locale locale;
	private @Getter @Lob String text;

	private transient EmailText secondEmailText;

	private EmailText(EmailText originEmailText, EmailText secondEmailText) {
		super();
		this.textKey = originEmailText.getTextKey();
		this.locale = originEmailText.getLocale();
		this.text = originEmailText.getText();
		this.secondEmailText = secondEmailText;
	}

	/**
	 * Returns an email text potentially augmented with the one provided by the given fallback provider in case the
	 * current one
	 *
	 * @param defaultLocale
	 * @param fallBackProvider
	 * @return
	 */
	public Templated withDefault(Locale defaultLocale, Function<String, Optional<EmailText>> fallBackProvider) {

		if (!isSameLanguageAs(defaultLocale)) {

			var templateDefault = fallBackProvider.apply(textKey).orElseThrow();

			// Muss eine neue Instanz sein, da sonst Zyklen durch gecachte Werte entstehen können.
			return new EmailText(this, templateDefault);

		} else {
			return this;
		}
	}

	/**
	 * Expands the underlying text with the given parameters. If a parameter is a function then it will be applied with
	 * the {@link #locale} of this {@link EmailText} to create a localized replacement.
	 * 
	 * @param parameters must not be {@literal null}.
	 * @since 1.4
	 */
	@Override
	public String expand(Map<String, ? extends Object> parameters) {

		Assert.notNull(parameters, "Parameters must not be null!");

		var text = getText();

		for (Entry<String, ? extends Object> replacement : parameters.entrySet()) {

			if (replacement.getValue() instanceof Function) {
				text = text.replace(String.format("{%s}", replacement.getKey()),
						((Function) replacement.getValue()).apply(locale).toString());
			} else {
				text = text.replace(String.format("{%s}", replacement.getKey()), replacement.getValue().toString());
			}
		}

		if (secondEmailText != null) {
			text = String.join(SEPARATOR, text, secondEmailText.expand(parameters));
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
