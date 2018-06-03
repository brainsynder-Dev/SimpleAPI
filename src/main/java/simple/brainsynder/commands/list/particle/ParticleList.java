package simple.brainsynder.commands.list.particle;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.commands.SubCommand;
import simple.brainsynder.commands.annotations.ICommand;
import simple.brainsynder.nms.ITellraw;
import simple.brainsynder.utils.Reflection;

import java.util.ArrayList;
import java.util.List;

@ICommand (
        name = "list",
        usage = "§8[§7~§8] §e/particlemaker §7list",
        description = "Lists all the particles."
)
public class ParticleList extends SubCommand {
    public ParticleList () {
        registerCompletion(1, fetchList());
    }

    @Override
    public boolean canExecute(CommandSender sender) {
        return sender.hasPermission("SimpleAPI.command.particle.list");
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            ITellraw tellraw = Reflection.getTellraw("§bParticleList §9(§7" + ParticleMaker.Particle.values().length + "§9)§b: ");
            int i = 1;
            for (ParticleMaker.Particle particle : ParticleMaker.Particle.values()) {
                if (particle.isCompatable()) {
                    tellraw.then(particle.getName());
                    tellraw.color(ChatColor.GRAY);
                    tellraw.tooltip("§7Click here to auto", "§7insert this into the command");
                    tellraw.suggest("/particlemaker " + particle.getName() + " 1 0.0,0.0,0.0");
                }else {
                    tellraw.then(particle.getName());
                    tellraw.color(ChatColor.RED);
                    tellraw.tooltip("§cNot supported in your", "§ccurrent server version", "§cUse server version: §7" + particle.getAllowedVersion());
                }
                if ((ParticleMaker.Particle.values().length ) != i) {
                    tellraw.then(", ");
                    tellraw.color(ChatColor.AQUA);
                }
                i++;
            }
            tellraw.send ((Player) sender);
        }else{
            StringBuilder builder = new StringBuilder();
            for (String name : fetchList()) {
                builder.append(name).append(", ");
            }
            String string = builder.toString();
            sender.sendMessage(string.substring(0, (string.length()-2)));
        }
    }

    private List<String> fetchList () {
        List<String> list = new ArrayList<>();
        for (ParticleMaker.Particle particle : ParticleMaker.Particle.values()) {
            if (particle.isCompatable()) list.add(particle.getName());
        }
        return list;
    }
}
