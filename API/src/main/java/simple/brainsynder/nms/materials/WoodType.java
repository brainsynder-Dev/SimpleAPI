package simple.brainsynder.nms.materials;

public enum WoodType {
    OAK ("LOG", "LEAVES", 0),
    SPRUCE ("LOG", "LEAVES", 1),
    BIRCH ("LOG", "LEAVES", 2),
    JUNGLE ("LOG", "LEAVES", 3),

    ACACIA ("LOG_2", "LEAVES_2", 0),
    DARK_OAK ("LOG_2", "LEAVES_2", 1);

    private int data;
    private String logLegacy = "", leaveLegacy = "";
    WoodType(String logLegacy, String leaveLegacy, int data) {
        this.logLegacy = logLegacy;
        this.leaveLegacy = leaveLegacy;
        this.data = data;
    }

    public String getLeaveLegacy() {
        return leaveLegacy;
    }

    public String getLogLegacy() {
        return logLegacy;
    }

    public int getData() {
        return data;
    }
}
