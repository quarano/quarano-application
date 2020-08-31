package quarano.maintenance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "quarano.deletion")
public class ObsoleteCleanerProperties {
    private final boolean enabled;
    private final int indexMonths;
    private final int contactMonths;
    private final boolean saveCopy;
    private final String exportPath;
    private final int batchSize;
}