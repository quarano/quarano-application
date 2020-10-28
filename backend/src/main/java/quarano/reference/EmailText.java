package quarano.reference;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import quarano.core.QuaranoAggregate;
import quarano.reference.EmailText.ClientTextIdentifier;

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
@Table(name = "email_texts")
@Data
@Setter(AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true, of = {})
public class EmailText extends QuaranoAggregate<EmailText, ClientTextIdentifier> {

	private final @Getter String textKey;
	private final @Getter Locale locale;

	@Lob
	private final @Getter String text;

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class ClientTextIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = -4584209020556099333L;

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
