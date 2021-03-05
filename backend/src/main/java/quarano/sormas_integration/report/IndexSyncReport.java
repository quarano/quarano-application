package quarano.sormas_integration.report;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Federico Grasso
 *
 * Index Synchronization Report Table
 */

@Entity
@Table(name = "index_sync_report")
@Setter(AccessLevel.PUBLIC)
@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class IndexSyncReport {

    @NonNull
    private Integer personsNumber;

    @NonNull
    private Integer casesNumber;

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

