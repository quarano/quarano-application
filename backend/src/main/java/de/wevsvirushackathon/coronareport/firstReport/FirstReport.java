package de.wevsvirushackathon.coronareport.firstReport;

import java.time.LocalDateTime;

import javax.persistence.*;

import de.wevsvirushackathon.coronareport.client.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FirstReport {

    @Id
    @GeneratedValue
    private Long id;


    private LocalDateTime dateTime;

    private boolean min15MinutesContactWithC19Pat;
    private boolean nursingActionOnC19Pat;
    private boolean directContactWithLiquidsOfC19pat;
    private boolean flightPassengerCloseRowC19Pat;
    private boolean flightCrewMemberWithC19Pat;
    private boolean belongToMedicalStaff;
    private boolean belongToNursingStaff;
    private boolean belongToLaboratoryStaff;
    private boolean familyMember;
    private boolean isPassengerOnSameFlightAsPatient;
    private boolean OtherContactType;

    @OneToOne(cascade = CascadeType.MERGE)
    private Client client;

}
