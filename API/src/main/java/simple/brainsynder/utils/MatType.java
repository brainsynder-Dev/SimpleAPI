package simple.brainsynder.utils;

public enum MatType {
    STAINED_GLASS_PANE,
    WOOL,
    STAINED_CLAY("TERRACOTTA"),
    INK_SACK,
    DYE,
    CONCRETE,
    CONCRETE_POWDER;
    private String name;

    MatType() {
        this.name = name();
    }

    MatType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}