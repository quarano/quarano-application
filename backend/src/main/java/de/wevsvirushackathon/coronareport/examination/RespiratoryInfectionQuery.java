package de.wevsvirushackathon.coronareport.examination;

import de.wevsvirushackathon.coronareport.client.Client;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Data
public class RespiratoryInfectionQuery {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Client client;

    private LocalDateTime dateTime;

    private boolean afterContactWithC19Pat;

    private LocalDateTime repeatedAnamnesisOn;
    private String repeatedAnamnesisAt;
    private String getRepeatedAnamnesisPhone;


}
