package simple.brainsynder.nms.v1_13_R2;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import simple.brainsynder.nms.DataConverter;
import simple.brainsynder.nms.materials.*;
import simple.brainsynder.nms.materials.types.*;
import simple.brainsynder.utils.MatType;
import simple.brainsynder.utils.SkullType;
import simple.brainsynder.wrappers.DyeColorWrapper;

public class DataConverterHandler extends DataConverter {
    @Override
    public Data getColoredMaterial(MatType type, int data) {
        DyeColorWrapper dye = DyeColorWrapper.getByWoolData((byte) data);
        if (type == MatType.INK_SACK) dye = DyeColorWrapper.getByDyeData((byte) data);

        String name = dye.name();
        if (name.equalsIgnoreCase("SILVER")) name = "LIGHT_GRAY";


        Material material;
        if ((type == MatType.INK_SACK) || (type == MatType.DYE)) {
            if (dye == DyeColorWrapper.WHITE) {
                material = Material.BONE_MEAL;
            } else if (dye == DyeColorWrapper.YELLOW) {
                material = Material.DANDELION_YELLOW;
            } else if (dye == DyeColorWrapper.BLUE) {
                material = Material.LAPIS_LAZULI;
            } else if (dye == DyeColorWrapper.BROWN) {
                material = Material.COCOA_BEANS;
            } else if (dye == DyeColorWrapper.GREEN) {
                material = Material.CACTUS_GREEN;
            } else if (dye == DyeColorWrapper.RED) {
                material = Material.ROSE_RED;
            } else if (dye == DyeColorWrapper.BLACK) {
                material = Material.INK_SAC;
            } else {
                material = findMaterial(name + "_DYE");
            }
        } else {
            material = findMaterial(name + "_" + type.getName());
        }


        return new Data(material, -1);
    }

    @Override
    public Data getSkullMaterial(SkullType type) {
        Material material = Material.PLAYER_HEAD;
        switch (type) {
            case DRAGON:
                material = Material.DRAGON_HEAD;
                break;
            case CREEPER:
                material = Material.CREEPER_HEAD;
                break;
            case ZOMBIE:
                material = Material.ZOMBIE_HEAD;
                break;
            case SKELETON:
                material = Material.SKELETON_SKULL;
                break;
            case WITHER:
                material = Material.WITHER_SKELETON_SKULL;
                break;
        }

        return new Data(material, -1);
    }

    @Override
    public EntityType getTypeFromItem(ItemStack stack) {
        Material type = stack.getType();
        if (type.name().contains("SPAWN_EGG")) {
            try {
                return EntityType.valueOf(type.name().replace("_SPAWN_EGG", ""));
            }catch (Exception ignored){}
        }
        if (!stack.hasItemMeta()) return EntityType.UNKNOWN;
        ItemMeta meta = stack.getItemMeta();
        if (!(meta instanceof SpawnEggMeta)) return EntityType.UNKNOWN;
        return ((SpawnEggMeta)meta).getSpawnedType();
    }
    
    @Override
    public boolean isSpawnEgg(ItemStack stack) {
        return (stack.getType().name().contains("SPAWN_EGG"));
    }
    
    @Override
    public ItemStack getSpawnEgg(EntityType type, ItemStack stack) {
        Material material = findMaterial(type.name()+"_SPAWN_EGG");
        stack.setType(material);
        return stack;
    }

    @Override
    public Material findMaterial(String name) {
        try {
            return Material.valueOf(name);
        } catch (Exception ignored) {
        }

        try {
            return Material.valueOf("LEGACY_" + name);
        } catch (Exception ignored) {
        }

        try {
            return Material.matchMaterial(name);
        } catch (Exception ignored) {
        }

        try {
            return Material.matchMaterial(name, true);
        } catch (Exception ignored) {
        }

        return super.findMaterial(name);
    }

    @Override
    public Data getWoodMaterial(WoodSelector selector, WoodType type) {
        String name = type.name();
        if (selector == WoodSelector.DOUBLE_SLAB) {
            name = name+"_SLAB";
        }else{
            name = name+"_"+selector.name();
        }
        return new Data(findMaterial(name), -1);
    }

    @Override
    public Data getFishMaterial(FishType type, boolean cooked) {
        String prefix = ((cooked) ? "COOKED_" : "");
        Material material = Material.COD;
        if (type == FishType.SALMON) material = Material.SALMON;
        if ((type == FishType.PUFFERFISH) && (!cooked)) material = Material.PUFFERFISH;
        if ((type == FishType.CLOWNFISH) && (!cooked)) material = Material.TROPICAL_FISH;
        return new Data(findMaterial(prefix+material.name()), -1);
    }

    @Override
    public Data getSlabMaterial(StoneSlabType type, boolean single) {
        String name = type.getName();
        if (type == StoneSlabType.WOODEN) name = "PETRIFIED_OAK";
        return new Data(findMaterial(name+"_SLAB"), -1);
    }

    @Override
    public Data getMaterial(WrappedType type) {
        Material material = Material.AIR;
        if (type instanceof GrassType) {
            GrassType value = (GrassType) type;
            switch (value) {
                case DEAD:
                    material = Material.DEAD_BUSH;
                    break;
                case NORMAL:
                    material = Material.GRASS;
                    break;
                case FERN_LIKE:
                    material = Material.FERN;
                    break;
            }
        }else if (type instanceof PrismarineType) {
            PrismarineType value = (PrismarineType) type;
            switch (value) {
                case REGULAR:
                    material = Material.PRISMARINE;
                    break;
                case BRICKS:
                    material = Material.PRISMARINE_BRICKS;
                    break;
                case DARK:
                    material = Material.DARK_PRISMARINE;
                    break;
            }
        }else if (type instanceof QuartzType) {
            QuartzType value = (QuartzType) type;
            switch (value) {
                case REGULAR:
                    material = Material.QUARTZ_BLOCK;
                    break;
                case CHISELED:
                    material = Material.CHISELED_QUARTZ_BLOCK;
                    break;
                case PILLAR:
                    material = Material.QUARTZ_PILLAR;
                    break;
            }
        }else if (type instanceof MonsterEggType) {
            MonsterEggType value = (MonsterEggType) type;
            material = findMaterial("INFESTED_"+value.name());
        }else if (type instanceof GoldAppleType) {
            GoldAppleType value = (GoldAppleType) type;
            material = ((value == GoldAppleType.REGULAR) ? Material.GOLDEN_APPLE : Material.ENCHANTED_GOLDEN_APPLE);
        }else if (type instanceof CobbleWallType) {
            CobbleWallType value = (CobbleWallType) type;
            material = ((value == CobbleWallType.COBBLE) ? Material.COBBLESTONE_WALL : Material.MOSSY_COBBLESTONE_WALL);
        }else{
            material = findMaterial(type.getName());
        }
        return new Data(material, -1);
    }

    @Override
    public Data getSandStone(SandType sandType, SandStoneType stoneType) {
        String name = sandType.name()+"STONE";
        if (stoneType != SandStoneType.REGULAR) name = stoneType.name()+"_"+name;
        return new Data(findMaterial(name), -1);
    }
}
