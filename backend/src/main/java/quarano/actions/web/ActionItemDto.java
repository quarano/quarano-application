package quarano.actions.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.RequiredArgsConstructor;
import quarano.actions.ActionItem;
import quarano.actions.ActionItem.ItemType;
import quarano.diary.DiaryEntry;
import quarano.diary.web.DiaryController;
import quarano.actions.DiaryEntryActionItem;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.support.MessageSourceAccessor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
public class ActionItemDto {

	private final ActionItem item;
	private final MessageSourceAccessor messages;

	public LocalDateTime getDate() {
		return item.getMetadata().getLastModified();
	}

	public ItemType getType() {
		return item.getType();
	}

	public float getWeight() {
		return item.getWeight();
	}

	public String getDescription() {
		return messages.getMessage(item.getDescription());
	}

	@SuppressWarnings("null")
	@JsonInclude(Include.NON_EMPTY)
	@JsonProperty("_links")
	public Map<String, Object> getLinks() {

		var diaryController = on(DiaryController.class);
		var links = new HashMap<String, Object>();

		if (DiaryEntryActionItem.class.isInstance(item)) {

			DiaryEntry entry = ((DiaryEntryActionItem) item).getEntry();

			links.put("entry",
					Map.of("href", fromMethodCall(diaryController.getDiaryEntry(entry.getId(), null)).toUriString()));
		}

		return links;
	}
}
