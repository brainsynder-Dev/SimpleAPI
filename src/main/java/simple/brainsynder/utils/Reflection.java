/*
 * Copyright (c) created class file on: 2016.
 * All rights reserved.
 * Copyright owner: brainsynder/Magnus498
 * To contact the developer go to:
 * - spigotmc.org and look up brainsynder
 * - email at: briansnyder498@gmail.com
 * - or Skype at live:starwars4393
 */
package simple.brainsynder.utils;

import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Skull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import simple.brainsynder.nms.*;
import simple.brainsynder.nms.key.*;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Reflection {
    private static HashMap<Class<? extends Entity>, Method> handles = new HashMap<>();

    public static <T> T invokeNMSStaticMethod(String className, String method, Class<?>[] parameterClasses, Object... params) {
        return invokeNMSMethod(className, method, null, parameterClasses, (Object[]) params);
    }

    public static <T> T initiateClass(Class<?> clazz) {
        try {
            return (T) clazz.newInstance();
        } catch (Exception e) {
        }
        return null;
    }

    public static <T> T initiateClass(Constructor<?> constructor, Object... args) {
        try {
            return (T) constructor.newInstance(args);
        } catch (Exception e) {
        }
        return null;
    }

    public static Constructor<?> fillConstructor(Class<?> clazz, Class<?>... values) {
        try {
            return clazz.getDeclaredConstructor(values);
        } catch (Exception e) {
        }
        return null;
    }

    public static <T> T invokeNMSMethod(String className, String method, Object invoker, Class<?>[] parameterClasses, Object... params) {
        Validate.isTrue(parameterClasses.length == params.length, "Parameter array's length must be equal to the params array's length");

        try {
            Class e = getNmsClass(className);
            Method m = e.getDeclaredMethod(method, parameterClasses);
            m.setAccessible(true);
            return (T) m.invoke(invoker, (Object[]) params);
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public static <T> T invokeNMSMethod(String method, Object invoker, Class<?>[] parameterClasses, Object... params) {
        Validate.isTrue(invoker != null, "Invoker cannot be null");
        return invokeNMSMethod(invoker.getClass().getSimpleName(), method, invoker, parameterClasses, (Object[]) params);
    }

    public static <T> T invokeNMSMethod(String method, Object invoker) {
        Validate.isTrue(invoker != null, "Invoker cannot be null");
        return invokeNMSMethod(method, invoker, new Class[0], new Object[0]);
    }

    public static Object newNMS(String className) {
        return newNMS(className, new Class[0]);
    }

    public static Object newNMS(String className, Class<?>[] parameterClasses, Object... params) {
        try {
            Class ex = getNmsClass(className);
            Constructor constructor = ex.getDeclaredConstructor(parameterClasses);
            constructor.setAccessible(true);
            return constructor.newInstance(params);
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public static <T> T getNMSField(Object owner, String fieldName) {
        return getNMSField(owner.getClass().getSimpleName(), owner, fieldName);
    }

    public static <T> T getNMSField(String className, Object owner, String fieldName) {
        try {
            Class ex = getNmsClass(className);
            Field field = ex.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(owner);
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public static <T> T getNMSStaticField(String className, String fieldName) {
        return getNMSField(className, null, fieldName);
    }

    public static <T> T invokeBukkitStaticMethod(String className, String method, Class<?>[] parameterClasses, Object... params) {
        return invokeBukkitMethod(className, method, null, parameterClasses, (Object[]) params);
    }

    public static <T> T invokeBukkitMethod(String className, String method, Object invoker, Class<?>[] parameterClasses, Object... params) {
        Validate.isTrue(parameterClasses.length == params.length, "Parameter array\'s length must be equal to the params array's length");

        try {
            Class e = getCBCClass(className);
            Method m = e.getDeclaredMethod(method, parameterClasses);
            m.setAccessible(true);
            return (T) m.invoke(invoker, (Object[]) params);
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public static <T> T invokeBukkitMethod(String method, Object invoker, Class<?>[] parameterClasses, Object... params) {
        return invokeBukkitMethod(invoker.getClass().getName().replace("org.bukkit.craftbukkit." + getVersion() + ".", ""), method, invoker, parameterClasses, (Object[]) params);
    }

    public static <T> T invokeBukkitMethod(String method, Object invoker) {
        return invokeBukkitMethod(method, invoker, new Class[0], new Object[0]);
    }

    public static Object newBukkit(String className) {
        return newBukkit(className, new Class[0]);
    }

    public static Object newBukkit(String className, Class<?>[] parameterClasses, Object... params) {
        try {
            Class ex = getCBCClass(className);
            Constructor constructor = ex.getConstructor(parameterClasses);
            constructor.setAccessible(true);
            return constructor.newInstance(params);
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public static <T> T getBukkitField(Object owner, String fieldName) {
        return getBukkitField(owner.getClass().getName().replace("org.bukkit.craftbukkit." + getVersion() + ".", ""), owner, fieldName);
    }

    public static <T> T getBukkitField(String className, Object owner, String fieldName) {
        try {
            Class ex = getCBCClass(className);
            Field field = ex.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(owner);
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public static <T> T getBukkitStaticField(String className, String fieldName) {
        return getBukkitField(className, null, fieldName);
    }

    public static Object getEntityHandle(Entity entity) {
        return invokeMethod(getMethod(getCBCClass("entity.CraftEntity"), "getHandle"), entity);
    }

    public static Object getHandle(Entity entity) {
        try {
            if (handles.get(entity.getClass()) != null) {
                return handles.get(entity.getClass()).invoke(entity);
            }
            Method entity_getHandle = entity.getClass().getMethod("getHandle");
            handles.put(entity.getClass(), entity_getHandle);
            return entity_getHandle.invoke(entity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static <T> T getPrivateField(String fieldName, Class clazz, Object object) {
        Field field;
        T o = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = (T) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return o;
    }

    public Object getPrivateStatic(Class<?> clazz, String f) throws Exception {
        Field field = clazz.getDeclaredField(f);
        field.setAccessible(true);
        return field.get(null);
    }

    private static IGlow glow = null;
    private static IActionMessage actionMessage = null;
    private static ITitleMessage titleMessage = null;
    private static IPathfinderGoal pathfinderGoal = null;
    private static IClearGoals iClearGoals = null;
    private static Constructor<?> tellraw = null;
    private static Constructor<?> iAnvil = null;
    private static Constructor<?> iTileSkull = null;
    private static ITabMessage tabMessage = null;
    private static IParticlePacket particlePacket = null;
    private static IParticleSender particleSender = null;
    private static IUpdateSign iUpdateSign = null;

    public static void init() {
        glow = null;
        pathfinderGoal = null;
        iClearGoals = null;
        tellraw = null;
        iAnvil = null;
        iTileSkull = null;
        tabMessage = null;
        particlePacket = null;

        List<Class<?>> classes = new ArrayList<>();

        for (String name : Arrays.asList(
                "GlowMaker",
                "ActionMessage",
                "TitleMessage",
                "PathGoal",
                "ClearGoal",
                "FancyMessage",
                "TabMessage",
                "HandleAnvilGUI",
                "TileSkull",
                "ParticlePacket",
                "UpdateSign",
                "ParticleSender"
        )) {
            try {
                Class<?> clazz = Class.forName("simple.brainsynder.nms." + getVersion() + "." + name);
                classes.add(clazz);
            } catch (Exception ignored) {
            }
        }

        for (Class<?> clazz : classes) {
            try {

                if (IGlow.class.isAssignableFrom(clazz)) {
                    glow = (IGlow) clazz.getConstructor().newInstance();
                }
                if (IActionMessage.class.isAssignableFrom(clazz)) {
                    actionMessage = (IActionMessage) clazz.getConstructor().newInstance();
                }
                if (ITitleMessage.class.isAssignableFrom(clazz)) {
                    titleMessage = (ITitleMessage) clazz.getConstructor().newInstance();
                }
                if (IPathfinderGoal.class.isAssignableFrom(clazz)) {
                    pathfinderGoal = (IPathfinderGoal) clazz.getConstructor().newInstance();
                }
                if (IClearGoals.class.isAssignableFrom(clazz)) {
                    iClearGoals = (IClearGoals) clazz.getConstructor().newInstance();
                }
                if (TellrawMaker.class.isAssignableFrom(clazz)) {
                    tellraw = clazz.getConstructor(String.class);
                }
                if (IParticleSender.class.isAssignableFrom(clazz)) {
                    particleSender = (IParticleSender) clazz.getConstructor().newInstance();
                }
                if (IAnvilGUI.class.isAssignableFrom(clazz)) {
                    iAnvil = clazz.getConstructor(Plugin.class, Player.class, IAnvilClickEvent.class);
                }
                if (ITileEntitySkull.class.isAssignableFrom(clazz)) {
                    iTileSkull = clazz.getConstructor(Skull.class);
                }
                if (IParticlePacket.class.isAssignableFrom(clazz)) {
                    particlePacket = (IParticlePacket) clazz.getConstructor().newInstance();
                }
                if (ITabMessage.class.isAssignableFrom(clazz)) {
                    tabMessage = (ITabMessage) clazz.getConstructor().newInstance();
                }
                if (IUpdateSign.class.isAssignableFrom(clazz)) {
                    iUpdateSign = (IUpdateSign) clazz.getConstructor().newInstance();
                }
            } catch (Exception e) {
            }
        }


        if (titleMessage == null) titleMessage = new TitleMaker();
        if (actionMessage == null) actionMessage = new ActionMaker();
        if (particleSender == null) particleSender = new ParticleSender();
        if (iUpdateSign == null) iUpdateSign = new DefaultSignUpdater();
    }

    public static IParticleSender getParticleSender() {
        return particleSender;
    }

    public static IUpdateSign getUpdateSign() {
        return iUpdateSign;
    }

    public static ITellraw getTellraw(String s) {
        if (tellraw == null)
            return new TellrawMaker(s);
        try {
            return (TellrawMaker) tellraw.newInstance(s);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("An error occurred while trying to get the ITellraw instance, sending tellraw command.");
            return new TellrawMaker(s);
        }
    }

    public static IAnvilGUI getAnvilMaker(Plugin plugin, Player player, IAnvilClickEvent event) {
        if (iAnvil == null)
            return null;
        try {
            return (IAnvilGUI) iAnvil.newInstance(plugin, player, event);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("An error occurred while trying to get the IAnvilGUI instance, returning null.");
            return null;
        }
    }

    public static ITileEntitySkull getTileSkull(Skull skull) {
        if (iTileSkull == null)
            return null;
        try {
            return (ITileEntitySkull) iTileSkull.newInstance(skull);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("An error occurred while trying to get the ITileSkull instance, returning null.");
            return null;
        }
    }

    public static IParticlePacket getParticlePacket() {
        return particlePacket;
    }

    public static ITabMessage getTabMessage() {
        return tabMessage;
    }

    public static IClearGoals getClearGoals() {
        return iClearGoals;
    }

    public static IPathfinderGoal getPathfinderGoal() {
        return pathfinderGoal;
    }

    public static IGlow glowManager() {
        return glow;
    }

    public static IActionMessage getActionMessage() {
        return actionMessage;
    }

    public static ITitleMessage getTitleMessage() {
        return titleMessage;
    }

    public static Class<?> getNmsClass(String name) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName("net.minecraft.server." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

    public static boolean isVersion(String version) {
        return getVersion().equals(version);
    }

    public static <T> FieldAccessor<T> getField(Class<?> target, String name, Class<T> fieldType) {
        return getField(target, name, fieldType, 0);
    }

    public static Object getFieldValue(Field field, Object instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static void sendPacket(Object packet, Player player) {
        try {
            Object entityHandle = getEntityHandle(player);
            Object playerConnection = getField(entityHandle.getClass(), "playerConnection").get(entityHandle);
            invoke(getMethod(getNmsClass("Packet"), "sendPacket", packet.getClass()), playerConnection);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Object invoke(Method method, Object instance, Object... parameters) {
        if (method == null) {
            return null;
        } else {
            try {
                return method.invoke(instance, parameters);
            } catch (InvocationTargetException | IllegalAccessException var4) {
                var4.printStackTrace();
                return null;
            }
        }
    }

    private static <T> FieldAccessor<T> getField(Class<?> target, String name, Class<T> fieldType, int index) {
        while (true) {
            for (final Field field : target.getDeclaredFields()) {
                if (((name == null) || (field.getName().equals(name))) && (fieldType.isAssignableFrom(field.getType())) && (index-- <= 0)) {
                    field.setAccessible(true);

                    return new FieldAccessor() {
                        public T get(Object target) {
                            try {
                                return (T) field.get(target);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException("Cannot access reflection.", e);
                            }
                        }

                        public void set(Object target, Object value) {
                            try {
                                field.set(target, value);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException("Cannot access reflection.", e);
                            }
                        }

                        public boolean hasField(Object target) {
                            return field.getDeclaringClass().isAssignableFrom(target.getClass());
                        }
                    };
                }

            }

            if (target.getSuperclass() != null) {
                target = target.getSuperclass();
                continue;
            }
            throw new IllegalArgumentException("Cannot find field with type " + fieldType);
        }
    }

    public interface FieldAccessor<T> {
        T get(Object paramObject);

        void set(Object paramObject1, Object paramObject2);

        boolean hasField(Object paramObject);
    }

    public static <T> T getWorldHandle(World world) {
        return invokeMethod(getMethod(getCBCClass("CraftWorld"), "getHandle"), world);
    }

    public static Field getField(Class clazz, String name) {
        try {
            return setFieldAccessible(clazz.getDeclaredField(name));
        } catch (Throwable var3) {
            return null;
        }
    }

    public static Constructor getConstructor(Class cl, Class... classes) {
        try {
            Constructor var8 = cl.getDeclaredConstructor(classes);
            var8.setAccessible(true);
            return var8;
        } catch (Throwable var7) {
            StringBuilder sb = new StringBuilder();
            Class[] var3 = classes;
            int var4 = classes.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Class c = var3[var5];
                sb.append(", ").append(c.getName());
            }

            return null;
        }
    }

    public static Field getFirstFieldOfType(Class cl, Class returnType, String... matches) {
        Field[] var3 = cl.getDeclaredFields();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Field f = var3[var5];
            Type[] types;
            if (f.getType() == returnType && f.getGenericType() instanceof ParameterizedType && matches.length == (types = ((ParameterizedType) f.getGenericType()).getActualTypeArguments()).length) {
                boolean match = true;

                for (int i = 0; i < matches.length; ++i) {
                    if (!((Class) types[i]).getName().matches(matches[i])) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    return setFieldAccessible(f);
                }
            }
        }

        return null;
    }

    public static Field setFieldAccessible(Field f) {
        try {
            f.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            int modifiers = modifiersField.getInt(f);
            modifiersField.setInt(f, modifiers & -17);
            return f;
        } catch (Throwable var3) {
            return null;
        }
    }


    public static Class getCBCClass(String className) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        try {
            return clazz.getDeclaredMethod(methodName, params);
        } catch (NoSuchMethodException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static <T> T invokeMethod(Method method, Object instance, Object... args) {
        try {
            return (T) method.invoke(instance, args);
        } catch (IllegalAccessException var4) {
            return null;
        } catch (InvocationTargetException var5) {
            var5.printStackTrace();
            return null;
        }
    }

    /**
     * Translates the name to a {@link org.bukkit.Material}
     *
     * @param name The new/old {@link org.bukkit.Material} name
     * @return
     */
    public static Material findMaterial(String name) {
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

        return Material.AIR;

    }

    public static Material fetchMaterial(String... names) {
        for (String name : names) {
            try {
                return Material.valueOf(name);
            } catch (Exception ignored) {
            }
        }
        return Material.AIR;
    }
}
