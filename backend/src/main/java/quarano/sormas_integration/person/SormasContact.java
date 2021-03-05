package quarano.sormas_integration.person;

import lombok.*;

/**
 * @author Federico Grasso
 *
 * SORMAS Contact model
 */

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SormasContact {
    private String uuid;
    private SormasContactPerson person;

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

    public SormasContactPerson getPerson() {
        return person;
    }

    public void setPerson(SormasContactPerson person) {
        this.person = person;
    }
}
