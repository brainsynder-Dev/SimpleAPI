package simple.brainsynder.api;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import simple.brainsynder.exceptions.InvalidMaterialException;

public class LeatherArmorMaker extends ItemMaker {
    private ItemStack item;
    private Color color;

    public LeatherArmorMaker(ItemStack item) {
        super(item);
        if (!item.getType().toString().startsWith("LEATHER_")) {
            try {
                throw new InvalidMaterialException("LeatherArmorMaker", "Material " + item.getType().toString() + " is not a piece of Leather Armor");
            } catch (InvalidMaterialException e) {
                e.printStackTrace();
            }
            return;
        }
        this.item = item;
    }

    public LeatherArmorMaker(Material mat, short data) {
        this(new ItemStack(mat, 1, data));
    }

    public LeatherArmorMaker(Material mat) {
        this(mat, (short) 0);
    }

    public LeatherArmorMaker setColor(Color color) {
        this.color = color;
        return this;
    }

    public LeatherArmorMaker setColor(int r, int g, int b) {
        assert (r >= 0) || (r <= 255);
        assert (g >= 0) || (g <= 255);
        assert (b >= 0) || (b <= 255);
        this.color = Color.fromRGB(r, g, b);
        return this;
    }

    public Color getColor() {
        return color;
    }

    public LeatherArmorMaker clone() {
        LeatherArmorMaker maker = new LeatherArmorMaker(this.item);
        maker.setColor(this.color);
        maker._name = _name;
        maker._lore = _lore;
        maker._amount = _amount;
        maker.enchantmentMap = enchantmentMap;
        maker.fakeEnchant = fakeEnchant;
        return maker;
    }

    @Override
    public ItemStack create() {
        ItemStack itemStack = this.item;
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        if (color != null)
            meta.setColor(this.color);
        if (_name != null)
            meta.setDisplayName(_name);
        if (!_lore.isEmpty())
            meta.setLore(_lore.toList());
        itemStack.setItemMeta(meta);
        if (!enchantmentMap.isEmpty()) {
            for (Enchantment enchant : enchantmentMap.keySet()) {
                itemStack.addUnsafeEnchantment(enchant, enchantmentMap.getKey(enchant));
            }
        }
        if (fakeEnchant && enchantmentMap.isEmpty())
            itemStack = addGlow(itemStack);
        return itemStack;
    }
}