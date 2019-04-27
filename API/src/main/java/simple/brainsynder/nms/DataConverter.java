package simple.brainsynder.nms;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.nms.materials.*;
import simple.brainsynder.nms.materials.types.*;
import simple.brainsynder.utils.MatType;
import simple.brainsynder.utils.SkullType;

public class DataConverter {
    public Data getColoredMaterial(MatType type, int data) {
        if (type == MatType.DYE) type = MatType.INK_SACK;
        return new Data(findMaterial(type.name()), data);
    }

    public ItemStack getSpawnEgg (EntityType type, ItemStack stack) {
        stack.setDurability(type.getTypeId());
        return stack;
    }

    public EntityType getTypeFromItem (ItemStack stack) {
        if (stack.getType() != Material.MONSTER_EGG) return EntityType.UNKNOWN;
        short id = stack.getDurability();
        if (id < 0)  return EntityType.UNKNOWN;
        return EntityType.fromId((int)id);
    }

    public boolean isSpawnEgg (ItemStack stack) {
        return  (getTypeFromItem(stack) != EntityType.UNKNOWN);
    }

    public Data getSkullMaterial(SkullType type) {
        return new Data(Material.SKULL_ITEM, type.ordinal());
    }
    public Data getMaterial (WrappedType type) {
        Material material = Material.AIR;
        if (type instanceof CoalType) material = Material.COAL;
        if (type instanceof CobbleWallType) material = Material.COBBLE_WALL;
        if (type instanceof DirtType) material = Material.DIRT;
        if (type instanceof FlowerType) {
            FlowerType flower = (FlowerType)type;
            material = (flower == FlowerType.DANDELION) ? Material.YELLOW_FLOWER : Material.RED_ROSE;
        }
        if (type instanceof GoldAppleType) material = Material.GOLDEN_APPLE;
        if (type instanceof GrassType) material = Material.LONG_GRASS;
        if (type instanceof PrismarineType) material = Material.PRISMARINE;
        if (type instanceof QuartzType) material = Material.QUARTZ_BLOCK;
        if (type instanceof SandType) material = Material.SAND;
        if (type instanceof SpongeType) material = Material.SPONGE;
        if (type instanceof StoneBrickType) material = Material.SMOOTH_BRICK;
        if (type instanceof StoneType) material = Material.STONE;
        if (type instanceof TallPlantType) material = Material.DOUBLE_PLANT;
        return new Data(material, type.getData());
    }
    public Data getSandStone (SandType sandType, SandStoneType stoneType) {
        Material material = Material.SANDSTONE;
        if (sandType == SandType.RED_SAND) material = Material.RED_SANDSTONE;
        return new Data(material, stoneType.getData());
    }
    public Data getSlabMaterial (StoneSlabType type, boolean single) {
        Material material = Material.DOUBLE_STEP;
        if (single) material = Material.STEP;
        return new Data(material, type.getData());
    }
    public Data getFishMaterial (FishType type, boolean cooked) {
        int data = type.getData();
        Material material = Material.RAW_FISH;
        if (cooked) {
            material = Material.COOKED_FISH;
            if (data > 1) data = 0;
        }
        return new Data(material, data);
    }
    public Data getWoodMaterial(WoodSelector selector, WoodType type) {
        Data data = new Data(Material.AIR, -1);
        if ((selector == WoodSelector.LOG) || (selector == WoodSelector.LEAVES)) {
            Material material = findMaterial((selector == WoodSelector.LOG) ? type.getLogLegacy() : type.getLeaveLegacy());
            if (material != Material.AIR) data.setMaterial(material);
            data.setData(type.getData());
        }else{
            String target = selector.name();
            if (selector == WoodSelector.PLANKS) target = "WOOD";
            if (selector == WoodSelector.SLAB) target = "WOOD_STEP";
            if (selector == WoodSelector.DOUBLE_SLAB) target = "WOOD_DOUBLE_STEP";

            Material material = findMaterial(target);
            if (material != Material.AIR) data.setMaterial(material);
            data.setData(type.ordinal());
        }
        return data;
    }


    public Material findMaterial(String name) {
        try {
            return Material.valueOf(name);
        } catch (Exception ignored) {
        }

        try {
            return Material.matchMaterial(name);
        } catch (Exception ignored) {
        }

        return Material.AIR;
    }

    public static class Data {
        private Material material;
        private int data = -1;

        public Data(Material material, int data) {
            this.material = material;
            this.data = data;
        }

        public Data() {
            this.material = Material.AIR;
        }

        void setData(int data) {
            this.data = data;
        }

        void setMaterial(Material material) {
            this.material = material;
        }

        public Material getMaterial() {
            return material;
        }

        public int getData() {
            return data;
        }

        public ItemBuilder toBuilder(int amount) {
            return new ItemBuilder(material, amount).withData(data);
        }
    }


}
