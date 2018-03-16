package simple.brainsynder.api;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import simple.brainsynder.reflection.FieldAccessor;
import simple.brainsynder.utils.Reflection;
import simple.brainsynder.utils.Valid;

import java.util.ArrayList;
import java.util.UUID;

public class SkullMaker extends ItemMaker{
    private String _owner;
    private String skullOwner = "SimpleAPI";
    public SkullMaker() {
        super(Material.SKULL_ITEM, (byte)3);
    }

    public SkullMaker setOwner(String name) {
        _owner = name;
        return this;
    }

    public boolean isSimilar (SkullMaker maker) {
        if (_name.equals(maker._name)) {
            if (_lore.equals(maker._lore)) {
                if (skullOwner.equals(maker.skullOwner)) {
                    return _owner.equals(maker._owner);
                }
            }
        }
        return false;
    }

    public SkullMaker clone() {
        SkullMaker maker = new SkullMaker();
        maker._name = _name;
        maker._lore = _lore;
        maker._amount = _amount;
        maker.enchantmentMap = enchantmentMap;
        maker.fakeEnchant = fakeEnchant;
        maker.skullOwner = skullOwner;
        maker._owner = _owner;
        return maker;
    }

    /**
     * Sets who the skull will be registered to.
     */
    public SkullMaker setSkullOwner (String skullOwner) {
        this.skullOwner = skullOwner;
        return this;
    }

    /**
     * @Deprecated
     */
    @Deprecated
    public SkullMaker setUrl(String name) {
        _owner = name;
        return this;
    }

    public SkullMaker setOwner(Player player) {
        _owner = player.getName();
        return this;
    }

    public SkullMaker setOwner(OfflinePlayer offlinePlayer) {
        _owner = offlinePlayer.getName();
        return this;
    }

    public ItemStack create() {
        ItemStack item = new ItemStack(_material, _amount, _data);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (_owner.length() > 17) {
            meta = applyTextureToMeta(meta, createProfile(_owner));
        } else {
            meta.setOwner(_owner);
        }
        if (_name != null)
            meta.setDisplayName(_name);
        if (!_lore.isEmpty())
            meta.setLore(_lore.toList());
        if (!flags.isEmpty()) {
            ArrayList<ItemFlag> itemFlags = flags.toArrayList();
            meta.addItemFlags(itemFlags.toArray(new ItemFlag[itemFlags.size()]));
        }
        item.setItemMeta(meta);
        if (!enchantmentMap.isEmpty()) {
            for (Enchantment enchant : enchantmentMap.keySet()) {
                item.addUnsafeEnchantment(enchant, enchantmentMap.getKey(enchant));
            }
        }
        if (fakeEnchant && enchantmentMap.isEmpty())
            item = addGlow(item);

        item.setItemMeta(meta);
        return item;
    }

    private GameProfile createProfile(String data) {
        Valid.notNull(data, "data can not be null");
        try {
            GameProfile profile = new GameProfile(UUID.randomUUID(), skullOwner);
            PropertyMap propertyMap = profile.getProperties();
            Property property = new Property("textures", data);
            propertyMap.put("textures", property);
            return profile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
}
