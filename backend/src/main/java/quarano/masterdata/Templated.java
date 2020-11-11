package quarano.masterdata;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.Assert;

/**
 * Abstraction for anything templated, i.e. something that can be expanded into a {@link String} by providing a
 * {@link Map} of parameters.
 *
 * @author Oliver Drotbohm
 */
public interface Templated {

	/**
	 * Creates a new {@link Templated} from the given plain {@link String} source.
	 *
	 * @param source must not be {@literal null}.
	 * @return
	 */
	public static Templated of(String source) {

		Assert.notNull(source, "Source must not be null!");

		return () -> source;
	}

	/**
	 * Returns the raw, unexpanded text.
	 *
	 * @return will never be {@literal null}.
	 */
	String getText();

	/**
	 * Expands the underlying text with the given parameters.
	 *
	 * @param parameters must not be {@literal null}.
	 * @return
	 */
	default String expand(Map<String, ? extends Object> parameters) {

		Assert.notNull(parameters, "Parameters must not be null!");

		var text = getText();

		for (Entry<String, ? extends Object> replacement : parameters.entrySet()) {
			text = text.replace(String.format("{%s}", replacement.getKey()), replacement.getValue().toString());
		}

		return text;
	}
}
