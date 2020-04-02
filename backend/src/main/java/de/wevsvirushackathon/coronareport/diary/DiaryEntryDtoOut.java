package de.wevsvirushackathon.coronareport.diary;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import de.wevsvirushackathon.coronareport.contactperson.ContactPerson;
import de.wevsvirushackathon.coronareport.symptomes.Symptom;
import de.wevsvirushackathon.coronareport.client.Client;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class DiaryEntryDtoOut {

    private int id;

    private Client client;
    private LocalDateTime dateTime;
    private float bodyTemperature;
    private List<Symptom> symptoms = new ArrayList<>();
    private List<ContactPerson> contactPersonList = new ArrayList<>();
    private boolean transmittedToHealthDepartment;
}
