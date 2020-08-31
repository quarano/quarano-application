package quarano.maintenance.web;

import lombok.Data;

@Data
public class SymptomDto extends QuaranoDto {
	private String name;
	private boolean isCharacteristic;
}
