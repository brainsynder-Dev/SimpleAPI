package simple.brainsynder.storage;

import lombok.Getter;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemStorage implements ConfigurationSerializable {
    @Getter
    private ItemStack[] items;

    public ItemStorage(ItemStack... items) {
        this.items = items;
    }

    public ItemStorage(Map<String, Object> config) {
        if (!config.isEmpty()) {
            Map<String, Object> items = ((MemorySection) config.get("items")).getValues(false);
            for (String slot : items.keySet()) {
                ItemStack item = (ItemStack) items.get(slot);
                this.items = new ItemStack[items.keySet().size()];
                int i = Integer.parseInt(slot);
                this.items[i] = item;
            }
        }
    }

    @Override public Map<String, Object> serialize() {
        Map map = new HashMap();
        if (items == null)
            return map;
        int i = 0;
        Map itemMap = new HashMap();
        for (ItemStack item : items) {
            itemMap.put(i, item);
            i++;
        }
        map.put("items", itemMap);
        return map;
    }

    public static ItemStorage deserialize(Map<String, Object> config) {
        return new ItemStorage(config);
    }
}
