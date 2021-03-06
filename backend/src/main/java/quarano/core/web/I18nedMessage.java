package quarano.core.web;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;
import quarano.core.web.QuaranoWebConfiguration.MessageSourceResolvableSerializer;

import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Simple implementation of {@link MessageSourceResolvable} to trigger translation of the given code(s). Useful, to e.g.
 * translate texts within {@link ErrorsWithDetails}.
 *
 * @author Oliver Drotbohm
 * @author Jens Kutzsche
 * @see ErrorsWithDetails
 * @see MessageSourceResolvableSerializer
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class I18nedMessage implements MessageSourceResolvable {

	@With @Nullable @Getter //
	private final String[] codes;

	@Nullable @Getter //
	private final Object[] arguments;

	/**
	 * This text is used if the codes can't resolved.
	 */
	@With @Nullable @Getter //
	private final String defaultMessage;

	/**
	 * Creates a new {@link I18nedMessage} for the given codes.
	 *
	 * @param code the primary code to resolve.
	 * @param additionalCodes additional codes to be used for lookup.
	 * @return will never be {@literal null}.
	 */
	public static I18nedMessage of(String code, String... additionalCodes) {

		Assert.hasText(code, "Code must not be null or empty!");

		var codes = Stream.concat(Stream.of(code), Arrays.stream(additionalCodes)).toArray(String[]::new);

		return new I18nedMessage(codes, new Object[0], null);
	}

	public I18nedMessage withArguments(Object... arguments) {
		return new I18nedMessage(codes, arguments, defaultMessage);
	}
}
