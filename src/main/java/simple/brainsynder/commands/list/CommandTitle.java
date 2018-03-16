package simple.brainsynder.commands.list;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import simple.brainsynder.Core;
import simple.brainsynder.commands.BaseCommand;
import simple.brainsynder.files.Language;
import simple.brainsynder.nms.ITellraw;
import simple.brainsynder.nms.ITitleMessage;
import simple.brainsynder.utils.Logger;
import simple.brainsynder.utils.Perms;
import simple.brainsynder.utils.Reflection;

public class CommandTitle extends BaseCommand {
    public CommandTitle() {
        super("titlemaker");
    }

    @Override
    public void consoleSendCommandEvent(CommandSender sender, String[] args) {
        ITitleMessage message = Reflection.getTitleMessage();
        if (message == null) {
            sender.sendMessage(Core.getLanguage().getString(Language.NOT_SUPPORTED, true));
            return;
        }
        String title;
        String subtitle = " ";
        if (args.length == 0) {
            sender.sendMessage("§cUsage: /titlemaker <player> <title> [subtitle]");
        } else if (args[0].equalsIgnoreCase("@a")) {
            if (args.length == 1) {
                sender.sendMessage("§cUsage: /titlemaker " + args[0] + " <title> [subtitle]");
            } else {
                String msg = ChatColor.translateAlternateColorCodes('&', args[1]);
                title = (msg.replace("_", " "));
                if (args.length == 2) {
                    message.sendMessage(Bukkit.getOnlinePlayers(), 0, 5, 2, title, subtitle);
                } else {
                    subtitle = ChatColor.translateAlternateColorCodes('&', args[2]);
                    subtitle = (subtitle.replace("_", " "));
                    message.sendMessage(Bukkit.getOnlinePlayers(), 0, 5, 2, title, subtitle);
                }
                if (Core.getLanguage().getBoolean(Language.LOG_COMMANDS))
                    Logger.log("titlemaker", sender.getName() + " executed the titlemaker command to:{" + args[0] + "} with the message Title:{" + msg + "}, SubTitle:{" + subtitle + "}");
            }
        } else if (args[0].startsWith("team=")) {
            Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(args[0].replaceFirst("team=", ""));
            if (team == null) {
                sender.sendMessage(Core.getLanguage().getString(Language.NO_TEAM, true).replace("%team%", args[0].replaceFirst("team=", "")));
                return;
            }
            if (args.length == 1) {
                sender.sendMessage("§cUsage: /titlemaker " + args[0] + " <title> [subtitle]");
            } else {
                String msg = ChatColor.translateAlternateColorCodes('&', args[1]);
                title = (msg.replace("_", " "));
                if (args.length == 2) {
                    for (String name : team.getEntries()) {
                        Player target = Bukkit.getPlayer(name);
                        if (target != null)
                            message.sendMessage(target, 0, 5, 2, title, subtitle);
                    }
                } else {
                    subtitle = ChatColor.translateAlternateColorCodes('&', args[2]);
                    subtitle = (subtitle.replace("_", " "));
                    for (String name : team.getEntries()) {
                        Player target = Bukkit.getPlayer(name);
                        if (target != null)
                            message.sendMessage(target, 0, 5, 2, title, subtitle);
                    }
                }
                if (Core.getLanguage().getBoolean(Language.LOG_COMMANDS))
                    Logger.log("titlemaker", sender.getName() + " executed the titlemaker command to:{" + args[0] + "} with the message Title:{" + msg + "}, SubTitle:{" + subtitle + "}");
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Core.getLanguage().getString(Language.MISSINGPLAYER, true));
                return;
            }
            if (args.length == 1) {
                sender.sendMessage("§cUsage: /titlemaker " + args[0] + " <title> [subtitle]");
            } else {
                String msg = ChatColor.translateAlternateColorCodes('&', args[1]);
                title = (msg.replace("_", " "));
                if (args.length == 2) {
                    message.sendMessage(target, 0, 5, 2, title, subtitle);

                } else {
                    subtitle = ChatColor.translateAlternateColorCodes('&', args[2]);
                    subtitle = (subtitle.replace("_", " "));
                    message.sendMessage(target, 0, 5, 2, title, subtitle);

                }
                if (Core.getLanguage().getBoolean(Language.LOG_COMMANDS))
                    Logger.log("titlemaker", sender.getName() + " executed the titlemaker command to:{" + args[0] + "} with the message Title:{" + msg + "}, SubTitle:{" + subtitle + "}");
            }
        }
    }

    @Override
    public void playerSendCommandEvent(Player player, String[] args) {
        if (!Perms.TITLE.has(player)) {
            player.sendMessage(Core.getLanguage().getString(Language.NOPERMISSION, true));
            return;
        }
        ITitleMessage message = Reflection.getTitleMessage();
        if (message == null) {
            player.sendMessage(Core.getLanguage().getString(Language.NOT_SUPPORTED, true));
            return;
        }
        String title;
        String subtitle = " ";

        if (args.length == 0) {
            ITellraw tellraw = Reflection.getTellraw("Usage: /titlemaker ");
            tellraw.color(ChatColor.RED);
            tellraw.then("<player> ");
            tellraw.color(ChatColor.RED);
            tellraw.tooltip("§7You can use §c'@a' §7for all online players", "§7Or you can use §c'team=(team name)'§7 to send the message", "§7to all scoreboard team members.");
            tellraw.then("<title>");
            tellraw.color(ChatColor.RED);
            tellraw.tooltip("§7This will set the title message", "§7Remember to replace spaces with and '_'");
            tellraw.then(" [subtitle]");
            tellraw.color(ChatColor.RED);
            tellraw.tooltip("§7This argument is optional you can", "§7leave it blank or put the text you want here", "§7Remember replace spaces with an '_'");
            tellraw.send(player);
        } else if (args[0].equalsIgnoreCase("@a")) {
            if (args.length == 1) {
                player.sendMessage("§cUsage: /titlemaker " + args[0] + " <title> [subtitle]");
            } else {
                String msg = ChatColor.translateAlternateColorCodes('&', args[1]);
                title = (msg.replace("_", " "));
                if (args.length == 2) {
                    message.sendMessage(Bukkit.getOnlinePlayers(), 0, 5, 2, title, subtitle);
                } else {
                    subtitle = ChatColor.translateAlternateColorCodes('&', args[2]);
                    subtitle = (subtitle.replace("_", " "));
                    message.sendMessage(Bukkit.getOnlinePlayers(), 0, 5, 2, title, subtitle);
                }
                if (Core.getLanguage().getBoolean(Language.LOG_COMMANDS))
                    Logger.log("titlemaker", player.getName() + " executed the titlemaker command to:{" + args[0] + "} with the message Title:{" + msg + "}, SubTitle:{" + subtitle + "}");
            }
        } else if (args[0].startsWith("team=")) {
            Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(args[0].replaceFirst("team=", ""));
            if (team == null) {
                player.sendMessage(Core.getLanguage().getString(Language.NO_TEAM, true).replace("%team%", args[0].replaceFirst("team=", "")));
                return;
            }
            if (args.length == 1) {
                player.sendMessage("§cUsage: /titlemaker " + args[0] + " <title> [subtitle]");
            } else {
                String msg = ChatColor.translateAlternateColorCodes('&', args[1]);
                title = (msg.replace("_", " "));
                if (args.length == 2) {
                    for (String name : team.getEntries()) {
                        Player target = Bukkit.getPlayer(name);
                        if (target != null)
                            message.sendMessage(target, 0, 5, 2, title, subtitle);
                    }
                } else {
                    subtitle = ChatColor.translateAlternateColorCodes('&', args[2]);
                    subtitle = (subtitle.replace("_", " "));
                    for (String name : team.getEntries()) {
                        Player target = Bukkit.getPlayer(name);
                        if (target != null)
                            message.sendMessage(target, 0, 5, 2, title, subtitle);
                    }
                }
                if (Core.getLanguage().getBoolean(Language.LOG_COMMANDS))
                    Logger.log("titlemaker", player.getName() + " executed the titlemaker command to:{" + args[0] + "} with the message Title:{" + msg + "}, SubTitle:{" + subtitle + "}");
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(Core.getLanguage().getString(Language.MISSINGPLAYER, true));
                return;
            }
            if (args.length == 1) {
                player.sendMessage("§cUsage: /titlemaker " + args[0] + " <title> [subtitle]");
            } else {
                String msg = ChatColor.translateAlternateColorCodes('&', args[1]);
                title = (msg.replace("_", " "));
                if (args.length == 2) {
                    message.sendMessage(target, 0, 5, 2, title, subtitle);

                } else {
                    subtitle = ChatColor.translateAlternateColorCodes('&', args[2]);
                    subtitle = (subtitle.replace("_", " "));
                    message.sendMessage(target, 0, 5, 2, title, subtitle);

                }
                if (Core.getLanguage().getBoolean(Language.LOG_COMMANDS))
                    Logger.log("titlemaker", player.getName() + " executed the titlemaker command to:{" + args[0] + "} with the message Title:{" + msg + "}, SubTitle:{" + subtitle + "}");
            }
        }
    }
}
