package de.wevsvirushackathon.coronareport.report;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import de.wevsvirushackathon.coronareport.contactperson.ContactPerson;
import de.wevsvirushackathon.coronareport.diary.DiaryEntry;
import de.wevsvirushackathon.coronareport.firstReport.FirstReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HDClient {

    private Long clientId;
    private String clientCode;
    private String surename;
    private String firstname;
    private String phone;
    private String zipCode;

    private boolean infected;
  
    private float currentBodyTemperature;
    private String healthDepartmentId;
    
    private LocalDateTime dateTimeOfLastReport;

    private List<MonitoringStatus> monitoringStatus = new ArrayList<>();
    
    private String monitoringMessage;
    
    private List<FirstReport> comments = new ArrayList<>();
    
    private Iterable<DiaryEntry> diaryEntires  = new ArrayList<>();
    
    private List<ContactPerson> contacts = new ArrayList<>();

}
