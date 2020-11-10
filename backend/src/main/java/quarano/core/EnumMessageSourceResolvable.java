package quarano.core;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.lang.NonNull;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class EnumMessageSourceResolvable implements MessageSourceResolvable {

	private final Enum<?> value;

	public static EnumMessageSourceResolvable of(Enum<?> value) {
		return new EnumMessageSourceResolvable(value);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.MessageSourceResolvable#getCodes()
	 */
	@NonNull
	@Override
	public String[] getCodes() {

		var name = value.name()
				.toLowerCase(Locale.US)
				.replace("_", "-");

		var type = value.getClass().getName().replace("$", ".");

		return new String[] { String.format("%s.%s", type, name) };
	}
}
