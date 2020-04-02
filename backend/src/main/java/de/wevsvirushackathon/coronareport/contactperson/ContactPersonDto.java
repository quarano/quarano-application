package de.wevsvirushackathon.coronareport.contactperson;

import de.wevsvirushackathon.coronareport.diary.TypeOfContract;
import de.wevsvirushackathon.coronareport.diary.TypeOfProtection;
import lombok.*;

@Data
public class ContactPersonDto {

    private Long id;
    private String surename;
    private String firstname;
    private String phone;
    private String email;
    private TypeOfContract typeOfContract;
    private TypeOfProtection typeOfProtection;
}
