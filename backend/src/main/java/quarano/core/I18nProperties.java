package quarano.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Locale;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * @author Jens Kutzsche
 */
@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "quarano.i18n")
public class I18nProperties {

	private final Locale defaultLocale = Locale.GERMANY;
}
