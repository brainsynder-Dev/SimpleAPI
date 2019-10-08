package simple.brainsynder.nms.utilities;

import org.bukkit.entity.Player;
import simple.brainsynder.math.MathUtils;
import simple.brainsynder.utils.ReturnValue;

import java.util.HashMap;
import java.util.Map;

public class JCommand {
    private Map<String, ReturnValue<Player>> commands = new HashMap<>();

    public String getCommand (ReturnValue<Player> onExecute) {
        return getCommand("jcommand_", onExecute);
    }

    public String getCommand (String prefix, ReturnValue<Player> onExecute) {
        String command = "/"+prefix+ randomID("1234567890", 5);
        commands.put(command, onExecute);
        return command;
    }

    public Map<String, ReturnValue<Player>> getCommands() {
        return commands;
    }

    public void removeCommand (String command) {
        commands.remove(command);
    }

    private String randomID (String chars, int length) {
        int charLength = chars.length()-1;
        StringBuilder id = new StringBuilder();

        for (int i = 0; i < length; i++) {
            id.append(chars.charAt(MathUtils.random(0, charLength)));
        }

        return id.toString();
    }
}