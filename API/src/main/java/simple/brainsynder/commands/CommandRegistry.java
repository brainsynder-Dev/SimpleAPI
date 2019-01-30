package simple.brainsynder.commands;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import simple.brainsynder.commands.annotations.ICommand;

import java.util.Arrays;

public class CommandRegistry<P extends JavaPlugin> {
    private P plugin;

    public CommandRegistry(P plugin) {
        this.plugin = plugin;
    }

    public void register(ParentCommand commandCore) throws Exception {
        Class<?> clazz = commandCore.getClass();
        if (!clazz.isAnnotationPresent(ICommand.class))
            throw new Exception(clazz.getSimpleName() + " Missing ICommand.class");
        ICommand annotation = clazz.getAnnotation(ICommand.class);
        if (annotation.name().isEmpty()) throw new Exception(clazz.getSimpleName() + " missing name");

        PluginCommand command = plugin.getCommand(annotation.name());
        command.setExecutor(commandCore);
        command.setTabCompleter(commandCore);
        if (!annotation.usage().isEmpty()) command.setUsage(annotation.usage());
        if (!annotation.description().isEmpty()) command.setDescription(annotation.description());
        if (annotation.alias().length != 0) command.setAliases(Arrays.asList(annotation.alias()));
    }
}
