package simple.brainsynder.nms.v1_13_R2;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import simple.brainsynder.nms.DataConverter;
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
        if (type == MatType.INK_SACK) {
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
}
