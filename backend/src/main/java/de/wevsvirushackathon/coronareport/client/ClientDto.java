package de.wevsvirushackathon.coronareport.client;

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
    private String phone;
    private String zipCode;
    private boolean infected;
    private String healthDepartmentId;
}
