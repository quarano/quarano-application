package de.wevsvirushackathon.coronareport.client;

import java.sql.Timestamp;

import de.wevsvirushackathon.coronareport.healthdepartment.HealthDepartment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    private Long clientId;
    private String clientCode;
 
    private String surename;
    private String firstname;
    private String street;
    private String city;
    private String zipCode;
    private String mobilephone;
    private String phone;
    private String email;
    
    private boolean completedPersonalData;
    private boolean completedQuestionnaire;
    private boolean completedContactRetro;
    
    private Timestamp quarantineStartDateTime;
    private Timestamp quarantineEndDateTime;

    private boolean infected;
    
    private  ClientType type;
    
    private HealthDepartment healthDepartment;
}
