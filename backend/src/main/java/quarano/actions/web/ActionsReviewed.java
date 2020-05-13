package quarano.actions.web;

import lombok.Data;

import org.springframework.util.StringUtils;

/**
 * @author Oliver Drotbohm
 */
@Data
public class ActionsReviewed {

	private String comment;

	public boolean hasComment() {
		return StringUtils.hasText(comment);
	}
}
