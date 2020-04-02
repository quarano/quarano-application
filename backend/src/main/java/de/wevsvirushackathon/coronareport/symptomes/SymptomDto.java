package de.wevsvirushackathon.coronareport.symptomes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SymptomDto {
	private Long id;
	private String name;
	private boolean isCharacteristic;
}
