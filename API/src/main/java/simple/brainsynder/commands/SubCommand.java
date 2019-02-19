package simple.brainsynder.commands;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.StringUtil;
import simple.brainsynder.commands.annotations.ICommand;
import simple.brainsynder.utils.Reflection;
import simple.brainsynder.utils.Return;

import java.util.*;

public class SubCommand implements CommandExecutor, TabCompleter {
    private Map<Integer, List<String>> tabCompletion = new HashMap<>();
    private Map<Integer, List<Complete>> tabCompletionArg = new HashMap<>();

    public void run(CommandSender sender) {
        run(sender, new String[0]);
    }

    public void run(CommandSender sender, String[] args) {
        run(sender);
    }

    protected void registerCompletion(int length, List<String> replacements) {
        Validate.notNull(replacements, "Arguments cannot be null");
        tabCompletion.put(length, replacements);
    }

    protected void registerCompletion(int length, Complete complete) {
        Validate.notNull(complete, "Arguments cannot be null");
        List<Complete> completes = tabCompletionArg.getOrDefault(length, new ArrayList<>());
        completes.add(complete);
        tabCompletionArg.put(length, completes);
    }

    public void sendUsage(CommandSender sender) {
        ICommand command = getCommand(getClass());
        if (command == null) return;
        if (command.usage().isEmpty()) return;
        String usage = ChatColor.translateAlternateColorCodes('&', command.usage());
        String description = ChatColor.translateAlternateColorCodes('&', command.description());

        if (sender instanceof Player) {
            Reflection.getTellraw(usage).tooltip(ChatColor.GRAY + description).send((Player) sender);
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', command.usage()));
        }
    }

    public ICommand getCommand(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(ICommand.class)) return null;
        return clazz.getAnnotation(ICommand.class);
    }

    public Map<Integer, List<String>> getTabCompletion() {
        return tabCompletion;
    }

    public void tabComplete(List<String> completions, CommandSender sender, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        if ((!tabCompletion.isEmpty()) || (!tabCompletionArg.isEmpty())) {
            int length = args.length;
            String toComplete = args[length - 1].toLowerCase(Locale.ENGLISH);

            List<String> replacements = tabCompletion.getOrDefault(length, new ArrayList<>());
            if ((length - 2) >= 0) {
                List<Complete> completes = tabCompletionArg.getOrDefault(length, new ArrayList<>());
                if (!completes.isEmpty()) {
                    for (Complete complete : completes) {
                        List<String> replace = new ArrayList<>();
                        if (complete.handleReplacement(sender, replace, args[length-2].toLowerCase(Locale.ENGLISH))){
                            replacements = replace;
                            break;
                        }
                    }
                }
            }
            for (String command : replacements) {
                if (command.isEmpty()) continue;
                if (StringUtil.startsWithIgnoreCase(command, toComplete)) {
                    completions.add(command);
                }
            }
        }
    }

    public boolean canExecute(CommandSender sender) {
        return true;
    }

    public String messageMaker(String[] args, int start) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }
        return builder.toString().trim();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            if (canExecute(sender)) run(sender);
        } else {
            if (canExecute(sender)) run(sender, args);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        List<String> completions = new ArrayList();
        tabComplete(completions, sender, args);
        if (!completions.isEmpty()) return completions;
        return new ArrayList<>();
    }

    String[] newArgs(String[] args) {
        ArrayList<String> newArgs = new ArrayList<>();
        Collections.addAll(newArgs, args);
        newArgs.remove(0);
        return newArgs.toArray(new String[newArgs.size()]);
    }

    protected interface Complete {
        boolean handleReplacement (CommandSender sender, List<String> replacements, String name);
    }

    /**
     * Will target the player/players specified as the argument
     *
     * <br>
     * Allowed Arguments: <Customizable>
     *
     * @param argument targeted player(s)
     * @param value fetched player(s)
     */
    public void send(Argument argument, Return<Player> value) {
        List<String> targets = new ArrayList<>();
        argument.target(targets);
        targets.forEach(arg -> send(arg, value));
    }

    /**
     * Will target the player/players specified as the argument
     *
     * <br>
     * Allowed Arguments: <Customizable>
     *
     * @param argument targeted player(s)
     * @param value fetched player(s)
     * @param type return type
     *             SUCCESS
     *             NO_PLAYER <If there was no player found>
     *             NO_TEAM <If the team selected was not found>
     */
    public void send(Argument argument, Return<Player> value, Return<ReturnType> type) {
        List<String> targets = new ArrayList<>();
        argument.target(targets);
        targets.forEach(arg -> send(arg, value, type));
    }

    /**
     * Will target the player/players specified as the argument
     *
     * <br>
     * Allowed Arguments:
     * - @a (all players)
     * - team=<scoreboard team> (Will target a certain team and all of its members)
     * - <name> (Will target the selected player)
     * - <uuid> (Will target the selected player)
     *
     * @param argument targeted player(s)
     * @param value fetched player(s)
     */
    public void send(String argument, Return<Player> value){
        send(argument, value, value1 -> {});
    }

    /**
     * Will target the player/players specified as the argument
     *
     * <br>
     * Allowed Arguments:
     * - @a (all players)
     * - team=<scoreboard team> (Will target a certain team and all of its members)
     * - <name> (Will target the selected player)
     * - <uuid> (Will target the selected player)
     *
     * @param argument targeted player(s)
     * @param value fetched player(s)
     * @param type return type
     *             SUCCESS
     *             NO_PLAYER <If there was no player found>
     *             NO_TEAM <If the team selected was not found>
     */
    public void send(String argument, Return<Player> value, Return<ReturnType> type) {
        if (argument.equals("@a")) {
            type.run(ReturnType.SUCCESS);
            Bukkit.getOnlinePlayers().forEach(value::run);
            return;
        }

        // Checks if the argument is a team selector
        if (argument.startsWith("team=")) {
            Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(argument.replaceFirst("team=", ""));
            if (team == null) {
                type.run(ReturnType.NO_TEAM);
                return;
            }
            type.run(ReturnType.SUCCESS);
            for (String name : team.getEntries()) {
                Player target = Bukkit.getPlayer(name);
                if (target != null) value.run(target);
            }
            return;
        }

        // Checks if the argument is a UUID
        try {
            UUID uuid = UUID.fromString(argument);
            Player target = Bukkit.getPlayer(uuid);
            if (target == null) {
                type.run(ReturnType.NO_PLAYER);
                return;
            }
            type.run(ReturnType.SUCCESS);
            value.run(target);
            return;
        }catch (Exception ignored) {}

        // Checks if the argument is a player name
        Player target = Bukkit.getPlayer(argument);
        if (target == null) {
            type.run(ReturnType.NO_PLAYER);
            return;
        }
        type.run(ReturnType.SUCCESS);
        value.run(target);
    }

    public enum ReturnType {
        SUCCESS,
        NO_TEAM,
        NO_PLAYER
    }

    public interface Argument {
        void target (List<String> targets);
    }

    public List<String> getOnlinePlayers () {
        List<String> list = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> list.add(player.getName()));
        return list;
    }
}
