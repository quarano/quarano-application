package quarano.tracking;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class RiskAssessment {

	private @Getter @Setter Boolean isHealthStaff;
	private @Getter @Setter Boolean isSenior;
	private @Getter @Setter Boolean hasPreExistingConditions;
	
}
