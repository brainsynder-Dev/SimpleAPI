/*
 * Copyright (c) created class file on: 2015.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */

package simple.brainsynder.api;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import simple.brainsynder.Core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class ParticleMaker {
    private static Constructor<?> packetConstructor = null;
    private static boolean newParticlePacketConstructor = false;
    private static Class<?> enumParticle = null;
    private Particle type;
    private double speed;
    private int count;
    private double offsetX;
    private double offsetY;
    private double offsetZ;
    private int[] data = new int[0];
    private static boolean compatible = true;
    private static int version = 17;
    private boolean colored = false;
    private int repeatAmount = 1;

    static {
        String vString = Reflection.getVersion().replace("v", "");
        int v = 17;
        if (!vString.isEmpty()) {
            String[] array = vString.split("_");
            v = Integer.parseInt(array[0] + array[1]);
            version = v;
        }
        try {
            Class<?> packetClass = Reflection.getNmsClass("PacketPlayOutWorldParticles");
            if (v < 18) {
                packetConstructor = packetClass.getConstructor(
                        String.class,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Integer.TYPE);
            } else {
                newParticlePacketConstructor = true;
                enumParticle = Reflection.getNmsClass("EnumParticle");
                packetConstructor = packetClass.getDeclaredConstructor(
                        enumParticle,
                        Boolean.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Float.TYPE,
                        Integer.TYPE,
                        int[].class);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            compatible = false;
        }
    }

    public ParticleMaker(Particle type, int count, double radius) {
        this(type, 0.0, count, radius);
    }

    public ParticleMaker(Particle type, double speed, int count, double radius) {
        this(type, speed, count, radius, radius, radius);
    }

    public ParticleMaker(Particle type, int count, double offsetX, double offsetY, double offsetZ)  {
        this(type, 0.0D, count, offsetX, offsetY, offsetZ);
    }

    public ParticleMaker(Particle type, int count, Color color) {
        this(type, 1.0F, 0, getColor(color.getRed()), getColor(color.getGreen()), getColor(color.getBlue()));
        this.data = new int[0];
        colored = true;
        this.repeatAmount = count;
    }

    public ParticleMaker(Particle type, Color color) {
        this(type, 1, color);
    }

    public ParticleMaker(Particle type, int count, NoteColor color) {
        this(type, 1.0F, 0, color.getValueX(), color.getValueY(), color.getValueZ());
        this.data = new int[0];
        colored = true;
        this.repeatAmount = count;
    }

    public ParticleMaker(Particle type, NoteColor color)  {
        this(type, 0, color);
    }

    public ParticleMaker(Particle type, double speed, int count, double offsetX, double offsetY, double offsetZ)  {
        if (version < 1.7) {
            try {
                throw new MissingParticleException("This server version is not supported for this particle class.");
            } catch (MissingParticleException e) {
                e.printStackTrace();
            }
        }
        this.type = type;
        if (!type.isCompatable()) {
            try {
                throw new MissingParticleException("The particle '" + type.getName() + "' is not supported in this version.");
            } catch (MissingParticleException e) {
                e.printStackTrace();
            }
        }
        this.speed = speed;
        this.count = count;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    public static int getVersion() {
        return version;
    }

    public void setData(Material material, short itemData) {
        data = new int[]{material.getId(), itemData};
    }

    public void setData(Material material) {
        setData(material, (short) 0);
    }

    public void setData(ItemStack item) {
        setData(item.getType(), item.getDurability());
    }

    public double getSpeed() {
        return this.speed;
    }

    public int getCount() {
        return this.count;
    }

    public double getOffsetX() {
        return this.offsetX;
    }

    public double getOffsetY() {
        return this.offsetY;
    }

    public double getOffsetZ() {
        return this.offsetZ;
    }

    public void sendToLocation(Location location) {
        if (!Core.getInstance().getLagCheck().isLagging()) {
            try {
                Object packet = createPacket(location);
                if (colored) {
                    for (int i = 0; i < repeatAmount; i++) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            Reflection.sendPacket(player, packet);
                        }
                    }
                    return;
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Reflection.sendPacket(player, packet);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendToPlayer(Player player) {
        if (!Core.getInstance().getLagCheck().isLagging()) {
            try {
                Object packet = createPacket(player.getLocation());
                if (colored) {
                    for (int i = 0; i < repeatAmount; i++) {
                        Reflection.sendPacket(player, packet);
                    }
                    return;
                }
                Reflection.sendPacket(player, packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendToPlayer(Player player, Location location) {
        if (!Core.getInstance().getLagCheck().isLagging()) {
            try {
                Object packet = createPacket(location);
                if (colored) {
                    for (int i = 0; i < repeatAmount; i++) {
                        Reflection.sendPacket(player, packet);
                    }
                    return;
                }
                Reflection.sendPacket(player, packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendToPlayers (List<Player> players, Location location) {
        if (!Core.getInstance().getLagCheck().isLagging()) {
            for (Player player : players) {
                sendToPlayer(player, location);
            }
        }
    }

    private Object createPacket(Location location) {
        if ((type == Particle.ITEM_CRACK) || (type == Particle.BLOCK_CRACK) || (type == Particle.ITEM_TAKE) || (type == Particle.BLOCK_DUST)) {
            if (data == new int[0]) {
                data = new int[] {57,0};
            }
        }
        try {
            Object packet;
            if (newParticlePacketConstructor) {
                Object particleType = enumParticle.getEnumConstants()[this.type.getId()];
                packet = packetConstructor.newInstance(
                        particleType,
                        true,
                        (float) location.getX(),
                        (float) location.getY(),
                        (float) location.getZ(),
                        (float) this.offsetX,
                        (float) this.offsetY,
                        (float) this.offsetZ,
                        (float) this.speed,
                        this.count,
                        data);
            } else {
                packet = packetConstructor.newInstance(
                        this.type.getName(),
                        (float) location.getX(),
                        (float) location.getY(),
                        (float) location.getZ(),
                        (float) this.offsetX,
                        (float) this.offsetY,
                        (float) this.offsetZ,
                        (float) this.speed,
                        this.count);
            }
            return packet;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean isCompatible() {
        return compatible;
    }

    public static final class NoteColor {
        private final int note;

        public NoteColor(int note) throws IllegalArgumentException {
            if (note < 0) {

                throw new IllegalArgumentException("The note value is lower than 0");
            }
            if (note > 24) {
                throw new IllegalArgumentException("The note value is higher than 24");
            }
            this.note = note;
        }

        public float getValueX() {
            return this.note / 24.0F;
        }

        public float getValueY() {
            return 0.0F;
        }

        public float getValueZ() {
            return 0.0F;
        }
    }

    private static float getColor(double value) {
        if(value <= 0.0F) {
            value = -1.0F;
        }

        return ((float)value) / 255.0F;
    }


    private static class MissingParticleException extends Exception{
        public MissingParticleException (String message) {
            super(message);
        }
    }

    private static class Reflection {
        private static HashMap<Class<? extends Entity>, Method> handles = new HashMap<> ();

        public static Class<?> getNmsClass(String name) {
            Class<?> clazz = null;
            try {
                clazz = Class.forName("net.minecraft.server." + getVersion() + "." + name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return clazz;
        }

        public static String getVersion() {
            return Bukkit.getServer().getClass().getPackage().getName().substring(23);
        }

        public static Object getHandle(Entity entity) {
            try {
                if (handles.get(entity.getClass()) != null) {
                    return handles.get(entity.getClass()).invoke(entity);
                }
                Method entity_getHandle = entity.getClass().getMethod("getHandle");
                handles.put(entity.getClass(), entity_getHandle);
                return entity_getHandle.invoke(entity);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        public static void sendPacket(Player p, Object packet)
            throws IllegalArgumentException {
            Field player_connection;
            Method player_sendPacket = null;
            try {
                player_connection = Reflection.getHandle(p).getClass().getField("playerConnection");
                Method[] arrayOfMethod;
                int j = (arrayOfMethod = player_connection.get(Reflection.getHandle(p)).getClass().getMethods()).length;
                for (int i = 0; i < j; i++) {
                    Method m = arrayOfMethod[i];
                    if (m.getName().equalsIgnoreCase("sendPacket")) {
                        player_sendPacket = m;
                    }
                }
                if (player_sendPacket != null) {
                    player_sendPacket.invoke(player_connection.get(Reflection.getHandle(p)), packet);
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException ex) {
                ex.printStackTrace();
            }
        }
    }

    public enum Particle {
        EXPLOSION_NORMAL("explode", 0, "1.7"),
        EXPLOSION_LARGE("largeexplode", 1, "1.7"),
        EXPLOSION_HUGE("hugeexplosion", 2, "1.7"),
        FIREWORKS_SPARK("fireworksSpark", 3, "1.7"),
        WATER_BUBBLE("bubble", 4, "1.7"),
        WATER_SPLASH("splash", 5, "1.7"),
        WATER_WAKE("wake", 6, "1.7"),
        SUSPENDED("suspended", 7, "1.7"),
        SUSPENDED_DEPTH("depthsuspend", 8, "1.7"),
        CRIT("crit", 9, "1.7"),
        CRIT_MAGIC("magicCrit", 10, "1.7"),
        SMOKE_NORMAL("smoke", 11, "1.7"),
        SMOKE_LARGE("largesmoke", 12, "1.7"),
        SPELL("spell", 13, "1.7"),
        SPELL_INSTANT("instantSpell", 14, "1.7"),
        SPELL_MOB("mobSpell", 15, "1.7"),
        SPELL_MOB_AMBIENT("mobSpellAmbient", 16, "1.7"),
        SPELL_WITCH("witchMagic", 17, "1.7"),
        DRIP_WATER("dripWater", 18, "1.7"),
        DRIP_LAVA("dripLava", 19, "1.7"),
        VILLAGER_ANGRY("angryVillager", 20, "1.7"),
        VILLAGER_HAPPY("happyVillager", 21, "1.7"),
        TOWN_AURA("townaura", 22, "1.7"),
        NOTE("note", 23, "1.7"),
        PORTAL("portal", 24, "1.7"),
        ENCHANTMENT_TABLE("enchantmenttable", 25, "1.7"),
        FLAME("flame", 26, "1.7"),
        LAVA("lava", 27, "1.7"),
        FOOTSTEP("footstep", 28, "1.7"),
        CLOUD("cloud", 29, "1.7"),
        REDSTONE("reddust", 30, "1.7"),
        SNOWBALL("snowballpoof", 31, "1.7"),
        SNOW_SHOVEL("snowshovel", 32, "1.7"),
        SLIME("slime", 33, "1.7"),
        HEART("heart", 34, "1.7"),
        BARRIER("barrier", 35, "1.7"),
        ITEM_CRACK("iconcrack_", 36, "1.7"),
        BLOCK_CRACK("tilecrack_", 37, "1.7"),
        BLOCK_DUST("blockdust_", 38, "1.7"),
        WATER_DROP("droplet", 39, "1.8"),
        ITEM_TAKE("take", 40, "1.8"),
        MOB_APPEARANCE("mobappearance", 41, "1.8"),
        DRAGON_BREATH("dragonbreath", 42, "1.9"),
        END_ROD("endRod", 43, "1.9"),
        DAMAGE_INDICATOR("damageIndicator", 44, "1.9"),
        SWEEP_ATTACK("sweepAttack", 45, "1.9"),
        FALLING_DUST("fallingdust", 46, "1.10"),
        TOTEM("totem", 47, "1.11"),
        SPIT("spit", 48, "1.11");

        private String name;
        private int id;
        private int version;
        private String allowed;

        Particle(String name, int id, String version) {
            this.name = name;
            this.id = id;
            allowed = version;
            this.version = Integer.parseInt((allowed.replace(".", "")));
        }

        public static Particle getById (int id) {
            for (Particle particle : values()) {
                if (particle.getId() == id)
                    return particle;
            }
            return null;
        }

        public static Particle getByName (String name) {
            for (Particle particle : values()) {
                if (particle.getName().equalsIgnoreCase(name))
                    return particle;
            }
            return null;
        }

        public static Particle getByEnum (String name) {
            for (Particle particle : values()) {
                if (particle.name().equals(name))
                    return particle;
            }
            return null;
        }

        public String getAllowedVersion () {
            return allowed;
        }

        public boolean isCompatable () {
            return (getVersion() <= ParticleMaker.version);
        }

        public int getVersion() {
            return version;
        }

        public String getName() {
            return this.name;
        }

        public int getId() {
            return this.id;
        }
    }
}
