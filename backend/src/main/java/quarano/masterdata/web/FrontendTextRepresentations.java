package quarano.masterdata.web;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import quarano.account.DepartmentProperties;
import quarano.masterdata.FrontendText;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.hateoas.server.core.Relation;
import org.springframework.stereotype.Component;

/**
 * @author Jens Kutzsche
 */
@Component
@RequiredArgsConstructor
public class FrontendTextRepresentations {

	private final @NonNull DepartmentProperties depProperties;

	public FrontendTextDto toRepresentation(FrontendText textEntity) {

		var placeholders = Map.of("departmentName", depProperties.getDefaultDepartment().getName());

		return FrontendTextDto.from(textEntity, placeholders);
	}

	@Relation(collectionRelation = "texts")
	@Value
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	static class FrontendTextDto {

		private String key;
		private String text;

		static FrontendTextDto from(FrontendText textEntity, Map<String, ? extends Object> placeholders) {

			String text = textEntity.getText();

			for (Entry<String, ? extends Object> replacement : placeholders.entrySet()) {
				text = text.replace(String.format("{%s}", replacement.getKey()), replacement.getValue().toString());
			}

			return new FrontendTextDto(textEntity.getTextKey(), text);
		}
	}
}
