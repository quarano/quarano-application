package quarano.reference.web;

import lombok.Value;
import quarano.reference.FrontendText;

import org.springframework.hateoas.server.core.Relation;
import org.springframework.stereotype.Component;

/**
 * @author Jens Kutzsche
 */
@Component
public class FrontendTextRepresentations {

	public FrontendTextDto toDto(FrontendText textEntity) {
		return FrontendTextDto.from(textEntity);
	}

	@Relation(collectionRelation = "texts")
	@Value(staticConstructor = "")
	static class FrontendTextDto {
		private String key;
		private String text;

		static FrontendTextDto from(FrontendText textEntity) {
			return new FrontendTextDto(textEntity.getTextKey(), textEntity.getText());
		}
	}
}
