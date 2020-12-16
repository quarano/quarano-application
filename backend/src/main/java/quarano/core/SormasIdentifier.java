package quarano.core;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.jmolecules.ddd.types.Identifier;

@Embeddable
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class SormasIdentifier implements Identifier, Serializable {

	private static final long serialVersionUID = 5512222218561608908L;

	final String sormasId;

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return sormasId;
	}
}
