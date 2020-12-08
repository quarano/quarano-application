package quarano.actions;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import quarano.account.Account;
import quarano.account.AuthenticationManager;
import quarano.department.Comment;
import quarano.department.TrackedCase;
import quarano.department.TrackedCaseRepository;

import javax.transaction.Transactional;

import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
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
	private final @NonNull ApplicationContext context;

	@SuppressWarnings("null")
	public void resolveItemsFor(TrackedCase trackedCase, @Nullable String comment) {

		if (StringUtils.hasText(comment)) {

			var author = authentication.getCurrentUser()
					.map(Account::getFullName)
					.orElse(context.getId()); // Application name

			trackedCase = cases.save(trackedCase.addComment(new Comment(comment, author)));
		}

		items.findByCase(trackedCase)
				.resolveManually(items::save);
	}

	/**
	 * Returns the number of unresolved {@link ActionItem}s for the given {@link TrackedCase}.
	 *
	 * @param trackedCase must not be {@literal null}.
	 * @return
	 */
	public long getNumberOfOpenItems(TrackedCase trackedCase) {

		Assert.notNull(trackedCase, "TrackedCase must not be null!");

		return items.countUnresolvedByTrackedPerson(trackedCase.getTrackedPerson().getId());
	}
}
