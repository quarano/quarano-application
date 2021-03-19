package quarano.sormas_integration.backlog;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author Federico Grasso
 */

/**
 * Index Backlog Table
 */
@Entity
@Table(name = "index_synch_backlog")
@Setter(AccessLevel.PUBLIC)
@Data
@Slf4j
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class IndexSynchBacklog {
    @NonNull
    private UUID id;

    @Id
    @Temporal(TemporalType.TIMESTAMP)
    @NonNull
    private Date syncDate;
}
