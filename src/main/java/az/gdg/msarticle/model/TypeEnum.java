package az.gdg.msarticle.model;

public enum TypeEnum {
    ARTICLE(1), FORUM(2), NEWS(3);

    private final int value;

    TypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}