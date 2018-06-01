package simple.brainsynder.commands;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import simple.brainsynder.commands.annotations.ICommand;

import java.lang.reflect.Field;
import java.util.*;

public class CommandRegistry<P extends Plugin> {
    private P plugin;
    private static Map<String, List<Command>> commands;

    public CommandRegistry(P plugin) {
        this.plugin = plugin;
        commands = new HashMap<>();
        commands.put(plugin.getName(), new ArrayList<>());
    }

    public void register(ParentCommand commandCore) throws Exception {
        Class<?> clazz = commandCore.getClass();
        if (!clazz.isAnnotationPresent(ICommand.class))
            throw new Exception(clazz.getSimpleName() + " Missing ICommand.class");
        ICommand annotation = clazz.getAnnotation(ICommand.class);
        if (annotation.name().isEmpty()) throw new Exception(clazz.getSimpleName() + " missing name");
        Command command = new Command0(commandCore, annotation);
        register(annotation.name(), command);
    }

    private class Command0 extends Command {
        private ParentCommand core = null;
        private List<SubCommand> subCommands = new ArrayList<>();

        private Command0(ParentCommand commandClass, ICommand command) {
            this(command);
            this.core = commandClass;
            subCommands = core.getSubCommands();
        }

        private Command0(ICommand command) {
            super(command.name());
            if (!command.description().isEmpty())
                setDescription(ChatColor.translateAlternateColorCodes('&', command.description()));
            if (!command.usage().isEmpty())
                setUsage(ChatColor.translateAlternateColorCodes('&', command.usage()));
            setAliases(Arrays.asList(command.alias()));
        }

        @Override
        public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
            Validate.notNull(sender, "Sender cannot be null");
            Validate.notNull(args, "Arguments cannot be null");
            Validate.notNull(alias, "Alias cannot be null");
            List<String> completions = new ArrayList();
            core.tabComplete(completions, sender, args);
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

            return super.tabComplete(sender, alias, args);
        }

        @Override
        public boolean execute(CommandSender sender, String s, String[] args) {
            if (core == null) return false;
            try {
                if (args.length == 0) {
                    if (core.canExecute(sender)) core.run(sender);
                } else {
                    String args0 = args[0];
                    SubCommand sub = parse(args0);
                    if (sub == null) {
                        if (core.canExecute(sender)) core.run(sender, args);
                        return false;
                    }
                    if (sub.canExecute(sender)) sub.run(sender, newArgs(args));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
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

        private String[] newArgs (String[] args) {
            ArrayList<String> newArgs = new ArrayList<>();
            Collections.addAll(newArgs, args);
            newArgs.remove(0);
            return newArgs.toArray(new String[newArgs.size()]);
        }
    }

    private void register(String fallback, Command command) {
        List<Command> list = commands.getOrDefault(plugin.getName(), new ArrayList<>());
        list.add(command);
        commands.put(plugin.getName(), list);

        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register(fallback, command);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
    }
}
