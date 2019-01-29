package simple.brainsynder.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import simple.brainsynder.nms.IAnvilGUI;

public class AnvilGUI {
    public AnvilGUI(Plugin plugin, Player player, IAnvilClickEvent handler) {
        gui = Reflection.getAnvilMaker(plugin, player, handler);
    }
    
    private static IAnvilGUI gui;
    
    public void setSlot(AnvilSlot slot, ItemStack item) {
        if (gui != null)
            gui.setSlot(slot, item);
    }
    
    public void open() {
        if (gui != null)
            gui.open();
    }
}
