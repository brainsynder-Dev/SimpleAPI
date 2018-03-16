package simple.brainsynder.commands.list;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import simple.brainsynder.Core;
import simple.brainsynder.commands.BaseCommand;
import simple.brainsynder.files.Language;
import simple.brainsynder.nms.IActionMessage;
import simple.brainsynder.nms.ITellraw;
import simple.brainsynder.utils.Logger;
import simple.brainsynder.utils.Perms;
import simple.brainsynder.utils.Reflection;

public class CommandAction extends BaseCommand {
    public CommandAction() {
        super("action");
    }

    @Override
    public void consoleSendCommandEvent(CommandSender sender, String[] args) {
        IActionMessage message = Reflection.getActionMessage();
        if (args.length == 0) {
            sender.sendMessage("§cUsage: /action <player> <message>");
        } else if (args[0].equalsIgnoreCase("@a")) {
            if (args.length == 1) {
                sender.sendMessage("§cUsage: /action " + args[0] + " <message>");
            } else {
                String msg = ChatColor.translateAlternateColorCodes('&', messageMaker(args, 1));
                message.sendMessage(Bukkit.getOnlinePlayers(), msg);
                if (Core.getLanguage().getBoolean(Language.LOG_COMMANDS))
                    Logger.log("action", sender.getName() + " executed the Action command to:{" + args[0] + "} with the message { " + msg + " }");
            }
        } else if (args[0].startsWith("team=")) {
            Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(args[0].replaceFirst("team=", ""));
            if (team == null) {
                sender.sendMessage(Core.getLanguage().getString(Language.NO_TEAM, true).replace("%team%", args[0].replaceFirst("team=", "")));
                return;
            }
            if (args.length == 1) {
                sender.sendMessage("§cUsage: /action " + args[0] + " <message>");
            } else {
                String msg = ChatColor.translateAlternateColorCodes('&', messageMaker(args, 1));
                if (Core.getLanguage().getBoolean(Language.LOG_COMMANDS))
                    Logger.log("action", sender.getName() + " executed the Action command to:{" + args[0] + "} with the message { " + msg + " }");
                for (String name : team.getEntries()) {
                    Player target = Bukkit.getPlayer(name);
                    if (target != null)
                        message.sendMessage(target, msg);
                }
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Core.getLanguage().getString(Language.MISSINGPLAYER, true));
                return;
            }
            if (args.length == 1) {
                sender.sendMessage("§cUsage: /action " + args[0] + " <message>");
            } else {
                String msg = ChatColor.translateAlternateColorCodes('&', messageMaker(args, 1));
                message.sendMessage(target, msg);
                if (Core.getLanguage().getBoolean(Language.LOG_COMMANDS))
                    Logger.log("action", sender.getName() + " executed the Action command to:{" + args[0] + "} with the message { " + msg + " }");
            }
        }
    }

    @Override
    public void playerSendCommandEvent(Player player, String[] args) {
        if (!Perms.ACTION.has(player)) {
            player.sendMessage(Core.getLanguage().getString(Language.NOPERMISSION, true));
            return;
        }
        IActionMessage message = Reflection.getActionMessage();

        if (args.length == 0) {
            ITellraw tellraw = Reflection.getTellraw("Usage: /action ");
            tellraw.color(ChatColor.RED);
            tellraw.then("<player> ");
            tellraw.color(ChatColor.RED);
            tellraw.tooltip("§7You can use §c'@a' §7for all online players", "§7Or you can use §c'team=(team name)'§7 to send the message", "§7to all scoreboard team members.");
            tellraw.then("<message>");
            tellraw.color(ChatColor.RED);
            tellraw.tooltip("§7This will set the message", "§7that will be shown above the hotbar", "§7Spaces and color codes are supported.");
            tellraw.send(player);
        } else if (args[0].equalsIgnoreCase("@a")) {
            if (args.length == 1) {
                player.sendMessage("§cUsage: /action " + args[0] + " <message>");
            } else {
                String msg = ChatColor.translateAlternateColorCodes('&', messageMaker(args, 1));
                message.sendMessage(Bukkit.getOnlinePlayers(), msg);
                if (Core.getLanguage().getBoolean(Language.LOG_COMMANDS))
                    Logger.log("action", player.getName() + " executed the Action command to:{" + args[0] + "} with the message { " + msg + " }");
            }
        } else if (args[0].startsWith("team=")) {
            Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(args[0].replaceFirst("team=", ""));
            if (team == null) {
                player.sendMessage(Core.getLanguage().getString(Language.NO_TEAM, true).replace("%team%", args[0].replaceFirst("team=", "")));
                return;
            }
            if (args.length == 1) {
                player.sendMessage("§cUsage: /action " + args[0] + " <message>");
            } else {
                String msg = ChatColor.translateAlternateColorCodes('&', messageMaker(args, 1));
                if (Core.getLanguage().getBoolean(Language.LOG_COMMANDS))
                    Logger.log("action", player.getName() + " executed the Action command to:{" + args[0] + "} with the message { " + msg + " }");
                for (String name : team.getEntries()) {
                    Player target = Bukkit.getPlayer(name);
                    if (target != null)
                    message.sendMessage(target, msg);
                }
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(Core.getLanguage().getString(Language.MISSINGPLAYER, true));
                return;
            }
            if (args.length == 1) {
                player.sendMessage("§cUsage: /action " + args[0] + " <message>");
            } else {
                String msg = ChatColor.translateAlternateColorCodes('&', messageMaker(args, 1));
                message.sendMessage(target, msg);
                if (Core.getLanguage().getBoolean(Language.LOG_COMMANDS))
                    Logger.log("action", player.getName() + " executed the Action command to:{" + args[0] + "} with the message { " + msg + " }");
            }
        }
    }
}
