package simple.brainsynder.nms.v1_9_R2;

import net.minecraft.server.v1_9_R2.EntityInsentient;
import net.minecraft.server.v1_9_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_9_R2.PathfinderGoalSelector;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftEntity;
import simple.brainsynder.nms.IClearGoals;
import simple.brainsynder.utils.Reflection;

import java.util.Set;

public class ClearGoal implements IClearGoals {
    @Override
    public void clearGoals(org.bukkit.entity.Entity e) {
        EntityInsentient creature = (EntityInsentient) ((CraftEntity) e).getHandle();
        ((Set)Reflection.getPrivateField("b", PathfinderGoalSelector.class, creature.goalSelector)).clear();
        ((Set)Reflection.getPrivateField("c", PathfinderGoalSelector.class, creature.goalSelector)).clear();
        ((Set)Reflection.getPrivateField("b", PathfinderGoalSelector.class, creature.targetSelector)).clear();
        ((Set)Reflection.getPrivateField("c", PathfinderGoalSelector.class, creature.targetSelector)).clear();
        creature.goalSelector.a(0, new PathfinderGoalFloat(creature));
    }
}
