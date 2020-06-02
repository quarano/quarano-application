/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano.actions;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import quarano.diary.Diary;
import quarano.diary.DiaryEntry;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class DiaryEntryActionItems {

	private final Streamable<DiaryEntryActionItem> items;
	private final Consumer<ActionItem> itemHandler;

	/**
	 * Creates a new {@link DiaryEntryActionItems} with the given {@link ActionItem}s filtered for
	 * {@link DiaryEntryActionItem}s and an {@link ActionItem} handler to persist state changes made during invocations of
	 * business methods.
	 *
	 * @param source must not be {@literal null}.
	 * @param itemHandler must not be {@literal null}.
	 * @return
	 */
	public static DiaryEntryActionItems of(Streamable<ActionItem> source, Consumer<ActionItem> itemHandler) {

		Assert.notNull(source, "Source action items must not be null!");
		Assert.notNull(itemHandler, "Item handler must not be null!");

		return new DiaryEntryActionItems(source //
				.filter(DiaryEntryActionItem.class::isInstance) //
				.map(DiaryEntryActionItem.class::cast), itemHandler);
	}

	/**
	 * Removes the {@link ActionItem} for first characteristic symptom from the given {@link DiaryEntry}. If it was
	 * previously created for that {@link DiaryEntry}, the {@link Diary} lookup is used and all subsequent entries are
	 * inspected for a characteristic symptom and the {@link ActionItem} is created for the first one found.
	 *
	 * @param entry must not be {@literal null}.
	 * @param diary must not be {@literal null}.
	 * @param factory must not be {@literal null}.
	 * @return
	 */
	DiaryEntryActionItems removeFirstCharacteristicSymptomFrom(DiaryEntry entry, Supplier<Diary> diary,
			Function<DiaryEntry, DiaryEntryActionItem> factory) {

		// Resolve current entry
		var currentEntry = items.filter(it -> it.getEntry().equals(entry)); //

		if (currentEntry.isEmpty()) {

			return this;

		} else {

			items.map(ActionItem::resolve) //
					.forEach(itemHandler);

			diary.get().stream()//
					.sorted(Comparator.comparing(DiaryEntry::getSlot)) //
					.filter(it -> it.getSymptoms().hasCharacteristicSymptom()) //
					.findFirst() //
					.map(factory::apply) //
					.ifPresent(itemHandler);
		}

		return this;
	}

	/**
	 * Adjusts the {@link ActionItem} for a first characteristic symptom with the given {@link DiaryEntry} reporting one.
	 * The state transition rules are as follows:
	 * <ol>
	 * <li>If there's no {@link ActionItem} for characteristic symptom, we simply create one for the given
	 * {@link DiaryEntry}</li>
	 * <li>If there already is one stemming from a previous entry, we don't do anything.</li>
	 * <li>If there already is one stemming from a {@link DiaryEntry} later than the given one, we resolve that and create
	 * a new one for the given {@link DiaryEntry}</li>
	 * <li>If there's none for the current {@link DiaryEntry}, we create one.</li>
	 * </ol>
	 *
	 * @param entry must not be {@literal null}.
	 * @param itemFactory must not be {@literal null}.
	 * @return
	 */
	DiaryEntryActionItems adjustFirstCharacteristicSymptomTo(DiaryEntry entry,
			Function<DiaryEntry, DiaryEntryActionItem> itemFactory) {

		Assert.notNull(entry, "DiaryEntry must not be null!");
		Assert.state(entry.getSymptoms().hasCharacteristicSymptom(),
				"Given entry does not contain a characteristic symptom!");
		Assert.notNull(itemFactory, "DiaryEntryActionitem factory must not be null!");

		// No entries at all -> create new one
		if (items.isEmpty()) {
			itemHandler.accept(itemFactory.apply(entry));
			return this;
		}

		if (items.stream().anyMatch(it -> it.getEntry().isBefore(entry))) {
			return this;
		}

		// Resolve all future entries
		items.filter(it -> it.getEntry().isAfter(entry)) //
				.map(ActionItem::resolve) //
				.forEach(itemHandler);

		// Create new one if the current entry is not already attached to an item
		if (!items.stream().anyMatch(it -> it.getEntry().equals(entry))) {
			itemHandler.accept(itemFactory.apply(entry));
		}

		return this;
	}
}
