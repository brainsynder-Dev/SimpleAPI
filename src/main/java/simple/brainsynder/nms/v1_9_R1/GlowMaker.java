package simple.brainsynder.nms.v1_9_R1;

import net.minecraft.server.v1_9_R1.ItemStack;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.NBTTagList;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;

public class GlowMaker implements simple.brainsynder.nms.IGlow {
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
        tag.set("ench", ench);
        nmsStack.setTag (tag);
        return CraftItemStack.asCraftMirror (nmsStack);    }
}
