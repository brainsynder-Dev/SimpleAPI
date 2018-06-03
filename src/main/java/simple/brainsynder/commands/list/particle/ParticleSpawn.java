package simple.brainsynder.commands.list.particle;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simple.brainsynder.Core;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.commands.SubCommand;
import simple.brainsynder.commands.annotations.ICommand;
import simple.brainsynder.files.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ICommand (
        name = "spawn",
        usage = "§8[§7~§8] §e/particlemaker §7spawn §e<particle> §c[count] §c[speed] §c[offset]",
        description = "Lists all the particles."
)
public class ParticleSpawn extends SubCommand {
    public ParticleSpawn() {
        registerCompletion(1, fetchList());
        registerCompletion(2, Arrays.asList("10"));
        registerCompletion(3, Arrays.asList("0.0"));
        registerCompletion(4, Arrays.asList("0.0"));
    }

    @Override
    public boolean canExecute(CommandSender sender) {
        return sender.hasPermission("SimpleAPI.command.particle.spawn");
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to do this command.");
            return;
        }

        //TODO: Get particle...
        if (args.length == 0) {
            sendUsage(sender);
            return;
        }
        ParticleMaker.Particle particle = null;
        for (ParticleMaker.Particle particles : ParticleMaker.Particle.values()) {
            if (particles.isCompatable()) {
                if (particles.getName().equalsIgnoreCase(args[0])) {
                    particle = particles;
                }
            }
        }
        if (particle == null) {
            sender.sendMessage(Core.getLanguage().getString(Language.NO_PARTICLE, true));
            return;
        }

        //TODO: Particle Amount
        if (args.length == 1) {
            ParticleMaker maker = new ParticleMaker(particle, 1, 0.0);
            maker.sendToLocation(((Player)sender).getLocation());
            return;
        }
        int amount = 1;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Core.getLanguage().getString(Language.INVALID_NUMBER, true));
            return;
        }

        //TODO: Particle Speed
        if (args.length == 2) {
            ParticleMaker maker = new ParticleMaker(particle, amount, 0.0);
            maker.sendToLocation(((Player)sender).getLocation());
            return;
        }
        double speed = 0.0;
        try {
            speed = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Core.getLanguage().getString(Language.INVALID_NUMBER, true));
            return;
        }

        //TODO: Particle Offsets
        if (args.length == 3) {
            ParticleMaker maker = new ParticleMaker(particle, speed, amount, 0.0);
            maker.sendToLocation(((Player)sender).getLocation());
            return;
        }

        try {
            double offset = Double.parseDouble(args[3]);
            ParticleMaker maker = new ParticleMaker(particle, speed, amount, offset);
            maker.sendToLocation(((Player)sender).getLocation());
        } catch (NumberFormatException e) {
            if (!args[3].contains(",")) {
                sender.sendMessage(Core.getLanguage().getString(Language.INVALID_NUMBER, true));
                return;
            }
            String[] offsets = args[3].split(",");
            if (offsets.length != 3) {
                sender.sendMessage(Core.getLanguage().getString(Language.MISSING_OFFSETS, true));
                return;
            }
            double offsetX = Double.parseDouble (offsets[0]);
            double offsetY = Double.parseDouble (offsets[1]);
            double offsetZ = Double.parseDouble (offsets[2]);
            ParticleMaker maker = new ParticleMaker(particle, amount, offsetX, offsetY, offsetZ);
            maker.sendToLocation(((Player)sender).getLocation());

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
