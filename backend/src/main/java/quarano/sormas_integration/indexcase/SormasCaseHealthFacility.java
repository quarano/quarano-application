package quarano.sormas_integration.indexcase;

        import lombok.AccessLevel;
        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.RequiredArgsConstructor;

/**
 * @author Federico Grasso
 *
 * SORMAS Case Person model
 */
@Data
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SormasCaseHealthFacility {
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
