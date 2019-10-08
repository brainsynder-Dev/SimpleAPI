package simple.brainsynder.commands;

import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import simple.brainsynder.Core;
import simple.brainsynder.files.plugin_use.Language;

/**
 * This will be removed some time in the future, please use the new command system
 *
 * {@link CommandRegistry}
 */
@Deprecated
public abstract class BaseCommand implements CommandExecutor {
    @Getter private String commandName;

    public void consoleSendCommandEvent(CommandSender sender, String[] args) {
        sender.sendMessage("§cYou must be a player to run this command.");
    }

    public BaseCommand(String commandName) {
        this.commandName = commandName;
    }

    public Language getLanguage () {
        return Core.getLanguage();
    }

    public void registerCommand(JavaPlugin plugin) {
        plugin.getCommand(getCommandName()).setExecutor(this);
    }

    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
        if (var1 instanceof Player) {
            this.playerSendCommandEvent((Player) var1, var4);
        } else {
            this.consoleSendCommandEvent(var1, var4);
        }
        return true;
    }

    public void playerSendCommandEvent(Player player, String[] args){
        player.sendMessage("§cYou do not have privilege to run this command.");
    }

    public String messageMaker (String[] args, int start) {
        StringBuilder msgBuilder = new StringBuilder ();
        for (int i = start;i < args.length;i++) {
            msgBuilder.append (args[ i ]).append (" ");
        }
        return msgBuilder.toString ().trim ();
    }
}
