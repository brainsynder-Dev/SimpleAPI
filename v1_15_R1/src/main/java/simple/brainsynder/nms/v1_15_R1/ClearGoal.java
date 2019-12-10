package simple.brainsynder.nms.v1_15_R1;

import net.minecraft.server.v1_15_R1.EntityInsentient;
import net.minecraft.server.v1_15_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_15_R1.PathfinderGoalSelector;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import simple.brainsynder.nms.IClearGoals;
import simple.brainsynder.utils.Reflection;

import java.util.Set;

public class ClearGoal implements IClearGoals {
    @Override
    public void clearGoals(org.bukkit.entity.Entity e) {
        EntityInsentient creature = (EntityInsentient) ((CraftEntity) e).getHandle();
        ((Set) Reflection.getPrivateField("d", PathfinderGoalSelector.class, creature.goalSelector)).clear();
        ((Set)Reflection.getPrivateField("d", PathfinderGoalSelector.class, creature.targetSelector)).clear();
        creature.goalSelector.a(0, new PathfinderGoalFloat(creature));
    }
}
