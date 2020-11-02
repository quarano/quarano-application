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
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true, of = {})
@ToString
public class EmailText extends QuaranoAggregate<EmailText, EmailTextIdentifier> {

	private @Getter String textKey;
	private @Getter Locale locale;
	private @Getter @Lob String text;

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
