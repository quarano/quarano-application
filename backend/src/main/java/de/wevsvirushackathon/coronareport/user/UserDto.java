package de.wevsvirushackathon.coronareport.user;

import de.wevsvirushackathon.coronareport.client.ClientDto;
import de.wevsvirushackathon.coronareport.healthdepartment.HealthDepartment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

	@With private String username;
    private HealthDepartment healthDepartment;
	private ClientDto client;
	private String firstname;
	private String surename;
	
}
