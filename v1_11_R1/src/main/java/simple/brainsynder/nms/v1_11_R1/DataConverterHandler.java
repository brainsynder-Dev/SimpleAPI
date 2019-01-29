package simple.brainsynder.nms.v1_11_R1;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import simple.brainsynder.nms.DataConverter;

public class DataConverterHandler extends DataConverter {
    @Override
    public EntityType getTypeFromItem(ItemStack stack) {
        if (!stack.hasItemMeta()) return EntityType.UNKNOWN;
        ItemMeta meta = stack.getItemMeta();
        if (!(meta instanceof SpawnEggMeta)) return EntityType.UNKNOWN;
        return ((SpawnEggMeta)meta).getSpawnedType();
    }

    @Override
    public ItemStack getSpawnEgg(EntityType type, ItemStack stack) {
        if (stack.getType() != Material.MONSTER_EGG) return stack;
        if (!stack.hasItemMeta()) return stack;
        SpawnEggMeta meta = (SpawnEggMeta) stack.getItemMeta();
        meta.setSpawnedType(type);
        stack.setItemMeta(meta);
        return stack;
    }
}
