package quarano.core;

import java.time.Period;

import org.springframework.core.convert.converter.Converter;

/**
 * @author Oliver Drotbohm
 */
public class StringToPeriodConverter implements Converter<String, Period> {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public Period convert(String source) {
		return Period.parse(source);
	}
}
