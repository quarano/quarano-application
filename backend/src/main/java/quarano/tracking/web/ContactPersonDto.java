package quarano.tracking.web;

import de.wevsvirushackathon.coronareport.diary.TypeOfContract;
import de.wevsvirushackathon.coronareport.diary.TypeOfProtection;
import lombok.Value;

@Value
public class ContactPersonDto {

	Long id;
	String surename, firstname, phone, email;
	TypeOfContract typeOfContract;
	TypeOfProtection typeOfProtection;
}
