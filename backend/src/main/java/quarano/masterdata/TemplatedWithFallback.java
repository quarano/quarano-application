package quarano.masterdata;

import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * A {@link Templated} implementation that delegates to two {@link Templated} instances in turn and concatenates them
 * via the {@value #SEPARATOR}.
 *
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
class TemplatedWithFallback implements Templated {

	static final String SEPARATOR = "\r\n\r\n==========\r\n\r\n";

	private final Templated primary, fallback;

	/*
	 * (non-Javadoc)
	 * @see quarano.masterdata.Templated#getText()
	 */
	@Override
	public String getText() {
		return String.join(SEPARATOR, primary.getText(), fallback.getText());
	}

	/*
	 * (non-Javadoc)
	 * @see quarano.masterdata.Templated#expand(java.util.Map)
	 */
	@Override
	public String expand(Map<String, ? extends Object> parameters) {
		return String.join(SEPARATOR, primary.expand(parameters), fallback.expand(parameters));
	}
}
