package quarano.sormas_integration.backlog;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
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
public class IndexSyncBacklog {
    @NonNull
    private UUID id;

    @Id
    @NonNull
    private LocalDateTime syncDate;
}
