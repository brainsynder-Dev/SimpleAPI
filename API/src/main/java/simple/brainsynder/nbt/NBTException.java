package simple.brainsynder.nbt;

public class NBTException extends Exception {
    public NBTException(String s, String s1, int i) {
        super(s + " at: " + prepareMessage(s1, i));
    }

    private static String prepareMessage(String text, int length) {
        StringBuilder stringbuilder = new StringBuilder();
        int i = Math.min(text.length(), length);

        if (i > 35) {
            stringbuilder.append("...");
        }

        stringbuilder.append(text, Math.max(0, i - 35), i);
        stringbuilder.append("<--[HERE]");
        return stringbuilder.toString();
    }
}
