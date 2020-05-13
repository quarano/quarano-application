package quarano.actions;

import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.function.Consumer;

import org.springframework.data.util.Streamable;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
public class ActionItems implements Streamable<ActionItem> {

	private final Streamable<ActionItem> items;

	public static ActionItems empty() {
		return of(Streamable.empty());
	}

	public static ActionItems of(ActionItem... items) {
		return of(Streamable.of(items));
	}

	public float getPriority() {

		return items.stream() //
				.map(ActionItem::getWeight) //
				.reduce(0.0f, (l, r) -> l + r);
	}

	public Streamable<ActionItem> getHealthItems() {

		return items //
				.filter(ActionItem::isUnresolved) //
				.filter(ActionItem::isMedicalItem);
	}

	public Streamable<ActionItem> getProcessItems() {

		return items //
				.filter(ActionItem::isUnresolved) //
				.filter(ActionItem::isProcessItem);
	}

	public Streamable<ActionItem> getResolvedItems() {

		return items //
				.filter(ActionItem::isResolved);
	}

	public Streamable<ActionItem> getUnresolvedItems() {

		return items //
				.filter(ActionItem::isUnresolved);
	}

	public boolean hasUnresolvedItems() {

		return items.stream() //
				.anyMatch(ActionItem::isUnresolved);
	}

	public ActionItems resolve(Consumer<ActionItem> callback) {

		getUnresolvedItems() //
				.map(ActionItem::resolve) //
				.forEach(callback);

		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<ActionItem> iterator() {
		return items.iterator();
	}
}
