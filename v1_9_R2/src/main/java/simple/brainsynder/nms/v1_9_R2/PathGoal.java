package simple.brainsynder.nms.v1_9_R2;

import net.minecraft.server.v1_9_R2.Entity;
import net.minecraft.server.v1_9_R2.EntityInsentient;
import net.minecraft.server.v1_9_R2.PathfinderGoal;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import simple.brainsynder.nms.IPathfinderGoal;

public class PathGoal implements IPathfinderGoal<PathfinderGoal> {
    @Override
    public void addGoal(LivingEntity var1, int var2, PathfinderGoal var3) {
        Entity entity = ((CraftEntity)var1).getHandle();
        EntityInsentient insentient = (EntityInsentient)entity;
        insentient.goalSelector.a(var2, var3);
    }
}
