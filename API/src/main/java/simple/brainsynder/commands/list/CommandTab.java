package simple.brainsynder.commands.list;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import simple.brainsynder.Core;
import simple.brainsynder.commands.BaseCommand;
import simple.brainsynder.files.plugin_use.Language;
import simple.brainsynder.nms.ITabMessage;
import simple.brainsynder.nms.ITellraw;
import simple.brainsynder.utils.Reflection;
import simple.brainsynder.utils.plugin_use.Logger;
import simple.brainsynder.utils.plugin_use.Perms;

public class CommandTab extends BaseCommand {
    public CommandTab() {
        super("tab");
    }

    @Override
    public void consoleSendCommandEvent(CommandSender sender, String[] args) {
        ITabMessage message = Reflection.getTabMessage();
        if (message == null) {
            sender.sendMessage(Core.getLanguage().getString(Language.NOT_SUPPORTED, true));
            return;
        }
        if (args.length == 0) {
            sender.sendMessage("§cUsage: /tab <player> <header> [footer]");
        } else if (args[0].equalsIgnoreCase("@a")) {
            if (args.length == 1) {
                sender.sendMessage("§cUsage: /tab " + args[0] + " <header> [footer]");
            } else {
                String msg = ChatColor.translateAlternateColorCodes('&', args[1]);
                String footer = " ";
                message.setHeader(msg.replace("_", " "));
                if (args.length == 2) {
                    message.setFooter(footer);
                    message.send(Bukkit.getOnlinePlayers());
                } else {
                    footer = ChatColor.translateAlternateColorCodes('&', args[2]);
                    message.setFooter(footer.replace("_", " "));
                    message.send(Bukkit.getOnlinePlayers());
                }
                if (Core.getLanguage().getBoolean(Language.LOG_COMMANDS))
                    Logger.log("tab", sender.getName() + " executed the Tab command to:{" + args[0] + "} with the message Header:{" + msg + "}, Footer:{" + footer + "}");
            }
        } else if (args[0].startsWith("team=")) {
            Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(args[0].replaceFirst("team=", ""));
            if (team == null) {
                sender.sendMessage(Core.getLanguage().getString(Language.NO_TEAM, true).replace("%team%", args[0].replaceFirst("team=", "")));
                return;
            }
            if (args.length == 1) {
                sender.sendMessage("§cUsage: /tab " + args[0] + " <header> [footer]");
            } else {
                String msg = ChatColor.translateAlternateColorCodes('&', args[1]);
                String footer = " ";
                message.setHeader(msg.replace("_", " "));
                if (args.length == 2) {
                    message.setFooter(footer);
                    for (String name : team.getEntries()) {
                        Player target = Bukkit.getPlayer(name);
                        if (target != null)
                            message.send(target);
                    }
                } else {
                    footer = ChatColor.translateAlternateColorCodes('&', args[2]);
                    message.setFooter(footer.replace("_", " "));
                    for (String name : team.getEntries()) {
                        Player target = Bukkit.getPlayer(name);
                        if (target != null)
                            message.send(target);
                    }
                }
                if (Core.getLanguage().getBoolean(Language.LOG_COMMANDS))
                    Logger.log("tab", sender.getName() + " executed the Tab command to:{" + args[0] + "} with the message Header:{" + msg + "}, Footer:{" + footer + "}");
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Core.getLanguage().getString(Language.MISSINGPLAYER, true));
                return;
            }
            if (args.length == 1) {
                sender.sendMessage("§cUsage: /tab " + args[0] + " <header> [footer]");
            } else {
                String msg = ChatColor.translateAlternateColorCodes('&', args[1]);
                String footer = " ";
                message.setHeader(msg.replace("_", " "));
                if (args.length == 2) {
                    message.setFooter(footer);
                    message.send(target);
                } else {
                    footer = ChatColor.translateAlternateColorCodes('&', args[2]);
                    message.setFooter(footer.replace("_", " "));
                    message.send(target);
                }
                if (Core.getLanguage().getBoolean(Language.LOG_COMMANDS))
                    Logger.log("tab", sender.getName() + " executed the Tab command to:{" + args[0] + "} with the message Header:{" + msg + "}, Footer:{" + footer + "}");
            }
        }
    }

    @Override
    public void playerSendCommandEvent(Player player, String[] args) {
        if (!Perms.TAB.has(player)) {
            player.sendMessage(Core.getLanguage().getString(Language.NOPERMISSION, true));
            return;
        }
        ITabMessage message = Reflection.getTabMessage();
        if (message == null) {
            player.sendMessage(Core.getLanguage().getString(Language.NOT_SUPPORTED, true));
            return;
        }
        if (args.length == 0) {
            ITellraw tellraw = Reflection.getTellraw("Usage: /tab ");
            tellraw.color(ChatColor.RED);
            tellraw.then("<player> ");
            tellraw.color(ChatColor.RED);
            tellraw.tooltip("§7You can use §c'@a' §7for all online players", "§7Or you can use §c'team=(team name)'§7 to send the message", "§7to all scoreboard team members.");
            tellraw.then("<header>");
            tellraw.color(ChatColor.RED);
            tellraw.tooltip("§7This will set the header message", "§7Remember to replace spaces with and '_'");
            tellraw.then(" [footer]");
            tellraw.color(ChatColor.RED);
            tellraw.tooltip("§7This argument is optional you can", "§7leave it blank or put the text you want here", "§7Remember replace spaces with an '_'");
            tellraw.send(player);
        } else if (args[0].equalsIgnoreCase("@a")) {
            if (args.length == 1) {
                player.sendMessage("§cUsage: /tab " + args[0] + " <header> [footer]");
            } else {
                String msg = ChatColor.translateAlternateColorCodes('&', args[1]);
                String footer = " ";
                message.setHeader(msg.replace("_", " "));
                if (args.length == 2) {
                    message.setFooter(footer);
                    message.send(Bukkit.getOnlinePlayers());
                } else {
                    footer = ChatColor.translateAlternateColorCodes('&', args[2]);
                    message.setFooter(footer.replace("_", " "));
                    message.send(Bukkit.getOnlinePlayers());
                }
                if (Core.getLanguage().getBoolean(Language.LOG_COMMANDS))
                    Logger.log("tab", player.getName() + " executed the Tab command to:{" + args[0] + "} with the message Header:{" + msg + "}, Footer:{" + footer + "}");
            }
        } else if (args[0].startsWith("team=")) {
            Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(args[0].replaceFirst("team=", ""));
            if (team == null) {
                player.sendMessage(Core.getLanguage().getString(Language.NO_TEAM, true).replace("%team%", args[0].replaceFirst("team=", "")));
                return;
            }
            if (args.length == 1) {
                player.sendMessage("§cUsage: /tab " + args[0] + " <header> [footer]");
            } else {
                String msg = ChatColor.translateAlternateColorCodes('&', args[1]);
                String footer = " ";
                message.setHeader(msg.replace("_", " "));
                if (args.length == 2) {
                    message.setFooter(footer);
                    for (String name : team.getEntries()) {
                        Player target = Bukkit.getPlayer(name);
                        if (target != null)
                            message.send(target);
                    }
                } else {
                    footer = ChatColor.translateAlternateColorCodes('&', args[2]);
                    message.setFooter(footer.replace("_", " "));
                    for (String name : team.getEntries()) {
                        Player target = Bukkit.getPlayer(name);
                        if (target != null)
                        message.send(target);
                    }
                }
                if (Core.getLanguage().getBoolean(Language.LOG_COMMANDS))
                    Logger.log("tab", player.getName() + " executed the Tab command to:{" + args[0] + "} with the message Header:{" + msg + "}, Footer:{" + footer + "}");
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(Core.getLanguage().getString(Language.MISSINGPLAYER, true));
                return;
            }
            if (args.length == 1) {
                player.sendMessage("§cUsage: /tab " + args[0] + " <header> [footer]");
            } else {
                String msg = ChatColor.translateAlternateColorCodes('&', args[1]);
                String footer = " ";
                message.setHeader(msg.replace("_", " "));
                if (args.length == 2) {
                    message.setFooter(footer);
                    message.send(target);
                } else {
                    footer = ChatColor.translateAlternateColorCodes('&', args[2]);
                    message.setFooter(footer.replace("_", " "));
                    message.send(target);
                }
                if (Core.getLanguage().getBoolean(Language.LOG_COMMANDS))
                    Logger.log("tab", player.getName() + " executed the Tab command to:{" + args[0] + "} with the message Header:{" + msg + "}, Footer:{" + footer + "}");
            }
        }
    }
}
