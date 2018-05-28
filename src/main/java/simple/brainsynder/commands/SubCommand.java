package simple.brainsynder.commands;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import simple.brainsynder.commands.annotations.ICommand;

import java.util.*;

public class SubCommand {
    protected boolean overrideTab = false;
    private List<SubCommand> subCommands = new ArrayList<>();
    private Map<Integer, List<String>> tabCompletion = new HashMap<>();

    public void run(CommandSender sender) {
        run(sender, new String[0]);
    }

    public void run(CommandSender sender, String[] args) {
        run(sender);
    }

    protected void registerCompletion (int length, List<String> replacements) {
        Validate.notNull(replacements, "Arguments cannot be null");
        tabCompletion.put(length, replacements);
    }

    public void sendUsage(CommandSender sender) {
        if (!getClass().isAnnotationPresent(ICommand.class)) return;
        ICommand command = getClass().getAnnotation(ICommand.class);
        if (command.usage().isEmpty()) return;
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', command.usage()));
    }

    public Map<Integer, List<String>> getTabCompletion() {
        return tabCompletion;
    }

    public void tabComplete(List<String> completions, CommandSender sender, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        if (!subCommands.isEmpty()) {
            if (!overrideTab) {
                if (args.length == 1) {
                    String toComplete = args[0].toLowerCase(Locale.ENGLISH);
                    for (SubCommand command : subCommands) {
                        if (!command.getClass().isAnnotationPresent(ICommand.class)) continue;
                        ICommand annotation = command.getClass().getAnnotation(ICommand.class);
                        if (annotation.name().isEmpty()) continue;
                        if (StringUtil.startsWithIgnoreCase(annotation.name(), toComplete)) {
                            completions.add(annotation.name());
                        }
                        Arrays.asList(annotation.alias()).forEach(name -> {
                            if (StringUtil.startsWithIgnoreCase(name, toComplete)) {
                                completions.add(name);
                            }
                        });
                    }
                }
            }
        }

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

    public String messageMaker(String[] args, int start) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }
        return builder.toString().trim();
    }

    protected void registerSub(SubCommand subCommand) {
        subCommands.add(subCommand);
    }

    protected List<SubCommand> getSubCommands() {
        return subCommands;
    }
}
