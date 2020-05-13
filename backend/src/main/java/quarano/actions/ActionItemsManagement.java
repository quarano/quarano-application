package quarano.actions;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Account;
import quarano.account.AuthenticationManager;
import quarano.department.Comment;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;

import javax.transaction.Transactional;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author Oliver Drotbohm
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ActionItemsManagement {

	private final @NonNull TrackedCaseRepository cases;
	private final @NonNull ActionItemRepository items;
	private final @NonNull AuthenticationManager authentication;

	@SuppressWarnings("null")
	public void resolveItemsFor(TrackedCase trackedCase, @Nullable String comment) {

		if (StringUtils.hasText(comment)) {

			var author = authentication.getCurrentUser().map(Account::getFullName).orElse("");

			trackedCase = cases.save(trackedCase.addComment(new Comment(comment, author)));
		}

		items.findByCase(trackedCase) //
				.resolve(items::save);
	}
}
