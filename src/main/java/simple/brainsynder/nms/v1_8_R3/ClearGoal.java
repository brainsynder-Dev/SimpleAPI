package simple.brainsynder.nms.v1_8_R3;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import simple.brainsynder.nms.IClearGoals;
import simple.brainsynder.utils.Reflection;

import java.util.List;

public class ClearGoal implements IClearGoals {
    @Override
    public void clearGoals(org.bukkit.entity.Entity e) {
        EntityInsentient creature = (EntityInsentient) ((CraftEntity) e).getHandle();
        ((List)Reflection.getPrivateField("b", PathfinderGoalSelector.class, creature.goalSelector)).clear();
        ((List)Reflection.getPrivateField("c", PathfinderGoalSelector.class, creature.goalSelector)).clear();
        ((List)Reflection.getPrivateField("b", PathfinderGoalSelector.class, creature.targetSelector)).clear();
        ((List)Reflection.getPrivateField("c", PathfinderGoalSelector.class, creature.targetSelector)).clear();
        creature.goalSelector.a(0, new PathfinderGoalFloat(creature));
    }
}
