package simple.brainsynder.nms.v1_10_R1;

import net.minecraft.server.v1_10_R1.Entity;
import net.minecraft.server.v1_10_R1.EntityInsentient;
import net.minecraft.server.v1_10_R1.PathfinderGoal;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
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
