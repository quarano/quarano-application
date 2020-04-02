package de.wevsvirushackathon.coronareport.report;

import lombok.Getter;

public enum MonitoringStatus {

	OK("Keine Auffälligkeiten"), OVERDUE("Rückmeldund nicht rechtzeitig erfolgt"),
	CHECK_HEALTH("Auffälligkeiten im Gesundheitszustand, bitte kontaktieren"),
	END_OF_QUARANTINE("Enddatum der Quarantäne ist erreicht");

	@Getter
	private String message;

	private MonitoringStatus(String message) {
		this.message = message;
	}

}
