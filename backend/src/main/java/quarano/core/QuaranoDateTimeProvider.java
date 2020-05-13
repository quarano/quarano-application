package quarano.core;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
public class QuaranoDateTimeProvider implements DateTimeProvider {

	private Period delta = Period.ZERO;

	/**
	 * @param delta the delta to set
	 */
	public void setDelta(Period delta) {
		this.delta = delta;
	}

	public void reset() {
		this.delta = Period.ZERO;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.auditing.DateTimeProvider#getNow()
	 */
	@Override
	public Optional<TemporalAccessor> getNow() {
		return Optional.of(LocalDateTime.now().plus(delta));
	}
}
