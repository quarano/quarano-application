package quarano.tracking.web;

import de.wevsvirushackathon.coronareport.client.Client;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class DiaryEntryDto {

	private Client client;
	private Date dateTime;
	private float bodyTemperature;
	private List<Integer> symptoms = new ArrayList<>();
	private List<Integer> contactPersonList = new ArrayList<>();
	private boolean transmittedToHealthDepartment;
}
