package de.wevsvirushackathon.coronareport.diary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.wevsvirushackathon.coronareport.client.Client;
import lombok.Data;

@Data
public class DiaryEntryDtoIn {

    private int id;
    private Client client;
    private Date dateTime;
    private float bodyTemperature;
    private List<Integer> symptoms = new ArrayList<>();
    private List<Integer> contactPersonList = new ArrayList<>();
    private boolean transmittedToHealthDepartment;
}
