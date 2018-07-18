package simple.brainsynder.commands;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import simple.brainsynder.commands.annotations.ICommand;
import simple.brainsynder.utils.Reflection;

import java.util.*;

public class SubCommand implements CommandExecutor, TabCompleter {
    private Map<Integer, List<String>> tabCompletion = new HashMap<>();

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
        if (!tabCompletion.isEmpty()) {
            int length = args.length;
            if (!tabCompletion.containsKey(length)) return;
            List<String> replacements = tabCompletion.getOrDefault(length, new ArrayList<>());
            String toComplete = args[length - 1].toLowerCase(Locale.ENGLISH);
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
}
