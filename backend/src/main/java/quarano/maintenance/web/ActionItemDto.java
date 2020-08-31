package quarano.maintenance.web;

import lombok.Data;

@Data
public class ActionItemDto extends QuaranoDto {
	private String type;
	private DescriptionDto description;
	private boolean resolved;
}
