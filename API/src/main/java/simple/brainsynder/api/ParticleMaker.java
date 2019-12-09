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
import simple.brainsynder.nms.IParticleSender;
import simple.brainsynder.storage.TriLoc;
import simple.brainsynder.utils.ServerVersion;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class ParticleMaker {
    private static IParticleSender particleSender = null;
    private Particle type;
    private double speed = 0.0;
    private int count = 1;
    private double offsetX = 0.0;
    private double offsetY = 0.0;
    private double offsetZ = 0.0;
    private IParticleSender.DustOptions dustOptions = null;
    private ItemStack data = null;
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
    }

    public ParticleMaker(Particle type, int count, double radius) {
        this(type, 0.0, count, radius);
    }

    public ParticleMaker(Particle type) {
        this(type, 0.0, 1, 0.0);
    }

    public ParticleMaker(Particle type, double speed, int count, double radius) {
        this(type, speed, count, radius, radius, radius);
    }

    public ParticleMaker(Particle type, int count, double offsetX, double offsetY, double offsetZ) {
        this(type, 0.0D, count, offsetX, offsetY, offsetZ);
    }

    /**
     * If you are using Redstone Particle then
     * Please use {@link ParticleMaker#setDustOptions(IParticleSender.DustOptions)}
     *
     * @Deprecated
     */
    @Deprecated
    public ParticleMaker(Particle type, int count, Color color) {
        this(type, 1.0F, 0, getColor(color.getRed()), getColor(color.getGreen()), getColor(color.getBlue()));
        colored = true;
        dustOptions = new IParticleSender.DustOptions(color, 1.0F);
        this.repeatAmount = count;
    }

    /**
     * If you are using Redstone Particle then
     * Please use {@link ParticleMaker#setDustOptions(IParticleSender.DustOptions)}
     *
     * @Deprecated
     */
    @Deprecated
    public ParticleMaker(Particle type, Color color) {
        this(type, 1, color);
    }

    public ParticleMaker(Particle type, int count, NoteColor color) {
        this(type, 1.0F, 0, color.getValueX(), color.getValueY(), color.getValueZ());
        colored = true;
        this.repeatAmount = count;
    }

    public ParticleMaker(Particle type, NoteColor color) {
        this(type, 0, color);
    }

    public ParticleMaker(Particle type, double speed, int count, double offsetX, double offsetY, double offsetZ) {
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
        particleSender = simple.brainsynder.utils.Reflection.getParticleSender();
    }

    public ParticleMaker setSpeed(double speed) {
        this.speed = speed;
        return this;
    }

    public ParticleMaker setCount(int count) {
        this.count = count;
        return this;
    }

    public ParticleMaker setDustOptions(IParticleSender.DustOptions dustOptions) {
        this.dustOptions = dustOptions;
        return this;
    }

    public ParticleMaker setOffset(double offsetX, double offsetY, double offsetZ) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        return this;
    }

    public static int getVersion() {
        return version;
    }

    public ParticleMaker setData(Material material, short itemData) {
        data = new ItemStack(material, 1, itemData);
        return this;
    }

    public ParticleMaker setData(Material material) {
        setData(material, (short) 0);
        return this;
    }

    public ParticleMaker setData(ItemStack item) {
        data = item;
        return this;
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
        location.getWorld().getNearbyEntities(location, 100, 100, 100).forEach(entity -> {
            if (entity instanceof Player) {
                sendToPlayer((Player) entity, location);
            }
        });
    }

    public void sendToPlayer(Player player) {
        sendToPlayer(player, player.getLocation());
    }

    public void sendToPlayer(Player player, Location location) {
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

    public void sendToPlayers(List<Player> players, Location location) {
        for (Player player : players) {
            sendToPlayer(player, location);
        }
    }

    private Object createPacket(Location location) {
        Object data = null;
        if ((type == Particle.ITEM_CRACK)
                || (type == Particle.BLOCK_CRACK)
                || (type == Particle.ITEM_TAKE)
                || (type == Particle.BLOCK_DUST)) {
            if (this.data == null) {
                data = new ItemStack(Material.STONE);
            }else data = this.data;
        } else if (type == Particle.REDSTONE) {
            if (dustOptions == null) dustOptions = new IParticleSender.DustOptions(Color.RED, 1);

            if (ServerVersion.isEqualNew(ServerVersion.v1_13_R1)) {
                data = dustOptions;
            }else{
                offsetX = getColor(dustOptions.getColor().getRed());
                offsetY = getColor(dustOptions.getColor().getGreen());
                offsetZ = getColor(dustOptions.getColor().getBlue());
            }
        }


        return particleSender.getPacket(type, new TriLoc<>((float) location.getX(), (float) location.getY(), (float) location.getZ()), new TriLoc<>((float) offsetX, (float) offsetY, (float) offsetZ), (float) speed, count, data);
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
        if (value <= 0.0F) {
            value = -1.0F;
        }

        return ((float) value) / 255.0F;
    }


    private static class MissingParticleException extends Exception {
        public MissingParticleException(String message) {
            super(message);
        }
    }

    private static class Reflection {
        private static HashMap<Class<? extends Entity>, Method> handles = new HashMap<>();

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
        UNKNOWN(false, "", -1, ServerVersion.UNKNOWN),
        BARRIER(false, "barrier", 35, ServerVersion.v1_8_R3),
        BLOCK_CRACK(true, "tilecrack_", 37, ServerVersion.v1_8_R3),
        BLOCK_DUST(true, "blockdust_", 38, ServerVersion.v1_8_R3),
        CLOUD(false, "cloud", 29, ServerVersion.v1_8_R3),
        CRIT(false, "crit", 9, ServerVersion.v1_8_R3),
        CRIT_MAGIC(false, "magicCrit", 10, ServerVersion.v1_8_R3),
        DRIP_LAVA(false, "dripLava", 19, ServerVersion.v1_8_R3),
        DRIP_WATER(false, "dripWater", 18, ServerVersion.v1_8_R3),
        ENCHANTMENT_TABLE(false, "enchantmenttable", 25, ServerVersion.v1_8_R3),
        EXPLOSION_HUGE(false, "hugeexplosion", 2, ServerVersion.v1_8_R3),
        EXPLOSION_LARGE(false, "largeexplode", 1, ServerVersion.v1_8_R3),
        EXPLOSION_NORMAL(false, "explode", 0, ServerVersion.v1_8_R3),
        FIREWORKS_SPARK(false, "fireworksSpark", 3, ServerVersion.v1_8_R3),
        FLAME(false, "flame", 26, ServerVersion.v1_8_R3),
        @Deprecated FOOTSTEP(false, "footstep", 28, ServerVersion.v1_8_R3, ServerVersion.v1_12_R1),  /*  Was removed in 1.13 */
        HEART(false, "heart", 34, ServerVersion.v1_8_R3),
        ITEM_CRACK(true, "iconcrack_", 36, ServerVersion.v1_8_R3),
        @Deprecated ITEM_TAKE(false, "take", 40, ServerVersion.v1_8_R3, ServerVersion.v1_12_R1),  /*  Was removed in 1.13 */
        LAVA(false, "lava", 27, ServerVersion.v1_8_R3),
        MOB_APPEARANCE(false, "mobappearance", 41, ServerVersion.v1_8_R3),
        NOTE(false, "note", 23, ServerVersion.v1_8_R3),
        PORTAL(false, "portal", 24, ServerVersion.v1_8_R3),
        REDSTONE(true, "reddust", 30, ServerVersion.v1_8_R3),
        SLIME(false, "slime", 33, ServerVersion.v1_8_R3),
        SMOKE_LARGE(false, "largesmoke", 12, ServerVersion.v1_8_R3),
        SMOKE_NORMAL(false, "smoke", 11, ServerVersion.v1_8_R3),
        SNOWBALL(false, "snowballpoof", 31, ServerVersion.v1_8_R3),
        SNOW_SHOVEL(false, "snowshovel", 32, ServerVersion.v1_8_R3),
        SPELL(false, "spell", 13, ServerVersion.v1_8_R3),
        SPELL_INSTANT(false, "instantSpell", 14, ServerVersion.v1_8_R3),
        SPELL_MOB(false, "mobSpell", 15, ServerVersion.v1_8_R3),
        SPELL_MOB_AMBIENT(false, "mobSpellAmbient", 16, ServerVersion.v1_8_R3),
        SPELL_WITCH(false, "witchMagic", 17, ServerVersion.v1_8_R3),
        SUSPENDED(false, "suspended", 7, ServerVersion.v1_8_R3),
        SUSPENDED_DEPTH(false, "depthsuspend", 8, ServerVersion.v1_8_R3),
        TOWN_AURA(false, "townaura", 22, ServerVersion.v1_8_R3),
        VILLAGER_ANGRY(false, "angryVillager", 20, ServerVersion.v1_8_R3),
        VILLAGER_HAPPY(false, "happyVillager", 21, ServerVersion.v1_8_R3),
        WATER_BUBBLE(false, "bubble", 4, ServerVersion.v1_8_R3),
        WATER_DROP(false, "droplet", 39, ServerVersion.v1_8_R3),
        WATER_SPLASH(false, "splash", 5, ServerVersion.v1_8_R3),
        WATER_WAKE(false, "wake", 6, ServerVersion.v1_8_R3),


        /* Added in 1.9 */
        DAMAGE_INDICATOR(false, "damageIndicator", -1, ServerVersion.v1_9_R1),
        DRAGON_BREATH(false, "dragonbreath", -1, ServerVersion.v1_9_R1),
        END_ROD(false, "endRod", -1, ServerVersion.v1_9_R1),
        SWEEP_ATTACK(false, "sweepAttack", -1, ServerVersion.v1_9_R1),


        /* Added in 1.11 */
        FALLING_DUST(true, "fallingdust", -1, ServerVersion.v1_11_R1),
        SPIT(false, "spit", -1, ServerVersion.v1_11_R1),
        TOTEM(false, "totem", -1, ServerVersion.v1_11_R1),


        /* Added in 1.13 */
        BUBBLE_COLUMN_UP(ServerVersion.v1_13_R1),
        BUBBLE_POP(ServerVersion.v1_13_R1),
        CURRENT_DOWN(ServerVersion.v1_13_R1),
        DOLPHIN(ServerVersion.v1_13_R1),
        @Deprecated LEGACY_BLOCK_CRACK(true, "legacy_block_crack", -1, ServerVersion.v1_13_R1),
        @Deprecated LEGACY_BLOCK_DUST(true, "legacy_block_dust", -1, ServerVersion.v1_13_R1),
        @Deprecated LEGACY_FALLING_DUST(true, "legacy_falling_dust", -1, ServerVersion.v1_13_R1),
        NAUTILUS(ServerVersion.v1_13_R1),
        SQUID_INK(ServerVersion.v1_13_R1),


        /* Added in 1.14 */
        CAMPFIRE_COSY_SMOKE(ServerVersion.v1_14_R1),
        CAMPFIRE_SIGNAL_SMOKE(ServerVersion.v1_14_R1),
        COMPOSTER(ServerVersion.v1_14_R1),
        FALLING_LAVA(ServerVersion.v1_14_R1),
        FALLING_WATER(ServerVersion.v1_14_R1),
        FLASH(ServerVersion.v1_14_R1),
        LANDING_LAVA(ServerVersion.v1_14_R1),
        SNEEZE(ServerVersion.v1_14_R1),


        /* Added in 1.15 */
        DRIPPING_HONEY(ServerVersion.v1_15_R1),
        FALLING_HONEY(ServerVersion.v1_15_R1),
        LANDING_HONEY(ServerVersion.v1_15_R1),
        FALLING_NECTAR(ServerVersion.v1_15_R1);

        private String name;
        private int id;
        private boolean requiresData;
        private ServerVersion version;
        private ServerVersion maxVersion;

        Particle(ServerVersion version) {
            this.name = name().toLowerCase();
            this.id = -1;
            this.requiresData=false;
            this.version = version;
            this.maxVersion = ServerVersion.UNKNOWN;
        }

        Particle(ServerVersion version, ServerVersion maxVersion) {
            this.name = name().toLowerCase();
            this.id = -1;
            this.requiresData=false;
            this.version = version;
            this.maxVersion = maxVersion;
        }

        Particle(boolean requiresData, String name, int id, ServerVersion version) {
            this.name = ((version == ServerVersion.v1_13_R1) ? name().toLowerCase() : name);
            this.id = id;
            this.requiresData=requiresData;
            this.version = version;
            this.maxVersion = ServerVersion.UNKNOWN;
        }

        Particle(boolean requiresData, String name, int id, ServerVersion version, ServerVersion maxVersion) {
            this.name = ((version == ServerVersion.v1_13_R1) ? name().toLowerCase() : name);
            this.id = id;
            this.requiresData=requiresData;
            this.version = version;
            this.maxVersion = maxVersion;
        }

        @Deprecated
        public static Particle getById(int id) {
            if (id == -1) return UNKNOWN;
            for (Particle particle : values()) {
                if (particle.getId() == id)
                    return particle;
            }
            return UNKNOWN;
        }

        public static Particle getByName(String name) {
            if (name.isEmpty()) return UNKNOWN;
            for (Particle particle : values()) {
                if (particle.getName().equalsIgnoreCase(name))
                    return particle;
                if (particle.name().equalsIgnoreCase(name))
                    return particle;
            }
            return UNKNOWN;
        }

        public String getAllowedVersion() {
            String versionName = version.name();
            if (maxVersion != ServerVersion.UNKNOWN)
                versionName += "-"+maxVersion.name();
            return versionName;
        }

        public boolean isCompatable() {
            if (maxVersion == ServerVersion.UNKNOWN) {
                return ServerVersion.isEqualNew(version);
            }
            if (getVersion() <= ParticleMaker.version) {
                // Particle was removed in a version and needs to be limited to what version can use it.
                return ServerVersion.isEqualOld(maxVersion);
            }
            return true;
        }

        public boolean requiresData() {
            return requiresData;
        }

        public int getVersion() {
            return version.getIntVersion();
        }

        public String getName() {
            return this.name;
        }

        public String fetchName () {
            return name().toLowerCase();
        }

        @Deprecated
        public int getId() {
            return this.id;
        }
    }
}
