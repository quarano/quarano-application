package quarano.tracking;

import io.jsonwebtoken.lang.Assert;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.data.domain.Range;
import org.springframework.util.NumberUtils;

/**
 * @author Oliver Drotbohm
 * @author Michael J. Simons
 */
@Embeddable
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class BodyTemperature {

	private static final Range<Float> VALID = Range.open(35.0f, 42.0f);

	@Column(name = "temperature", precision = 5, scale = 3) //
	private final BigDecimal value;
	
	public static BodyTemperature of(float value) {
		return new BodyTemperature(BigDecimal.valueOf(value));
	}

	/**
	 * Returns whether the current {@link BodyTemperature} exceeds the given one.
	 *
	 * @param that must not be {@literal null}.
	 * @return
	 */
	public boolean exceeds(BodyTemperature that) {

		Assert.notNull(that, "BodyTemperature must not be null!");

		return this.value.compareTo(that.value) >= 1;
	}

	public static boolean isValid(float value) {
		return VALID.contains(value);
	}

	/**
	 * Parses a {@link BodyTemperature} from the given source {@link String}. Required by Spring Boot's configuration
	 * properties binding.
	 *
	 * @param source must not be {@literal null} or empty.
	 * @return
	 */
	public static BodyTemperature from(String source) {

		Assert.hasText(source, "No body temperature source value given!");

		return BodyTemperature.of(NumberUtils.parseNumber(source, Float.class));
	}

	public float getValue() {
		return this.value.floatValue();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(Locale.GERMAN, "%.1f°C", value.floatValue());
	}
}
