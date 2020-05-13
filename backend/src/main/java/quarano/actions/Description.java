package quarano.actions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import quarano.core.EnumMessageSourceResolvable;
import quarano.tracking.BodyTemperature;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.context.MessageSourceResolvable;

/**
 * @author Oliver Drotbohm
 * @author Felix Schultze
 */
@Embeddable
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Description implements MessageSourceResolvable {

	private final @Enumerated(EnumType.STRING) @Getter DescriptionCode code;
	private final String arguments;

	public static Description of(DescriptionCode code, Object... arguments) {

		var collect = Arrays.stream(arguments) //
				.map(Object::toString) //
				.collect(Collectors.joining("|"));

		return new Description(code, collect);
	}

	public static Description forIncreasedTemperature(BodyTemperature temperature, BodyTemperature reference) {
		return of(DescriptionCode.INCREASED_TEMPERATURE, temperature, reference);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.MessageSourceResolvable#getCodes()
	 */
	@Override
	public String[] getCodes() {
		return EnumMessageSourceResolvable.of(code).getCodes();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.MessageSourceResolvable#getArguments()
	 */
	@Override
	public Object[] getArguments() {
		return arguments.split("\\|");
	}
}
