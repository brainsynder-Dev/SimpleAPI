package simple.brainsynder.commands.list;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simple.brainsynder.Core;
import simple.brainsynder.commands.ParentCommand;
import simple.brainsynder.commands.annotations.ICommand;
import simple.brainsynder.files.plugin_use.Language;
import simple.brainsynder.nms.IActionMessage;
import simple.brainsynder.nms.ITellraw;
import simple.brainsynder.utils.Reflection;
import simple.brainsynder.utils.plugin_use.Logger;

@ICommand (
        name = "action",
        description = "Sends a message above the target players hotbar"
)
public class CommandAction extends ParentCommand {
    public CommandAction () {
        registerCompletion(1, getOnlinePlayers());
    }

    @Override
    public boolean canExecute(CommandSender sender) {
        return sender.hasPermission("SimpleAPI.command.action");
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return;
        }
        String target = args[0];

        if (args.length == 1) {
            sendUsage(sender);
            return;
        }

        String message = messageMaker(args, 1);
        IActionMessage action = Reflection.getActionMessage();
        send(target, value -> action.sendMessage(value, message), type -> {
            if (type == ReturnType.NO_PLAYER) {
                sender.sendMessage(Core.getLanguage().getString(Language.NO_TEAM, true).replace("%team%", args[0].replaceFirst("team=", "")));
            }else if (type == ReturnType.NO_TEAM) {
                sender.sendMessage(Core.getLanguage().getString(Language.MISSINGPLAYER, true));
            }
        });
        if (Core.getLanguage().getBoolean(Language.LOG_COMMANDS))
            Logger.log("action", sender.getName() + " executed the Action command to:{" + target + "} with the message { " + message + " }");

    }

    @Override
    public void sendUsage(CommandSender sender) {
        if (sender instanceof Player) {
            ITellraw tellraw = Reflection.getTellraw("Usage: /action ");
            tellraw.color(ChatColor.GRAY);
            tellraw.then("§4<§cplayer§4> ");
            tellraw.tooltip("§7You can use §c'@a' §7for all online players", "§7Or you can use §c'team=(team name)'§7 to send the message", "§7to all scoreboard team members.");
            tellraw.then("§4<§cmessage§4>");
            tellraw.tooltip("§7This will set the message", "§7that will be shown above the hotbar", "§7Spaces and color codes are supported.");
            tellraw.send((Player) sender);
            return;
        }

        super.sendUsage(sender);
    }
}
