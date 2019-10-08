package simple.brainsynder.listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import simple.brainsynder.Core;
import simple.brainsynder.api.ItemMaker;
import simple.brainsynder.nms.ITellraw;
import simple.brainsynder.utils.Reflection;
import simple.brainsynder.utils.plugin_use.Perms;

import java.util.HashMap;
import java.util.Map;

public class LightDetector implements Listener {
    
    @EventHandler
    public void preCraft(CraftItemEvent e) {
        try {
            if (!e.getRecipe().getResult().isSimilar(Core.getInstance().getLLDetector().create()))
                if (e.getInventory().contains(Core.getInstance().getLLDetector().create())) {
                    e.setCancelled(true);
                }
            if (!e.getRecipe().getResult().isSimilar(Core.getInstance().getTextureFinder().create()))
                if (e.getInventory().contains(Core.getInstance().getTextureFinder().create())) {
                    e.setCancelled(true);
                }
        } catch (Exception ignored) {
        }
    }
    
    private Map<String, Boolean> already = new HashMap<>();
    
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        try {
            Player player = e.getPlayer();
            ItemStack item = player.getItemInHand();
            if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
                return;
            if (e.getClickedBlock() == null)
                return;
            if (item == null)
                return;
            if (!Perms.LIGHTDETECTOR_USE.has(player))
                return;
            if (Core.getInstance().getLLDetector().isSimilar(new ItemMaker(item))) {
                if (already.containsKey(player.getName())) {
                    boolean var = already.get(player.getName());
                    if (var) {
                        already.put(player.getName(), !var);
                        return;
                    }
                }
                already.put(player.getName(), true);
                Block block = e.getClickedBlock().getRelative(BlockFace.UP);
                byte ll = block.getLightLevel();
                ITellraw raw = Reflection.getTellraw("Hover for LightLevel Information");
                raw.color(ChatColor.AQUA);
                if (block.getType().isSolid()) {
                    raw.tooltip(
                            "§cThere is a block on top of the",
                            "§ctargeted block, Requires to have",
                            "§cair above the selected block."
                    );
                } else {
                    raw.tooltip(
                            "§bLight Level Data §3§m-------",
                            "§bLightLevel§3: §7" + ll,
                            "§bMobs Spawn§3: §7" + (ll <= 7),
                            "§bSilverfish Spawn§3: §7" + (ll <= 11),
                            "§bIce Melts§3: §7" + (ll >= 11),
                            "§bMushroom Spread§3: §7" + (ll <= 12),
                            "§bSaplings Grow§3: §7" + (ll >= 9),
                            "§bStem Plants Grow§3: §7" + (ll >= 9),
                            "§bPlants Uproot§3: §7" + (ll <= 7),
                            "§bPlants Don't grow§3: §7" + (ll == 8),
                            "§bGrass spreads§3: §7" + (ll >= 9),
                            "§bDirt accepts spread§3: §7" + (ll > 3)
                    );
                }
                raw.send(player);
            }
        } catch (Exception ignored) {
        }
    }
}
