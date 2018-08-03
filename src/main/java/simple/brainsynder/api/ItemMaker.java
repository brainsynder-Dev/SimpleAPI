package simple.brainsynder.api;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import simple.brainsynder.nms.IGlow;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simple.brainsynder.utils.*;
import simple.brainsynder.wrappers.MaterialWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a little messy...
 * Please use {@link ItemBuilder}
 */
@Deprecated
public class ItemMaker implements Cloneable {
    protected Material _material = Material.AIR;
    protected int _amount = 1;
    protected byte _data = 0;
    protected boolean fakeEnchant = false;
    protected String _name = "ItemStack";
    protected Lore<String> _lore = new Lore<>();
    protected IStorage<ItemFlag> flags = new StorageList<>();
    protected AdvMap<Enchantment, Integer> enchantmentMap = new AdvMap<>();
    
    public ItemMaker(Material material) {
        this(material, (byte) 0);
    }
    
    public ItemMaker(Material material, byte data) {
        _material = material;
        if (ServerVersion.getVersion() != ServerVersion.v1_13_R1) {
            _data = data;
        }
        _amount = 1;
        _name = null;
        enchantmentMap = new AdvMap<>();
    }
    
    public ItemMaker(MaterialWrapper material) {
        this(material.toMaterial(), (byte) 0);
    }
    
    public ItemMaker(MaterialWrapper material, byte data) {
        this(material.toMaterial(), data);
    }
    
    public ItemMaker(ItemStack item) {
        _material = item.getType();
        _data = (byte) item.getDurability();
        _amount = item.getAmount();
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasLore())
                _lore.fromList(meta.getLore());
            if (meta.hasDisplayName())
                _name = meta.getDisplayName();
            if (meta.getItemFlags().size() != 0) {
                for (ItemFlag flag : meta.getItemFlags()) {
                    flags.add(flag);
                }
            }
        }
        enchantmentMap = new AdvMap<>();
        if ((item.getEnchantments() != null) && (!item.getEnchantments().isEmpty())) {
            for (Enchantment enchant : item.getEnchantments().keySet()) {
                try {
                    int lvl = item.getEnchantments().get(enchant);
                    enchantmentMap.put(enchant, lvl);
                } catch (Exception ignored) {
                }
            }
        }
    }
    
    public ItemMaker clone() {
        ItemMaker maker = new ItemMaker(_material, _data);
        maker._name = _name;
        maker._lore = _lore;
        maker._amount = _amount;
        maker.enchantmentMap = enchantmentMap;
        maker.fakeEnchant = fakeEnchant;
        maker.flags = flags.copy();
        return maker;
    }
    
    public ItemMaker setAmount(int amount) {
        _amount = amount;
        return this;
    }
    
    public ItemMaker setFlags(ItemFlag... itemFlags) {
        if (itemFlags != null) {
            for (ItemFlag flag : itemFlags) {
                if (!containsFlag(flag))
                    flags.add(flag);
            }
        }
        return this;
    }
    
    public ItemMaker addFlag(ItemFlag itemFlag) {
        if (itemFlag != null) {
            if (!containsFlag(itemFlag))
                flags.add(itemFlag);
        }
        return this;
    }
    
    public boolean containsFlag(ItemFlag itemFlag) {
        return flags.contains(itemFlag);
    }
    
    public ItemMaker removeFlag(ItemFlag itemFlag) {
        if (containsFlag(itemFlag))
            flags.remove(itemFlag);
        return this;
    }
    
    public ItemMaker enchant(Enchantment enchant, int level) {
        Valid.notNull(enchant, "Enchantment can not be null");
        Valid.notNull(enchant, "");
        if (enchant == null) {
            System.out.println("Enchantment can not be null");
            return this;
        }
        if (level <= 0) {
            System.out.println("Level must be greater than 0");
            return this;
        }
        enchantmentMap.put(enchant, level);
        return this;
    }
    
    public ItemMaker enchant(Enchantment[] enchant, int level) {
        for (Enchantment enchantment : enchant)
            enchant(enchantment, level);
        return this;
    }
    
    public ItemMaker enchant() {
        fakeEnchant = true;
        return this;
    }
    
    public ItemMaker enchant(List<Enchantment> enchant, int level) {
        return enchant((Enchantment[]) enchant.toArray(), level);
    }
    
    public ItemMaker enchant(Enchantment[] enchant, int[] level) {
        if (enchant.length != level.length) {
            System.out.println("Enchantment[] and int[] are not the same length.");
            return this;
        }
        for (int i = 0; i < level.length; i++) {
            enchant(enchant[i], level[i]);
        }
        return this;
    }
    
    public ItemMaker setName(String name) {
        _name = ChatColor.translateAlternateColorCodes('&', name);
        return this;
    }
    
    public Lore<String> getLore() {
        return _lore;
    }
    
    public ItemMaker addLoreLine(String line) {
        if (line == null) {
            _lore.add(" ");
            return this;
        }
        _lore.add(ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', line));
        return this;
    }
    
    public ItemMaker addLoreLine(int lineNum, String line) {
        if (line == null) {
            _lore.add(lineNum, " ");
            return this;
        }
        _lore.add(lineNum, ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', line));
        return this;
    }
    
    public ItemMaker removeLoreLine(int line) {
        if (line > _lore.size()) {
            return this;
        }
        _lore.remove(_lore.get(line));
        return this;
    }
    
    public ItemMaker removeLoreLine(String line) {
        if (!_lore.contains(line)) {
            return this;
        }
        _lore.remove(line);
        return this;
    }
    
    public boolean isSimilar(ItemMaker maker) {
        return create().isSimilar(maker.create());
    }
    
    public ItemStack create() {
        ItemStack item = new ItemStack(_material, _amount, _data);
        ItemMeta meta = item.getItemMeta();
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
        
        return item;
    }
    
    protected ItemStack addGlow(ItemStack item) {
        IGlow glow = Reflection.glowManager();
        if (glow == null)
            return item;
        return glow.addItemGlow(item);
    }
}
