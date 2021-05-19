package quarano.sormas_integration.lookup;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sormas_lookup")
@Setter(AccessLevel.PUBLIC)
@Data
@Slf4j
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SormasLookup {
    @NonNull
    @Id
    private String personId;

    @NonNull
    private String caseId;
}
