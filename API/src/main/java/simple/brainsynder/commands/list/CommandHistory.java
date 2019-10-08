package simple.brainsynder.commands.list;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simple.brainsynder.Core;
import simple.brainsynder.api.WebAPI;
import simple.brainsynder.commands.ParentCommand;
import simple.brainsynder.commands.annotations.ICommand;
import simple.brainsynder.nms.ITellraw;
import simple.brainsynder.utils.Reflection;

@ICommand (
        name = "userhistory",
        description = "Collects the users name history"
)
public class CommandHistory extends ParentCommand {
    public CommandHistory() {
        registerCompletion(1, getOnlinePlayers());
    }

    @Override
    public boolean canExecute(CommandSender sender) {
        return sender.hasPermission("SimpleAPI.command.history");
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return;
        }


        String username = ChatColor.stripColor(args[0]);
        sender.sendMessage("§3Loading name history for: " + username);
        WebAPI.findHistory(username, Core.getInstance(),names -> {
            if (names.isEmpty()) {
                sender.sendMessage("§cError: §7An error occurred when connecting to minecraftchar.us, Is the username correct?");
                return;
            }
            sender.sendMessage(ChatColor.DARK_AQUA+username + "'s NameHistory ----");
            int num = 0;
            int size = names.size();
            for (String S : names) {
                if (num == 0 && (((size - 1) != num))) {
                    sender.sendMessage("§9- §7" + S.replace("(First Username)", "§b(First Name)"));
                } else if (num == (size - 1)) {
                    sender.sendMessage("§9- §7" + S.replace("(First Username)", "") + " §b(Current Name)");
                } else {
                    sender.sendMessage("§9- §7" + S.replace("(First Username)", ""));
                }
                num++;
            }
            StringBuilder line2 = new StringBuilder();
            for (int i = 0; i < (size - 3); i++)
                line2.append('-');
            sender.sendMessage(ChatColor.DARK_AQUA + line2.toString());

            if (sender instanceof Player) {
                ITellraw message = Reflection.getTellraw("Name history received at: ");
                message.color(ChatColor.DARK_AQUA);
                message.then("v2.minecraftchar.us/history/?user=" + username);
                message.color(ChatColor.GRAY);
                message.tooltip("§7Click here to go to the page");
                message.link("https://v2.minecraftchar.us/history/?user=" + username);
                message.send((Player) sender);
            }else{
                sender.sendMessage("§3Name history received at: §7http://v2.minecraftchar.us/history/?user=" + username);
            }
        });
    }

    @Override
    public void sendUsage(CommandSender sender) {
        if (sender instanceof Player) {
            ITellraw message = Reflection.getTellraw("Usage: /userhistory ");
            message.color(ChatColor.GRAY);
            message.then("§4<§cusername§4>");
            message.tooltip("§7This argument can be a", "§7Players past name or their current name", "§cNote: §7Username must be valid");
            message.send((Player) sender);
            return;
        }
        super.sendUsage(sender);
    }
}
