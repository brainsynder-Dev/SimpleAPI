package simple.brainsynder.nms;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.utils.MatType;
import simple.brainsynder.utils.SkullType;

public class DataConverter {
    public Data getColoredMaterial(MatType type, int data) {
        return new Data(findMaterial(type.name()), data);
    }

    public ItemStack getSpawnEgg (EntityType type, ItemStack stack) {
        stack.setDurability(type.getTypeId());
        return stack;
    }

    public EntityType getTypeFromItem (ItemStack stack) {
        if (stack.getType() != Material.MONSTER_EGG) return EntityType.UNKNOWN;
        short id = stack.getDurability();
        if (id < 0)  return EntityType.UNKNOWN;
        return EntityType.fromId((int)id);
    }

    public boolean isSpawnEgg (ItemStack stack) {
        return  (getTypeFromItem(stack) != EntityType.UNKNOWN);
    }

    public Data getSkullMaterial(SkullType type) {
        return new Data(Material.SKULL_ITEM, type.ordinal());
    }

    public Material findMaterial(String name) {
        try {
            return Material.valueOf(name);
        } catch (Exception ignored) {
        }

        try {
            return Material.matchMaterial(name);
        } catch (Exception ignored) {
        }

        return Material.AIR;
    }

    public static class Data {
        public Material material;
        public int data = -1;

        public Data(Material material, int data) {
            this.material = material;
            this.data = data;
        }

        public ItemBuilder toBuilder(int amount) {
            return new ItemBuilder(material, amount).withData(data);
        }
    }

}
