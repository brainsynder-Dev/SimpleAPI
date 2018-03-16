package simple.brainsynder.commands.list;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import simple.brainsynder.Core;
import simple.brainsynder.api.WebAPI;
import simple.brainsynder.commands.BaseCommand;
import simple.brainsynder.files.Language;
import simple.brainsynder.nms.ITellraw;
import simple.brainsynder.utils.Perms;
import simple.brainsynder.utils.Reflection;

public class CommandHistory extends BaseCommand {
    public CommandHistory() {
        super("userhistory");
    }

    @Override
    public void playerSendCommandEvent(Player player, String[] args) {
        if (!Perms.HISTORY.has(player)) {
            player.sendMessage(Core.getLanguage().getString(Language.NOPERMISSION, true));
            return;
        }
        if (args.length == 0) {
            ITellraw message = Reflection.getTellraw("Usage: /userhistory ");
            message.color(ChatColor.RED);
            message.then("<username>");
            message.color(ChatColor.RED);
            message.tooltip("§7This argument can be a", "§7Players past name or their current name", "§cNote: §7Username must be valid");
            message.send(player);
        } else {
            String username = ChatColor.stripColor(args[0]);
            player.sendMessage("§3Loading name history for: " + username);
            String value = WebAPI.getData(WebAPI.Type.PREVIOUS_NAMES, username);
            if (value == null) {
                player.sendMessage("§cError: §7An error occurred when connecting to minecraftchar.us, Is the username correct?");
                return;
            }
            String line1 = username + "'s NameHistory ----";
            player.sendMessage(ChatColor.DARK_AQUA + line1);
            String[] values = value.split("\n");
            int num = 0;
            for (String S : values) {
                if (num == 0 && (((values.length - 1) != num))) {
                    player.sendMessage("§9- §7" + S.replace("(First Username)", "§b(First Name)"));
                } else if (num == (values.length - 1)) {
                    player.sendMessage("§9- §7" + S.replace("(First Username)", "") + " §b(Current Name)");
                } else {
                    player.sendMessage("§9- §7" + S.replace("(First Username)", ""));
                }
                num++;
            }

            StringBuilder line2 = new StringBuilder();
            for (int i = 0; i < (line1.length() - 3); i++)
                line2.append('-');
            player.sendMessage(ChatColor.DARK_AQUA + line2.toString());
            ITellraw message = Reflection.getTellraw("Name history received at: ");
            message.color(ChatColor.DARK_AQUA);
            message.then("v2.minecraftchar.us/history/?user=" + username);
            message.color(ChatColor.GRAY);
            message.tooltip("§7Click here to go to the page");
            message.link("https://v2.minecraftchar.us/history/?user=" + username);
            message.send(player);
        }
    }
}
