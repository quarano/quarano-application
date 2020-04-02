package de.wevsvirushackathon.coronareport.contactperson;

import com.sun.istack.NotNull;
import de.wevsvirushackathon.coronareport.diary.TypeOfContract;
import de.wevsvirushackathon.coronareport.diary.TypeOfProtection;
import de.wevsvirushackathon.coronareport.client.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ContactPerson {
    @Id
    @GeneratedValue
    @NotNull
    private Long id;

    @ManyToOne
    @JoinColumn(name="client_id", nullable=false)
    private Client client;

    private String surename;

    private String firstname;

    private String phone;

    private String email;

    private TypeOfContract typeOfContract;

    private TypeOfProtection typeOfProtection;
}
