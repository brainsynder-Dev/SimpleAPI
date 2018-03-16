/*
 * Copyright (c) created class file on: 2016.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */

package simple.brainsynder.nms.v1_9_R1;

import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.nms.IAnvilGUI;
import simple.brainsynder.utils.AnvilClickEvent;
import simple.brainsynder.utils.AnvilSlot;
import simple.brainsynder.utils.IAnvilClickEvent;
import simple.brainsynder.utils.Valid;

import java.util.HashMap;

public class HandleAnvilGUI implements IAnvilGUI {
    public static class AnvilContainer extends ContainerAnvil {
        public AnvilContainer(EntityHuman entity) {
            super(entity.inventory, entity.world, new BlockPosition(0, 0, 0), entity);
        }

        @Override
        public boolean a(EntityHuman entityhuman) {
            return true;
        }
    }
    
    private Player player;
    private HashMap<AnvilSlot, ItemStack> items = new HashMap<>();
    private Inventory inv;
    private LIST listener;
    private int levels = 0;
    private float exp = 0.0F;
    private Plugin plugin;
    private IAnvilClickEvent handler;
    
    public HandleAnvilGUI(Plugin plugin, final Player player, final IAnvilClickEvent handler) {
        this.player = player;
        this.plugin = plugin;
        this.handler = handler;
        listener = new LIST();
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }
    
    private class LIST implements Listener {
        
        @EventHandler(ignoreCancelled = true)
        public void onInventoryClick(InventoryClickEvent event) {
            if (event.getWhoClicked() instanceof Player) {
                
                if (event.getInventory().equals(inv)) {
                    ItemStack item = event.getCurrentItem();
                    int slot = event.getRawSlot();
                    String name = "";
                    
                    if (item != null) {
                        if (item.hasItemMeta()) {
                            ItemMeta meta = item.getItemMeta();
                            
                            if (meta.hasDisplayName()) {
                                name = meta.getDisplayName();
                            }
                        }
                    }
                    
                    if (player != null) {
                        if (player.getGameMode() == GameMode.ADVENTURE
                                || player.getGameMode() == GameMode.SURVIVAL
                                && player.getLevel() > 0) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (player == null)
                                        return;
                                    player.setLevel(player.getLevel());
                                    player.setExp(player.getExp());
                                }
                            }.runTaskLater(plugin, 2);
                        }
                    }
                    
                    AnvilClickEvent clickEvent = new AnvilClickEvent(event.getInventory(), AnvilSlot.bySlot(slot), name, event.getInventory().getItem(2));
                    handler.onAnvilClick(clickEvent);
                    Bukkit.getServer().getPluginManager().callEvent(clickEvent);
                    event.setCancelled(clickEvent.isCanceled());
                    if (clickEvent.getWillClose()) {
                        event.getWhoClicked().closeInventory();
                    }
                    if (clickEvent.getWillDestroy()) {
                        destroy();
                    }
                }
            }
        }
        
        @EventHandler(ignoreCancelled = true)
        public void onInventoryClose(InventoryCloseEvent event) {
            if (event.getPlayer() instanceof Player) {
                Inventory inv = event.getInventory();
                if (inv.equals(HandleAnvilGUI.this.inv)) {
                    inv.clear();
                    destroy();
                }
            }
        }
        
        @EventHandler(ignoreCancelled = true)
        public void onPlayerQuit(PlayerQuitEvent event) {
            if (event.getPlayer().equals(getPlayer())) {
                destroy();
            }
        }
    }
    
    public Player getPlayer() {
        return player;
    }
    
    @Override
    public void setSlot(AnvilSlot slot, ItemStack item) {
        items.put(slot, item);
    }
    
    @Override
    public void open() {
        levels = player.getLevel();
        exp = player.getExp();
        EntityPlayer p = ((CraftPlayer) player).getHandle();
        AnvilContainer container = new AnvilContainer(p);
        this.inv = container.getBukkitView().getTopInventory();
        for (AnvilSlot slot : this.items.keySet()) {
            this.inv.setItem(slot.getSlot(), items.get(slot));
        }
        int c = p.nextContainerCounter();
        p.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage("Anvil"), 0));
        p.activeContainer = container;
        p.activeContainer.windowId = c;
        p.activeContainer.addSlotListener(p);
        player.setLevel(50);
    }
    
    public void destroy() {
        Valid.notNull(player, "Player is null");
        Valid.notNull(exp, "exp is null");
        Valid.notNull(levels, "levels is null");
        player.setExp(exp);
        player.setLevel(levels);
        items = null;
        
        HandlerList.unregisterAll(listener);
        
        listener = null;
    }
}
