package simple.brainsynder.commands;

import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import simple.brainsynder.commands.annotations.ICommand;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ParentCommand extends SubCommand {

    @Override
    public void tabComplete(List<String> completions, CommandSender sender, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        if (!getSubCommands().isEmpty()) {
            if (!overrideTab) {
                if (args.length == 1) {
                    String toComplete = args[0].toLowerCase(Locale.ENGLISH);
                    for (SubCommand command : getSubCommands()) {
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
        super.tabComplete(completions, sender, args);
    }
}
