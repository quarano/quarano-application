package quarano.core.web;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import quarano.core.web.QuaranoWebConfiguration.MessageSourceResolvableSerializer;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * Simple implementation of {@link MessageSourceResolvable} to trigger translation of the given code(s). Useful, to e.g.
 * translate texts within {@link ErrorsWithDetails}.
 *
 * @author Oliver Drotbohm
 * @see ErrorsWithDetails
 * @see MessageSourceResolvableSerializer
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class I18nedMessage implements MessageSourceResolvable {

	private final String[] codes;
	private final Object[] arguments;

	/**
	 * Creates a new {@link I18nedMessage} for the given codes.
	 *
	 * @param code the primary code to resolve.
	 * @param additionalCodes additional codes to be used for lookup.
	 * @return will never be {@literal null}.
	 */
	public static I18nedMessage of(String code, String... additionalCodes) {

		Assert.hasText(code, "Code must not be null or empty!");

		String[] codes = new String[additionalCodes.length + 1];

		codes[0] = code;

		for (int i = 0; i < additionalCodes.length; i++) {
			codes[i + 1] = additionalCodes[i];
		}

		return new I18nedMessage(codes, new Object[0]);
	}

	public I18nedMessage withArguments(Object... arguments) {
		return new I18nedMessage(codes, arguments);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.MessageSourceResolvable#getCodes()
	 */
	@NonNull
	@Override
	public String[] getCodes() {
		return codes;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.MessageSourceResolvable#getArguments()
	 */
	@NonNull
	@Override
	public Object[] getArguments() {
		return arguments;
	}
}
