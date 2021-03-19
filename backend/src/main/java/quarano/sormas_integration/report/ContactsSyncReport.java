package quarano.sormas_integration.report;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Federico Grasso
 *
 * Contacts Synchronization Report Table
 */

@Entity
@Table(name = "contacts_sync_report")
@Setter(AccessLevel.PUBLIC)
@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ContactsSyncReport {

    @NonNull
    private Integer personsNumber;

    @Id
    @Temporal(TemporalType.TIMESTAMP)
    @NonNull
    private Date syncDate;

    @NonNull
    private Long syncTime;

    @NonNull
    private ReportStatus status;

    public enum ReportStatus {
        STARTED,
        FAILED,
        SUCCESS
    }
}

