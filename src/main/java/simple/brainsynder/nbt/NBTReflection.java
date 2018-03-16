package simple.brainsynder.nbt;

import lombok.SneakyThrows;
import simple.brainsynder.utils.Reflection;

import java.lang.reflect.Method;

public class NBTReflection {

    @SneakyThrows
    public static Object getNewNBTTag() {
        return Reflection.getNmsClass("NBTTagCompound").newInstance();
    }

    @SneakyThrows
    public static Object setNBTTag(Object NBTTag, Object NMSItem) {
        Method method = NMSItem.getClass().getMethod("setTag", NBTTag.getClass());
        method.invoke(NMSItem, NBTTag);
        return NMSItem;
    }
}
