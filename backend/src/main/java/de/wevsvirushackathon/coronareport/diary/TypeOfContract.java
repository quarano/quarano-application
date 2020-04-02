package de.wevsvirushackathon.coronareport.diary;

public enum TypeOfContract {
    O("O"), S("S"), P("P"), AE("Ä"), Aer("Aer"), Mat("Mat"), And("And");

    private final String label;

    TypeOfContract(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

}
