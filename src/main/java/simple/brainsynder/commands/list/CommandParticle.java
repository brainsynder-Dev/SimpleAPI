package simple.brainsynder.commands.list;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import simple.brainsynder.Core;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.commands.BaseCommand;
import simple.brainsynder.files.Language;
import simple.brainsynder.nms.ITellraw;
import simple.brainsynder.utils.Perms;
import simple.brainsynder.utils.Reflection;

public class CommandParticle extends BaseCommand {
    public CommandParticle() {
        super("particlemaker");
    }

    private void sendHelp(Player player) {
        ITellraw tellraw = Reflection.getTellraw("/particlemaker ");
        tellraw.color(ChatColor.RED);
        tellraw.then("§c<§7particle§c>");
        tellraw.color(ChatColor.GRAY);
        tellraw.command("/particlemaker list");
        tellraw.tooltip(
                "§7The name of the particle to send at your location",
                "§7Click here to see a list of particles"
        );
        tellraw.then(" §c<§7amount§c>");
        tellraw.tooltip(
                "§7The amount of particles sent to your location"
        );
        tellraw.then(" §c[§7offsetX§c,§7offsetY§c,§7offsetZ§c]");
        tellraw.color(ChatColor.GRAY);
        tellraw.tooltip(
                "§7This argument is optional",
                "§7Must have the 3 values",
                "§7These 3 values must be setup like: #.#"
        );
        tellraw.send(player);
    }

    @Override
    public void playerSendCommandEvent(Player player, String[] args) {
        if (!Perms.PARTICLE.has(player)) {
            player.sendMessage(Core.getLanguage().getString(Language.NOPERMISSION, true));
            return;
        }
        if (args.length == 0) {
            sendHelp(player);
        } else if (args[0].equalsIgnoreCase("list")) {
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
            tellraw.send (player);
        } else {
            ParticleMaker.Particle particle = null;
            for (ParticleMaker.Particle particles : ParticleMaker.Particle.values()) {
                if (particles.isCompatable()) {
                    if (particles.getName().equalsIgnoreCase(args[0])) {
                        particle = particles;
                    }
                }
            }
            if (particle == null) {
                player.sendMessage(Core.getLanguage().getString(Language.NO_PARTICLE, true));
                return;
            }
            if (args.length == 1) {
                sendHelp(player);
            } else {
                int amount = 0;
                try {
                    amount = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage(Core.getLanguage().getString(Language.INVALID_NUMBER, true));
                }
                if (args.length == 2) {
                    ParticleMaker maker = new ParticleMaker(particle, amount, 0.0);
                    maker.sendToLocation(player.getLocation());
                } else {
                    String[] offsets = args[2].split(",");
                    if (offsets.length != 3) {
                        player.sendMessage(Core.getLanguage().getString(Language.MISSING_OFFSETS, true));
                        return;
                    }
                    double offsetX = Double.parseDouble (offsets[0]);
                    double offsetY = Double.parseDouble (offsets[1]);
                    double offsetZ = Double.parseDouble (offsets[2]);
                    ParticleMaker maker = new ParticleMaker(particle, amount, offsetX, offsetY, offsetZ);
                    maker.sendToLocation(player.getLocation());
                }
            }
        }
    }
}
