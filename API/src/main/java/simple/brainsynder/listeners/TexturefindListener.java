package simple.brainsynder.listeners;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import simple.brainsynder.Core;
import simple.brainsynder.api.ItemMaker;
import simple.brainsynder.events.SkullTextureEvent;
import simple.brainsynder.nms.ITellraw;
import simple.brainsynder.nms.ITileEntitySkull;
import simple.brainsynder.utils.Perms;
import simple.brainsynder.utils.Reflection;

import java.util.Collection;

public class TexturefindListener implements Listener {
    
    @EventHandler
    public void onClickSkull(PlayerInteractEvent e) {
        try {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                ItemStack item = e.getPlayer().getItemInHand();
                if (item == null || item.getType() == Material.AIR)
                    return;
                if (!Perms.TEXTUREGETTER_USE.has(e.getPlayer()))
                    return;
                if (Core.getInstance().getTextureFinder().isSimilar(new ItemMaker(item))) {
                    Block b = e.getClickedBlock();
                    Skull skull = (Skull) b.getState();
                    ITileEntitySkull skullTile = Reflection.getTileSkull(skull);
                    GameProfile profile = skullTile.getGameProfile();
                    Collection<Property> textures = profile.getProperties().get("textures");
                    String text = "";
                    for (Property texture : textures) {
                        text = texture.getValue();
                    }
                    String decoded = Base64Coder.decodeString(text);
                    JSONParser parse = new JSONParser();
                    try {
                        Object url = parse.parse(decoded);
                        JSONObject texture = (JSONObject) url;
                        JSONObject SKIN = (JSONObject) texture.get("textures");
                        JSONObject dlurl = (JSONObject) SKIN.get("SKIN");
                        String result = String.valueOf(dlurl.get("url"));
                        SkullTextureEvent event = new SkullTextureEvent(e.getPlayer(), result);
                        Bukkit.getServer().getPluginManager().callEvent(event);
                        ITellraw tellraw = Reflection.getTellraw("Skull Texture Link Download: ");
                        tellraw.color(ChatColor.YELLOW);
                        tellraw.then("HERE");
                        tellraw.style(new ChatColor[]{ChatColor.UNDERLINE});
                        tellraw.link(result);
                        tellraw.tooltip("Click here to go to the link", "to get the skin URL for the skull.");
                        tellraw.send(e.getPlayer());
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }
}
