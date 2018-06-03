package simple.brainsynder.commands.list;

import org.bukkit.command.CommandSender;
import simple.brainsynder.commands.ParentCommand;
import simple.brainsynder.commands.annotations.ICommand;
import simple.brainsynder.commands.list.particle.ParticleList;
import simple.brainsynder.commands.list.particle.ParticleSpawn;

@ICommand(
        name = "particlemaker",
        description = "Send particles to your location."
)
public class CommandParticle extends ParentCommand {
    public CommandParticle() {
        registerSub(new ParticleList());
        registerSub(new ParticleSpawn());
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        sendHelp(sender, false);
    }
}
