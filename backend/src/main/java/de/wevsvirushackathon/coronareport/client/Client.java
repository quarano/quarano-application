package de.wevsvirushackathon.coronareport.client;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import de.wevsvirushackathon.coronareport.firstReport.FirstReport;
import de.wevsvirushackathon.coronareport.healthdepartment.HealthDepartment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Client {

    @Id
    @GeneratedValue
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    private HealthDepartment healthDepartment;


    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE,
            orphanRemoval = true
    )
    private FirstReport comments;

}
