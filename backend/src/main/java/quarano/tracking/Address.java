package quarano.tracking;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;

import org.springframework.util.StringUtils;

/**
 * @author Oliver Drotbohm
 */
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PACKAGE)
public class Address {

	private String street;
	private HouseNumber houseNumber = HouseNumber.NONE;
	private String city;
	private ZipCode zipCode;

	boolean isComplete() {

		return StringUtils.hasText(street) //
				&& StringUtils.hasText(city) //
				&& zipCode != null;
	}

	@EqualsAndHashCode
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class HouseNumber {

		public static final HouseNumber NONE = HouseNumber.of("¯\\_(ツ)_/¯");

		private final String value;

		public static HouseNumber of(String source) {
			return source == null || source.isBlank() //
					? HouseNumber.NONE //
					: new HouseNumber(source);
		}

		public boolean isAbsent() {
			return NONE.equals(this);
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return value;
		}
	}
}
