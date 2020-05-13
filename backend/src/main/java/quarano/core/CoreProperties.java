package quarano.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @author Oliver Drotbohm
 */
@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("quarano.app")
public class CoreProperties {
	private final String host;
}
