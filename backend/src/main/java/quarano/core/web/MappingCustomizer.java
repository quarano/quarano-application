package quarano.core.web;

import org.modelmapper.ModelMapper;

/**
 * Callback interface for customizations to be applied to a {@link ModelMapper}. Primary purpose is to be able to order
 * them so that e.g. fundamental mapping setup is already registered when standard type inspection is triggered during
 * the setup of specialized types.
 *
 * @author Oliver Drotbohm
 */
public interface MappingCustomizer {

	/**
	 * Customize the given {@link ModelMapper}.
	 *
	 * @param mapper will never be {@literal null}.
	 */
	void customize(ModelMapper mapper);
}
