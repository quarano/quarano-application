package de.wevsvirushackathon.coronareport.client;

import de.wevsvirushackathon.coronareport.firstReport.FirstReport;
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
public class Client {

    @Id
    @GeneratedValue
    private Long clientId;
    private String clientCode;
    private String surename;
    private String firstname;
    private String phone;
    private String zipCode;

    private boolean infected;

    private String healthDepartmentId;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE,
            orphanRemoval = true
    )
    private FirstReport comments;

}
