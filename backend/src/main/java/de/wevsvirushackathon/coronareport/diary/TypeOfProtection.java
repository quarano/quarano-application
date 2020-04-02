package de.wevsvirushackathon.coronareport.diary;

public enum TypeOfProtection {
    Zero("0"),
    M1("M1"),
    M2("M2"),
    K("K"),
    H("H"),
    S("S");

    private final String label;

    TypeOfProtection(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
