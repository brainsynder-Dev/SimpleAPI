package simple.brainsynder.commands.list;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import simple.brainsynder.commands.ParentCommand;
import simple.brainsynder.commands.annotations.ICommand;
import simple.brainsynder.nms.ITellraw;
import simple.brainsynder.utils.Reflection;
import simple.brainsynder.utils.SpigotPluginHandler;

import java.util.ArrayList;
import java.util.List;

@ICommand(
        name = "simpleapi",
        description = "Checks if any of the plugins that use SimpleAPI need updating"
)
public class CommandSimpleAPI extends ParentCommand {

    @Override
    public void run(CommandSender sender) {
        sender.sendMessage("§bSimpleAPI §9>> §7Looking for updates...");
        try {
            List<Boolean> updates = new ArrayList<>();
            SpigotPluginHandler.updaterMap.values().forEach(handler -> {
                PluginDescriptionFile pdf = handler.getPlugin().getDescription();
                ITellraw tellraw = Reflection.getTellraw("§9• §b" + pdf.getName() + " >> Hover for more info");
                tellraw.color(ChatColor.AQUA);
                String downloadURL = "http://spigotmc.org//";
                if (handler.getId() != 0) downloadURL = "https://spigotmc.org/resources/" + handler.getId() + "/";
                tellraw.link(downloadURL);

                String finalDownloadURL = downloadURL;
                handler.needsUpdate((needsUpdate, version) -> {
                    tellraw.tooltip(
                            "§7A new version of §b" + pdf.getName() + "§7 is Out!",
                            "§7Version §b" + version + "§7, current version running is version §b" + pdf.getVersion(),
                            "§7Update has §b" + handler.getDownloads() + "§7 download(s)",
                            "§7It would be wise to check this update out.",
                            "§7Click this text to go to the spigot page.");
                    updates.add(true);
                    if (sender instanceof Player) {
                        tellraw.send((Player) sender);
                    } else {
                        sender.sendMessage("§bSimpleAPI §9>> §7An update was found for " + pdf.getName() + "!");
                        sender.sendMessage("§bSimpleAPI §9>> §7 Current Version: " + pdf.getVersion());
                        sender.sendMessage("§bSimpleAPI §9>> §7 New Version: " + version);
                        sender.sendMessage("§bSimpleAPI §9>> §7 Download at: " + finalDownloadURL);

                    }
                });
            });
        } catch (Exception e) {
            sender.sendMessage("§bSimpleAPI §9>> §7All plugins using SimpleAPIs' updater are up to date");
        }
    }

    @Override
    public boolean canExecute(CommandSender sender) {
        return sender.hasPermission("SimpleAPI.command.updater");
    }
}
