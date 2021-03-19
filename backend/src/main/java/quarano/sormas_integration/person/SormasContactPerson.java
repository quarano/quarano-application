package quarano.sormas_integration.person;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author Federico Grasso
 *
 * SORMAS Contact person model
 */

@Data
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SormasContactPerson {
    private String uuid;

    /**
     * Getters and setter
     * (Already defined with Lombok
     * but re-defined to make properties accessible by MapStruct)
     */

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
