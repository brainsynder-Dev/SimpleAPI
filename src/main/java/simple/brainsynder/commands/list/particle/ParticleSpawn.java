package simple.brainsynder.commands.list.particle;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simple.brainsynder.Core;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.commands.SubCommand;
import simple.brainsynder.commands.annotations.ICommand;
import simple.brainsynder.files.Language;
import simple.brainsynder.nms.IParticleSender;
import simple.brainsynder.wrappers.ColorWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ICommand (
        name = "spawn",
        usage = "§8[§7~§8] §e/particlemaker §7spawn §e<particle> §c[data] §c[count] §c[speed] §c[offset]",
        description = "Spawns the selected particle (data is not required unless the particle needs it)"
)
public class ParticleSpawn extends SubCommand {
    public ParticleSpawn() {
        registerCompletion(1, fetchList());
        registerCompletion(2, Arrays.asList("c:10"));
        registerCompletion(2, (replacements, name) -> {
            if (ParticleMaker.Particle.REDSTONE.fetchName().equalsIgnoreCase(name)) {
                replacements.addAll(fetchColorList());
                return true;
            }
            return false;
        });
        registerCompletion(2, (replacements, name) -> {
            if (name.toLowerCase().contains("item") || name.toLowerCase().contains("icon") || name.toLowerCase().contains("block")) {
                replacements.addAll(fetchMaterialList());
                return true;
            }
            return false;
        });
        registerCompletion(3, Arrays.asList("s:0.0"));
        registerCompletion(3, (replacements, name) -> {
            if (fetchColorList().contains(name.toUpperCase()) || fetchMaterialList().contains(name.toUpperCase())) {
                replacements.add("c:10");
                return true;
            }
            return false;
        });
        registerCompletion(4, Arrays.asList("off:0","off:0,0,0"));
        registerCompletion(4, (replacements, name) -> {
            if (name.startsWith("c:")) {
                replacements.add("s:0.0");
                return true;
            }
            return false;
        });
        registerCompletion(5, (replacements, name) -> {
            if (name.startsWith("s:")) {
                replacements.add("off:0");
                replacements.add("off:0,0,0");
                return true;
            }
            return false;
        });
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
                if (particles.fetchName().equalsIgnoreCase(args[0])) {
                    particle = particles;
                }
            }
        }
        if (particle == null) {
            sender.sendMessage(Core.getLanguage().getString(Language.NO_PARTICLE, true));
            return;
        }
        ParticleMaker maker = new ParticleMaker(particle);
        if (particle.requiresData()) {
            if (args.length == 1) {
                handleArgs(maker, args, 1, (Player) sender);
            }else{
                if (particle == ParticleMaker.Particle.REDSTONE) {
                    ColorWrapper color = ColorWrapper.RED;
                    for (String c : fetchColorList()) {
                        if (c.equalsIgnoreCase(args[1])) {
                            color = ColorWrapper.valueOf(c.toUpperCase());
                            break;
                        }
                    }
                    maker.setDustOptions(new IParticleSender.DustOptions(color.getColor(), 1F));
                }else{
                    Material material = Material.STONE;
                    for (String mat : fetchMaterialList()) {
                        if (mat.equalsIgnoreCase(args[1])) {
                            material = Material.valueOf(mat);
                            break;
                        }
                    }
                    maker.setData(material);
                }
                handleArgs(maker, args, 2, (Player) sender);
            }
        }else{
            handleArgs(maker, args, 1, (Player) sender);
        }
    }

    private void handleArgs (ParticleMaker maker, String[] args, int start, Player player) {
        int args2 = start+1;
        int args3 = start+2;

        //TODO: Particle Amount
        if (args.length == start) {
            maker.sendToLocation(player.getLocation());
            return;
        }
        int amount = 1;
        try {
            amount = Integer.parseInt(clear(args[start]));
        } catch (NumberFormatException e) {
            player.sendMessage(Core.getLanguage().getString(Language.INVALID_NUMBER, true));
            return;
        }
        maker.setCount(amount);


        //TODO: Particle Speed
        if (args.length == args2) {
            maker.sendToLocation(player.getLocation());
            return;
        }
        double speed = 0.0;
        try {
            speed = Double.parseDouble(clear(args[args2]));
        } catch (NumberFormatException e) {
            player.sendMessage(Core.getLanguage().getString(Language.INVALID_NUMBER, true));
            return;
        }
        maker.setSpeed(speed);


        //TODO: Particle Offsets
        if (args.length == args3) {
            maker.sendToLocation(player.getLocation());
            return;
        }

        String off = clear(args[args3]);
        try {
            double offset = Double.parseDouble(off);
            maker.setOffset(offset, offset, offset);
            maker.sendToLocation(player.getLocation());
        } catch (NumberFormatException e) {
            if (!off.contains(",")) {
                player.sendMessage(Core.getLanguage().getString(Language.INVALID_NUMBER, true));
                return;
            }
            String[] offsets = off.split(",");
            if (offsets.length != 3) {
                player.sendMessage(Core.getLanguage().getString(Language.MISSING_OFFSETS, true));
                return;
            }
            double offsetX = Double.parseDouble (offsets[0]);
            double offsetY = Double.parseDouble (offsets[1]);
            double offsetZ = Double.parseDouble (offsets[2]);

            maker.setOffset(offsetX, offsetY, offsetZ);
            maker.sendToLocation(player.getLocation());
        }
    }

    private String clear (String string) {
        return string.replace("c:", "").replace("s:", "").replace("off:", "");
    }

    private List<String> fetchList () {
        List<String> list = new ArrayList<>();
        for (ParticleMaker.Particle particle : ParticleMaker.Particle.values()) {
            if (particle.isCompatable()) list.add(particle.fetchName());
        }
        return list;
    }

    private List<String> fetchMaterialList () {
        List<String> list = new ArrayList<>();
        for (Material material : Material.values()) {
            if (!material.name().startsWith("LEGACY")) list.add(material.name());
        }
        return list;
    }

    private List<String> fetchColorList () {
        List<String> list = new ArrayList<>();
        for (ColorWrapper color : ColorWrapper.values()) {
            list.add(color.name());
        }
        return list;
    }
}
