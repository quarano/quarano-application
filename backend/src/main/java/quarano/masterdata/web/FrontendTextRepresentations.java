package quarano.masterdata.web;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import quarano.account.DepartmentProperties;
import quarano.masterdata.FrontendText;

import java.util.Map;

import org.springframework.hateoas.server.core.Relation;
import org.springframework.stereotype.Component;

/**
 * @author Jens Kutzsche
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
class FrontendTextRepresentations {

	private final @NonNull DepartmentProperties departmentConfiguration;

	public FrontendTextDto toRepresentation(FrontendText entity) {

		var placeholders = Map.of("departmentName", departmentConfiguration.getDefaultDepartment().getName());

		return new FrontendTextDto(entity.getTextKey(), entity.expand(placeholders));
	}

	@Relation(collectionRelation = "texts")
	@Value
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	static class FrontendTextDto {
		String key, text;
	}
}
