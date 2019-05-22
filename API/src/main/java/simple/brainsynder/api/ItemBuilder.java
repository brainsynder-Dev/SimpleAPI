package simple.brainsynder.api;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simple.brainsynder.nbt.StorageTagCompound;
import simple.brainsynder.nbt.StorageTagList;
import simple.brainsynder.nbt.StorageTagString;
import simple.brainsynder.nms.DataConverter;
import simple.brainsynder.nms.materials.*;
import simple.brainsynder.nms.materials.types.*;
import simple.brainsynder.reflection.FieldAccessor;
import simple.brainsynder.utils.*;

import java.util.*;

@SuppressWarnings("ALL")
public class ItemBuilder {
    private static DataConverter converter = null;
    private JSONObject JSON;
    private ItemStack is;
    private ItemMeta im;

    public static DataConverter getConverter() {
        if (converter == null) converter = Reflection.getConverter();
        return converter;
    }

    public ItemBuilder(Material material) {
        this (material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        JSON = new JSONObject(new LinkedHashMap());
        JSON.put("material", material.name());
        if (amount != 1) JSON.put("amount", amount);
        this.is = new ItemStack(material, amount);
        this.im = is.getItemMeta();
    }

    public static ItemBuilder getSkull (SkullType type) {
        return getSkull(type, 1);
    }
    public static ItemBuilder getSkull (SkullType type, int amount) {
        return getConverter().getSkullMaterial(type).toBuilder(amount);
    }

    public static ItemBuilder getColored (MatType type) {
        return getColored(type, 0);
    }
    public static ItemBuilder getColored (MatType type, int data) {
        return getColored(type, data, 1);
    }
    public static ItemBuilder getColored (MatType type, int data, int amount) {
        return getConverter().getColoredMaterial(type, data).toBuilder(amount);
    }

    public static ItemBuilder getMaterial (WrappedType type) {
        return getConverter().getMaterial(type).toBuilder(1);
    }
    public static ItemBuilder getSandStone (SandType sandType, SandStoneType stoneType) {
        return getConverter().getSandStone(sandType, stoneType).toBuilder(1);
    }
    public static ItemBuilder getSlabMaterial (StoneSlabType type, boolean single) {
        return getConverter().getSlabMaterial(type, single).toBuilder(1);
    }
    public static ItemBuilder getFishMaterial (FishType type, boolean cooked) {
        return getConverter().getFishMaterial(type, cooked).toBuilder(1);
    }
    public static ItemBuilder getWoodMaterial(WoodSelector selector, WoodType type) {
        return getConverter().getWoodMaterial(selector, type).toBuilder(1);
    }

    public static ItemBuilder fromItem (ItemStack stack) {
        ItemBuilder builder = new ItemBuilder(stack.getType(), stack.getAmount());
        if (stack.getData() != null) builder.withData(stack.getData().getData());
        if (stack.hasItemMeta()) {
            ItemMeta meta = stack.getItemMeta();
            if (meta.hasDisplayName()) builder.withName(meta.getDisplayName());
            if (meta.hasEnchants()) {
                meta.getEnchants().forEach((enchantment, integer) -> {
                    builder.withEnchant(enchantment, integer);
                });
            }
            if (meta.hasLore()) builder.withLore(meta.getLore());
            if (!meta.getItemFlags().isEmpty()) {
                meta.getItemFlags().forEach(itemFlag -> {
                    builder.withFlag(itemFlag);
                });
            }

            if (meta instanceof SkullMeta) {
                SkullMeta skull = (SkullMeta) meta;
                if (skull.hasOwner()) builder.setOwner(skull.getOwner());
                String texture = builder.getTexture(builder.getGameProfile(skull));
                if (!texture.isEmpty()) builder.setTexture(texture);
            }

            DataConverter converter = Reflection.getConverter();
            if (converter.isSpawnEgg(stack)) builder.withEntity(converter.getTypeFromItem(stack));
        }
        return builder;
    }

    private static DataConverter.Data translate (String material, int data) {
        DataConverter converter = getConverter();
        if (ServerVersion.isEqualNew(ServerVersion.v1_13_R1)) {
            for (MatType type : MatType.values()) {
                if (type.name().equalsIgnoreCase(material)) {
                    return converter.getColoredMaterial(type, data);
                }
            }
            String rawMat = material.toUpperCase();

            if (rawMat.contains("BARDING"))
                return new DataConverter.Data(converter.findMaterial(rawMat.replace("BARDING", "HORSE_ARMOR")), 0);

            switch (rawMat) {
                case "SKULL_ITEM": return converter.getSkullMaterial(SkullType.values()[data]);
                case "COAL": return converter.getMaterial(CoalType.values()[data]);
                case "COBBLE_WALL": return converter.getMaterial(CobbleWallType.values()[data]);
                case "DIRT": return converter.getMaterial(DirtType.values()[data]);
                case "YELLOW_FLOWER": return converter.getMaterial(FlowerType.DANDELION);
                case "RED_ROSE": return converter.getMaterial(FlowerType.values()[data+1]);
                case "GOLDEN_APPLE": return converter.getMaterial(GoldAppleType.values()[data]);
                case "LONG_GRASS": return converter.getMaterial(GrassType.values()[data]);
                case "PRISMARINE": return converter.getMaterial(PrismarineType.values()[data]);
                case "QUARTZ_BLOCK": return converter.getMaterial(QuartzType.values()[data]);
                case "SAND": return converter.getMaterial(SandType.values()[data]);
                case "SPONGE": return converter.getMaterial(SpongeType.values()[data]);
                case "SMOOTH_BRICK": return converter.getMaterial(StoneBrickType.values()[data]);
                case "STONE": return converter.getMaterial(StoneType.values()[data]);
                case "DOUBLE_PLANT": return converter.getMaterial(TallPlantType.values()[data]);
                case "COMMAND": return new DataConverter.Data(converter.findMaterial("COMMAND_BLOCK"), 0);
                case "EYE_OF_ENDER": return new DataConverter.Data(converter.findMaterial("ENDER_EYE"), 0);
                case "PORK": return new DataConverter.Data(converter.findMaterial("PORKCHOP"), 0);
                case "GRILLED_PORK": return new DataConverter.Data(converter.findMaterial("COOKED_PORKCHOP"), 0);
                case "SULPHUR": return new DataConverter.Data(converter.findMaterial("GUNPOWDER"), 0);
            }
        }

        return null;
    }

    public static ItemBuilder fromJSON (JSONObject json) {
        if (!json.containsKey("material")) throw new NullPointerException("JSONObject seems to be missing speed material");

        int amount = 1;
        if (json.containsKey("amount")) amount = Integer.parseInt(String.valueOf(json.get("amount")));
        String materialName = String.valueOf(json.get("material"));
        int rawData = Integer.parseInt(String.valueOf(json.getOrDefault("data", "0")));
        Material material = null;
        try {
            material = Material.valueOf(materialName);
        }catch (Exception e) {
            DataConverter.Data data = translate(materialName, rawData);
            if (data == null)
                throw new NullPointerException("Could not find '"+materialName+"' for version "+ServerVersion.getVersion().name());
            material = data.getMaterial();
            rawData = data.getData();
        }
        ItemBuilder builder = new ItemBuilder(material, amount);

        if (json.containsKey("name")) builder.withName(String.valueOf(json.get("name")));
        if (json.containsKey("lore")) {
            List<String> lore = new ArrayList<>();
            lore.addAll(((JSONArray)json.get("lore")));
            builder.withLore(lore);
        }
        if (json.containsKey("data")) builder.withData(rawData);

        if (json.containsKey("enchants")) {
            JSONArray array = (JSONArray) json.get("enchants");
            for (Object o : array) {
                try {
                    String[] args = String.valueOf(o).split(" ~~ ");
                    Enchantment enchant = Enchantment.getByName(args[0]);
                    int level = Integer.parseInt(args[1]);
                    builder.withEnchant(enchant, level);
                }catch (Exception ignored) {}
            }
        }
        if (json.containsKey("flags")) {
            JSONArray array = (JSONArray) json.get("flags");
            for (Object o : array) {
                ItemFlag flag = ItemFlag.valueOf(String.valueOf(o));
                builder.withFlag(flag);
            }
        }
        if (json.containsKey("entity"))
            builder.withEntity(EntityType.valueOf(String.valueOf(json.get("entity"))));

        if (json.containsKey("skullData")) {
            JSONObject skull = (JSONObject) json.get("skullData");

            if (skull.containsKey("texture")) builder.setTexture(String.valueOf(skull.get("texture")));
            if (skull.containsKey("owner")) builder.setOwner(String.valueOf(skull.get("owner")));
        }

        return builder;
    }

    public StorageTagCompound toCompound () {
        StorageTagCompound compound = new StorageTagCompound ();
        compound.setString("material", is.getType().name());
        if (is.getAmount() > 1) compound.setInteger("amount", is.getAmount());
        if (im.hasDisplayName()) compound.setString("name", im.getDisplayName());
        if (is.getDurability() > 0) compound.setInteger("data", is.getDurability());

        if (im.hasLore()) {
            StorageTagList lore = new StorageTagList();
            im.getLore().forEach(line -> lore.appendTag(new StorageTagString(line)));
            compound.setTag("lore", lore);
        }

        if (!is.getEnchantments().isEmpty()) {
            StorageTagList enchants = new StorageTagList();
            is.getEnchantments().forEach((enchantment, level) -> enchants.appendTag(new StorageTagString(enchantment.getName()+" ~~ "+level)));
            compound.setTag("enchants", enchants);
        }

        if (!im.getItemFlags().isEmpty()) {
            StorageTagList flags = new StorageTagList();
            im.getItemFlags().forEach(itemFlag -> flags.appendTag(new StorageTagString(itemFlag.name())));
            compound.setTag("flags", flags);
        }

        DataConverter converter = Reflection.getConverter();
        if (converter.isSpawnEgg(is)) compound.setString("entity", converter.getTypeFromItem(is).name());

        if (im instanceof SkullMeta) {
            StorageTagCompound skull = new StorageTagCompound();
            SkullMeta meta = (SkullMeta) im;
            if (meta.hasOwner()) skull.setString("owner", meta.getOwner());

            String texture = getTexture(getGameProfile(meta));
            if (!texture.isEmpty()) skull.setString("texture", texture);

            compound.setTag("skullData", skull);
        }
        return compound;
    }

    public static ItemBuilder fromCompound (StorageTagCompound compound) {
        if (!compound.hasKey("material")) throw new NullPointerException("JSONObject seems to be missing speed material");

        int amount = 1;
        if (compound.hasKey("amount")) amount = compound.getInteger("amount");
        Material material = Material.valueOf(compound.getString("material"));
        ItemBuilder builder = new ItemBuilder(material, amount);

        if (compound.hasKey("name")) builder.withName(compound.getString("name"));
        if (compound.hasKey("lore")) {
            List<String> lore = new ArrayList<>();
            StorageTagList list = (StorageTagList) compound.getTag("lore");
            for (int i = 0; i < list.tagCount(); i++) lore.add(list.getStringTagAt(i));
            builder.withLore(lore);
        }
        if (compound.hasKey("data")) builder.withData(compound.getInteger("data"));

        if (compound.hasKey("enchants")) {
            StorageTagList list = (StorageTagList) compound.getTag("enchants");
            for (int i = 0; i < list.tagCount(); i++) {
                try {
                    String[] args = list.getStringTagAt(i).split(" ~~ ");
                    Enchantment enchant = Enchantment.getByName(args[0]);
                    int level = Integer.parseInt(args[1]);
                    builder.withEnchant(enchant, level);
                }catch (Exception ignored) {}
            }
        }
        if (compound.hasKey("flags")) {
            StorageTagList list = (StorageTagList) compound.getTag("flags");
            for (int i = 0; i < list.tagCount(); i++) {
                ItemFlag flag = ItemFlag.valueOf(list.getStringTagAt(i));
                builder.withFlag(flag);
            }
        }
        if (compound.hasKey("entity"))
            builder.withEntity(EntityType.valueOf(compound.getString("entity")));

        if (compound.hasKey("skullData")) {
            StorageTagCompound skull = compound.getCompoundTag("skullData");

            if (skull.hasKey("texture")) builder.setTexture(skull.getString("texture"));
            if (skull.hasKey("owner")) builder.setOwner(skull.getString("owner"));
        }

        return builder;
    }

    public ItemBuilder clone () {
        return fromJSON(toJSON());
    }

    public ItemBuilder withName(String name) {
        JSON.put("name", name);
        im.setDisplayName(translate(name));
        return this;
    }

    public String getName () {
        if (im.hasDisplayName()) return im.getDisplayName();
        return WordUtils.capitalizeFully(is.getType().name().toLowerCase().replace("_", " "));
    }

    public ItemBuilder withEntity(EntityType type) {
        JSON.put("entity", type.name());
        is.setItemMeta(im);

        is = Reflection.getConverter().getSpawnEgg(type, is);
        im = is.getItemMeta();
        return this;
    }

    public ItemBuilder withLore(List<String> lore) {
        JSONArray LORE = new JSONArray();
        LORE.addAll(lore);
        JSON.put("lore", LORE);

        im.setLore(translate(lore));
        return this;
    }
    public ItemBuilder addLore(String... lore) {
        JSONArray LORE = new JSONArray();
        if (JSON.containsKey("lore")) LORE = (JSONArray) JSON.get("lore");
        List<String> itemLore = new ArrayList<>();
        if (im.hasLore()) itemLore = im.getLore();

        LORE.addAll(Arrays.asList(lore));
        JSON.put("lore", LORE);
        List<String> finalItemLore = itemLore;
        Arrays.asList(lore).forEach(s -> finalItemLore.add(translate(s)));
        im.setLore(finalItemLore);
        return this;
    }
    public ItemBuilder clearLore() {
        if (JSON.containsKey("lore")) JSON.remove("lore");
        im.getLore().clear();
        return this;
    }

    public void setRawMeta(ItemMeta meta) {
        this.im = meta;
    }

    public ItemMeta getRawMeta() {
        return im;
    }

    public ItemBuilder removeLore(String lore) {
        List<String> itemLore = new ArrayList<>();
        if (im.hasLore()) itemLore = im.getLore();
        if (JSON.containsKey("lore")) {
            JSONArray LORE = (JSONArray) JSON.get("lore");
            LORE.stream().filter(o -> String.valueOf(o).startsWith(lore)).forEach(o -> LORE.remove(o));
            if (LORE.isEmpty()) {
                JSON.remove("lore");
            }else{
                JSON.put("lore", LORE);
            }
        }
        itemLore.remove(translate(lore));
        im.setLore(itemLore);
        return this;
    }

    @Deprecated
    public ItemBuilder withData(int data) {
        JSON.put("data", data);
        is.setDurability((short) data);
        return this;
    }
    public ItemBuilder withData (MaterialData data) {
        // Checks from the Bukkit API - Start
        Material mat = is.getType();
        if (data != null && mat != null && mat.getData() != null) {
            if (data.getClass() != mat.getData() && data.getClass() != MaterialData.class) {
                throw new IllegalArgumentException("Provided data is not of type " + mat.getData().getName() + ", found " + data.getClass().getName());
            }

            is.setData(data);
        } else {
            is.setData(data);
        }
        // Checks - end

        JSON.put("material", data.getItemType());
        JSON.put("data", data.getData()); // MaterialData#getData is depricated... but it is still in the 1.13 Bukkit API Preview

        return this;
    }

    public ItemBuilder withEnchant(Enchantment enchant, int level) {
        JSONArray ENCHANTS = new JSONArray();
        if (JSON.containsKey("enchants")) ENCHANTS = (JSONArray) JSON.get("enchants");
        ENCHANTS.add(enchant.getName()+" ~~ "+level);
        JSON.put("enchants", ENCHANTS);
        is.addUnsafeEnchantment(enchant, level);
        return this;
    }
    public ItemBuilder removeEnchant(Enchantment enchant) {
        if (JSON.containsKey("enchants")) {
            JSONArray ENCHANTS = (JSONArray) JSON.get("enchants");
            ENCHANTS.stream().filter(o -> String.valueOf(o).startsWith(enchant.getName())).forEach(o -> ENCHANTS.remove(o));
            if (ENCHANTS.isEmpty()) {
                JSON.remove("enchants");
            }else{
                JSON.put("enchants", ENCHANTS);
            }
        }

        is.removeEnchantment(enchant);
        return this;
    }

    public ItemBuilder withFlag(ItemFlag flag) {
        JSONArray FLAGS = new JSONArray();
        if (JSON.containsKey("flags")) FLAGS = (JSONArray) JSON.get("flags");
        FLAGS.add(flag.name());
        JSON.put("flags", FLAGS);
        im.addItemFlags(flag);
        return this;
    }
    public ItemBuilder removeFlag(ItemFlag flag) {
        if (JSON.containsKey("flags")) {
            JSONArray FLAGS = (JSONArray) JSON.get("flags");
            FLAGS.stream().filter(o -> String.valueOf(o).equals(flag.name())).forEach(o -> FLAGS.remove(o));

            if (FLAGS.isEmpty()) {
                JSON.remove("flags");
            }else{
                JSON.put("flags", FLAGS);
            }
        }

        im.removeItemFlags(flag);
        return this;
    }

    public JSONObject toJSON () {
        return JSON;
    }
    public ItemStack build() {
        is.setItemMeta(im);
        return is;
    }

    public ItemBuilder setOwner (String owner) {
        JSONObject SKULL = (JSONObject) JSON.getOrDefault("skullData", new JSONObject());
        SKULL.put("owner", owner);
        JSON.put("skullData", SKULL);

        if (is.getType().name().equals("PLAYER_HEAD") || (is.getType().name().contains("SKULL_ITEM") && (is.getDurability() == 3))) {
            SkullMeta meta = (SkullMeta) im;
            meta.setOwner(owner);
            im = meta;
        }
        return this;
    }

    public ItemBuilder setTexture (String textureURL) {
        if (textureURL.startsWith("http")) textureURL = Base64Wrapper.encodeString("{\"textures\":{\"SKIN\":{\"url\":\"" + textureURL + "\"}}}");

        JSONObject SKULL = (JSONObject) JSON.getOrDefault("skullData", new JSONObject());
        SKULL.put("texture", textureURL);
        JSON.put("skullData", SKULL);

        if (is.getType().name().equals("PLAYER_HEAD") || (is.getType().name().contains("SKULL_ITEM") && (is.getDurability() == 3))) {
            SkullMeta meta = (SkullMeta) im;

            if (textureURL.length() > 17) {
                im = applyTextureToMeta(meta, createProfile(textureURL));
            }else{
                meta.setOwner(textureURL);
                im = meta;
            }
        }
        return this;
    }

    public boolean isSimilar(ItemStack check) {
        List<Boolean> values = new ArrayList<>();
        if (check == null) return false;
        ItemStack main = build();
        if (main.getType() == check.getType()) {
            if (check.hasItemMeta() && main.hasItemMeta()) {
                ItemMeta mainMeta = main.getItemMeta();
                ItemMeta checkMeta = check.getItemMeta();
                if (mainMeta.hasDisplayName() && checkMeta.hasDisplayName()) {
                    values.add(mainMeta.getDisplayName().equals(checkMeta.getDisplayName()));
                }

                if (mainMeta.hasLore() && checkMeta.hasLore()) {
                    values.add(mainMeta.getLore().equals(checkMeta.getLore()));
                }

                if (mainMeta.hasEnchants() && checkMeta.hasEnchants()) {
                    values.add(mainMeta.getEnchants().equals(checkMeta.getEnchants()));
                }

                if ((mainMeta instanceof SkullMeta) && (checkMeta instanceof SkullMeta)) {
                    SkullMeta mainSkullMeta = (SkullMeta) mainMeta;
                    SkullMeta checkSkullMeta = (SkullMeta) checkMeta;

                    try { // This is just to ignore any NPE errors that might happen if using regular skulls
                        if (mainSkullMeta.hasOwner() && checkSkullMeta.hasOwner()) {
                            values.add(mainSkullMeta.getOwner().equals(checkSkullMeta.getOwner()));
                        }
                        values.add(getTexture(getGameProfile(mainSkullMeta)).equals(getTexture(getGameProfile(checkSkullMeta))));
                    }catch (Exception ignored) {}
                }

                DataConverter converter = Reflection.getConverter();
                if (converter.isSpawnEgg(check) && converter.isSpawnEgg(main))
                    values.add(converter.getTypeFromItem(main) == converter.getTypeFromItem(check));

                if (!values.isEmpty()) return !values.contains(false);
            }
        }

        return main.isSimilar(check);
    }

    public ItemStack getRawItem() {
        return is;
    }

    /**
     * @Deprecated
     *   This method does not save the data changed for the Meta, It will in the future save it.
     */
    @Deprecated
    public <T extends ItemMeta> ItemBuilder handleMeta (Class<T> clazz, Return<T> meta){
        if (im != null) return this;
        if (im.getClass().isAssignableFrom(clazz)) meta.run((T) im);
        return this;
    }

    private String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    private List<String> translate(List<String> message) {
        ArrayList<String> newLore = new ArrayList<>();
        message.forEach(msg -> newLore.add(translate(msg)));
        return newLore;
    }
    private GameProfile createProfile(String data) {
        Valid.notNull(data, "data can not be null");

        JSONObject SKULL = (JSONObject) JSON.getOrDefault("skullData", new JSONObject());
        try {
            GameProfile profile = new GameProfile(UUID.randomUUID(), String.valueOf(SKULL.getOrDefault("owner", "Steve")));
            PropertyMap propertyMap = profile.getProperties();
            Property property = new Property("textures", data);
            propertyMap.put("textures", property);
            return profile;
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }
    private SkullMeta applyTextureToMeta(SkullMeta meta, GameProfile profile) {
        Valid.notNull(meta, "meta cannot be null");
        Valid.notNull(profile, "profile cannot be null");
        Class craftMetaSkull = Reflection.getCBCClass("inventory.CraftMetaSkull");
        Class c = craftMetaSkull.cast(meta).getClass();
        FieldAccessor field = FieldAccessor.getField(c, "profile", GameProfile.class);
        field.set(meta, profile);
        return meta;
    }
    private GameProfile getGameProfile(SkullMeta meta) {
        Valid.notNull(meta, "meta cannot be null");
        Class craftMetaSkull = Reflection.getCBCClass("inventory.CraftMetaSkull");
        Class c = craftMetaSkull.cast(meta).getClass();
        FieldAccessor<GameProfile> field = FieldAccessor.getField(c, "profile", GameProfile.class);
        return field.get(meta);
    }
    private String getTexture (GameProfile profile) {
        PropertyMap propertyMap = profile.getProperties();
        Collection<Property> properties = propertyMap.get("textures");
        String text = "";

        for (Property property : properties) {
            if (property.getName().equals("textures")) {
                text = property.getValue();
                break;
            }
        }
        return text;
    }
}