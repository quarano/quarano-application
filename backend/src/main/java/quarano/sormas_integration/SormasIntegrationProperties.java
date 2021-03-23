package quarano.sormas_integration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import quarano.account.Department;
import quarano.account.DepartmentContact;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.core.rki.FederalStates;

import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;
import static org.springframework.util.StringUtils.trimWhitespace;

@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("quarano.sormas-synch")
public class SormasIntegrationProperties {

    private final @Getter Master master;
    private final @Getter Interval interval;

    private final @Getter String sormasurl;
    private final @Getter String sormasuser;
    private final @Getter String sormaspass;

    @Getter
    @RequiredArgsConstructor
    static class Master {
        private final String indexcases, contacts;
    }

    @Getter
    @RequiredArgsConstructor
    static class Interval {
        private final String indexcases, contacts;
    }
}