package quarano.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.util.Assert;

/**
 * Core Quarano properties.
 *
 * @author Oliver Drotbohm
 */
@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("quarano.app")
public class CoreProperties {

	private final String host;

	/**
	 * Returns a complete URI based on the configured host and the given relative URL.
	 *
	 * @param url must not be {@literal null} or empty.
	 * @return
	 * @since 1.4
	 */
	public String getHostRelativeUrl(String url) {

		Assert.hasText(url, "Url must not be null or empty!");

		var scheme = host.startsWith("localhost") ? "http" : "https";
		var preparedUrl = url.startsWith("/") ? url.substring(1) : url;

		return String.format("%s://%s/%s", scheme, host, preparedUrl);
	}
}
