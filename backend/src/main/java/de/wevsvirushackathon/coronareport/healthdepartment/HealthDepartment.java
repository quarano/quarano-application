package de.wevsvirushackathon.coronareport.healthdepartment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthDepartment {
    @Id
    private String id;
    private String fullName;
    private UUID passCode;

}
