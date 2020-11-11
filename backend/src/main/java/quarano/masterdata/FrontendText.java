package quarano.masterdata;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import quarano.core.QuaranoAggregate;
import quarano.masterdata.FrontendText.FrontendTextIdentifier;

import java.io.Serializable;
import java.util.Locale;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.jmolecules.ddd.types.Identifier;

/**
 * @author Jens Kutzsche
 */
@Entity
@Table(name = "frontend_texts")
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true, of = {})
@ToString
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(staticName = "of")
public class FrontendText extends QuaranoAggregate<FrontendText, FrontendTextIdentifier> implements Templated {

	public enum Keys {

		WELCOME_INDEX, WELCOME_CONTACT, TERMS, IMPRINT, DATA_PROTECTION;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return name().toLowerCase(Locale.ENGLISH).replace("_", "-");
		}
	}

	private @Getter String textKey;
	private @Getter Locale locale;
	private @Getter @Lob String text;

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class FrontendTextIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = -4584209020556099333L;

		final UUID frontendTextId;

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return frontendTextId.toString();
		}
	}
}
