package quarano.core;

import lombok.*;
import quarano.core.ZipCode;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import javax.persistence.Embeddable;

/**
 * @author Oliver Drotbohm
 */
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

	private String street;
	private HouseNumber houseNumber = HouseNumber.NONE;
	private String city;
	private ZipCode zipCode;

	public boolean isComplete() {

		return StringUtils.hasText(street)
				&& StringUtils.hasText(city)
				&& zipCode != null;
	}

	@EqualsAndHashCode
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class HouseNumber {

		public static final HouseNumber NONE = HouseNumber.of("¯\\_(ツ)_/¯");

		private final String value;

		public static HouseNumber of(@Nullable String source) {
			return source == null || source.isBlank()
					? HouseNumber.NONE
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
			return (isAbsent()) ? "" : value;
		}
	}
}
