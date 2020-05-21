package az.gdg.msarticle.model;

public enum TypeEnum {

    ARTICLE(0), FORUM(1), NEWS(2);

    private final int value;

    TypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
