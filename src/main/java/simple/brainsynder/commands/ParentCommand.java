package simple.brainsynder.commands;

import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import simple.brainsynder.commands.annotations.ICommand;

import java.util.*;

public class ParentCommand extends SubCommand {
    protected boolean overrideTab = false;
    private List<SubCommand> subCommands = new ArrayList<>();

    public void sendHelp (CommandSender sender, boolean parent) {
        if (!subCommands.isEmpty())
            subCommands.forEach(subCommand -> subCommand.sendUsage(sender));
        if (parent) sendUsage(sender);
    }

    @Override
    public void tabComplete(List<String> completions, CommandSender sender, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        if (!subCommands.isEmpty()) {
            if (!overrideTab) {
                if (args.length == 1) {
                    String toComplete = args[0].toLowerCase(Locale.ENGLISH);
                    for (SubCommand command : subCommands) {
                        if (!command.getClass().isAnnotationPresent(ICommand.class)) continue;
                        if (!command.canExecute(sender)) continue;
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
        super.tabComplete(completions, sender, args);
    }

    protected void registerSub(SubCommand subCommand) {
        subCommands.add(subCommand);
    }

    protected List<SubCommand> getSubCommands() {
        return subCommands;
    }

    private SubCommand parse (String name) {
        SubCommand sub = null;
        for (SubCommand command : subCommands) {
            if (!command.getClass().isAnnotationPresent(ICommand.class)) continue;
            ICommand annotation = command.getClass().getAnnotation(ICommand.class);
            if (annotation.name().isEmpty()) continue;
            if ((!annotation.name().equalsIgnoreCase(name))
                    && (!Arrays.asList(annotation.alias()).contains(name))) continue;
            sub = command;
            break;
        }
        return sub;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        try {
            if (args.length == 0) {
                if (canExecute(sender)) run(sender);
            } else {
                String args0 = args[0];
                SubCommand sub = parse(args0);
                if (sub == null) {
                    if (canExecute(sender)) run(sender, args);
                    return false;
                }
                if (sub.canExecute(sender)) sub.run(sender, newArgs(args));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        List<String> completions = new ArrayList();
        tabComplete(completions, sender, args);
        if (!completions.isEmpty()) return completions;

        if (args.length >= 1) {
            String args0 = args[0];
            SubCommand sub = null;
            for (SubCommand command : subCommands) {
                if (!command.getClass().isAnnotationPresent(ICommand.class)) continue;
                ICommand annotation = command.getClass().getAnnotation(ICommand.class);
                if (annotation.name().isEmpty()) continue;
                if ((!annotation.name().equalsIgnoreCase(args0))
                        && (!Arrays.asList(annotation.alias()).contains(args0))) continue;
                sub = command;
                break;
            }

            if (sub != null) {
                ArrayList<String> newArgs = new ArrayList<>();
                Collections.addAll(newArgs, args);
                newArgs.remove(0);
                String[] arguments = newArgs.toArray(new String[newArgs.size()]);
                sub.tabComplete(completions, sender, arguments);
                if (!completions.isEmpty()) return completions;
            }
        }

        return new ArrayList<>();
    }

}
