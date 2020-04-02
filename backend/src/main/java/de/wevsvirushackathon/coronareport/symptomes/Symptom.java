package de.wevsvirushackathon.coronareport.symptomes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Symptom {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private boolean isCharacteristic; 

}
