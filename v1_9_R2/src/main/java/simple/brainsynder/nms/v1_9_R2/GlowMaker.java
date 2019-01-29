package simple.brainsynder.nms.v1_9_R2;

import net.minecraft.server.v1_9_R2.ItemStack;
import net.minecraft.server.v1_9_R2.NBTTagCompound;
import net.minecraft.server.v1_9_R2.NBTTagList;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import simple.brainsynder.nms.IGlow;

public class GlowMaker implements IGlow {
    @Override
    public org.bukkit.inventory.ItemStack addItemGlow(org.bukkit.inventory.ItemStack item) {
        ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag())
        {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) {
            tag = nmsStack.getTag();
        }
        NBTTagList ench = new NBTTagList();
        if (tag != null) {
            tag.set("ench", ench);
        }
        nmsStack.setTag (tag);
        return CraftItemStack.asCraftMirror (nmsStack);    }
}
