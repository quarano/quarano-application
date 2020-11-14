package quarano.masterdata.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import quarano.account.DepartmentProperties;
import quarano.masterdata.EmailText;
import quarano.masterdata.FrontendText;

import java.util.Map;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.hateoas.server.mvc.MvcLink;
import org.springframework.stereotype.Component;

/**
 * @author Jens Kutzsche
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
class TextRepresentations {

	private final @NonNull DepartmentProperties departmentConfiguration;

	public TextDto toRepresentation(FrontendText entity, boolean rawText) {

		var placeholders = placeholderMap(rawText,
				Map.of("departmentName", departmentConfiguration.getDefaultDepartment().getName()));

		var textDto = new TextDto(entity.getTextKey(), entity.expand(placeholders));

		if (rawText) {
			textDto.add(MvcLink.of(on(FrontendTextController.class).putText(entity.getTextKey(), entity.getLocale(), null),
					IanaLinkRelations.EDIT));
		}

		return textDto;
	}

	public TextDto toRepresentation(EmailText entity) {
		var textDto = new TextDto(entity.getTextKey(), entity.expand(Map.of()));

		textDto.add(MvcLink.of(on(EmailTextController.class).putText(entity.getTextKey(), entity.getLocale(), null),
				IanaLinkRelations.EDIT));

		return textDto;
	}

	private Map<String, String> placeholderMap(boolean rawText, Map<String, String> placeholder) {
		return rawText ? Map.of() : placeholder;
	}

	@Relation(collectionRelation = "texts")
	@Value
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	static class TextDto extends RepresentationModel<TextDto> {
		String key, text;
	}
}
