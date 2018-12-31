package simple.brainsynder.utils;

import org.bukkit.Bukkit;

public enum ServerVersion {
    UNKNOWN ("Unknown"),
    v1_8_R3 ("v1_8_R3"),
    v1_9_R1 ("v1_9_R1"),
    v1_9_R2 ("v1_9_R2"),
    v1_10_R1 ("v1_10_R1"),
    v1_11_R1 ("v1_11_R1"),
    v1_12_R1 ("v1_12_R1"),
    v1_13_R1 ("v1_13_R1"),
    v1_13_R2 ("v1_13_R2");

    private String version;
    ServerVersion (String version) {
        this.version = version;
    }

    public int getIntVersion () {
        if (version.equals("Unknown")) return -1;
        String vString = version.replace("v", "");
        int v = 17;
        if (!vString.isEmpty()) {
            String[] array = vString.split("_");
            v = Integer.parseInt(array[0] + array[1]);
        }
        return v;
    }

    public static ServerVersion getVersion () {
        for (ServerVersion version : values()) {
            if (version.version.equals(Bukkit.getServer().getClass().getPackage().getName().substring(23))) {
                return version;
            }
        }
        return ServerVersion.UNKNOWN;
    }

    public static boolean isEqualNew (ServerVersion version) {
        return (getVersion().getIntVersion() >= version.getIntVersion());
    }

    public static boolean isEqual (ServerVersion version) {
        return (getVersion().getIntVersion() == version.getIntVersion());
    }

    public static boolean isEqualOld (ServerVersion version) {
        return (getVersion().getIntVersion() <= version.getIntVersion());
    }
}
