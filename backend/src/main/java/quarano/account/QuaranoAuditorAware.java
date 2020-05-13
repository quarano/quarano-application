package quarano.account;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
public class QuaranoAuditorAware implements AuditorAware<UUID> {

	private final AuthenticationManager authentication;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.AuditorAware#getCurrentAuditor()
	 */
	@Override
	public Optional<UUID> getCurrentAuditor() {
		return authentication.getCurrentUser().map(Account::getIdAsUuid);
	}
}
