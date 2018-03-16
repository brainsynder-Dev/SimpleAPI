package simple.brainsynder.nbt;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class ItemNBT {
    private ItemStack bukkitItem;

    private ItemNBT(ItemStack item) {
        bukkitItem = item.clone();
    }

    public static ItemNBT getItemNBT(ItemStack item) {
        return new ItemNBT(item);
    }

    public void setString(String key, String value) {
        bukkitItem = ItemNBTReflection.setString(bukkitItem, key, value);
    }

    public String getString(String key) {
        return ItemNBTReflection.getString(bukkitItem, key);
    }

    public void setInteger(String key, int value) {
        bukkitItem = ItemNBTReflection.setInt(bukkitItem, key, value);
    }

    public Integer getInteger(String key) {
        return ItemNBTReflection.getInt(bukkitItem, key);
    }

    public void setDouble(String key, double value) {
        bukkitItem = ItemNBTReflection.setDouble(bukkitItem, key, value);
    }

    public double getDouble(String key) {
        return ItemNBTReflection.getDouble(bukkitItem, key);
    }

    public void setBoolean(String key, boolean value) {
        bukkitItem = ItemNBTReflection.setBoolean(bukkitItem, key, value);
    }

    public boolean getBoolean(String key) {
        return ItemNBTReflection.getBoolean(bukkitItem, key);
    }

    public boolean hasKey(String key) {
        return ItemNBTReflection.hasKey(bukkitItem, key);
    }

    public void removeKey(String key) {
        bukkitItem = ItemNBTReflection.remove(bukkitItem, key);
    }

    public Set<String> getKeys() {
        return ItemNBTReflection.getKeys(bukkitItem);
    }

    public ItemStack getBukkitItem() {
        return this.bukkitItem;
    }

    private static class ItemNBTReflection {
        @SneakyThrows
        private static Class getCraftItemStack() {
            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
            Class c = Class.forName("org.bukkit.craftbukkit." + version + ".inventory.CraftItemStack");
            return c;
        }

        @SneakyThrows
        private static Object getNBTTagCompound(Object nmsitem) {
            Class c = nmsitem.getClass();
            java.lang.reflect.Method method = c.getMethod("getTag");
            Object answer = method.invoke(nmsitem);
            return answer;
        }

        @SneakyThrows
        private static Object getNMSItemStack(ItemStack item) {
            Class cis = getCraftItemStack();
            java.lang.reflect.Method method = cis.getMethod("asNMSCopy", ItemStack.class);
            Object answer = method.invoke(cis, item);
            return answer;
        }

        @SneakyThrows
        private static ItemStack getBukkitItemStack(Object item) {
            Class cis = getCraftItemStack();
            java.lang.reflect.Method method = cis.getMethod("asCraftMirror", item.getClass());
            Object answer = method.invoke(cis, item);
            return (ItemStack) answer;
        }


        @SneakyThrows
        public static ItemStack setString(ItemStack item, String key, String text) {
            if (text == null) return remove(item, key);
            Object nmsitem = getNMSItemStack(item);
            if (nmsitem == null) {
                return null;
            }
            Object nbttag = getNBTTagCompound(nmsitem);
            if (nbttag == null) {
                nbttag = NBTReflection.getNewNBTTag();
            }
            java.lang.reflect.Method method = nbttag.getClass().getMethod("setString", String.class, String.class);
            method.invoke(nbttag, key, text);
            nmsitem = NBTReflection.setNBTTag(nbttag, nmsitem);
            return getBukkitItemStack(nmsitem);
        }


        @SneakyThrows
        public static ItemStack setInt(ItemStack item, String key, Integer i) {
            if (i == null) return remove(item, key);
            Object nmsitem = getNMSItemStack(item);
            if (nmsitem == null) {
                return null;
            }
            Object nbttag = getNBTTagCompound(nmsitem);
            if (nbttag == null) {
                nbttag = NBTReflection.getNewNBTTag();
            }
            java.lang.reflect.Method method = nbttag.getClass().getMethod("setInt", String.class, int.class);
            method.invoke(nbttag, key, i);
            nmsitem = NBTReflection.setNBTTag(nbttag, nmsitem);
            return getBukkitItemStack(nmsitem);
        }


        @SneakyThrows
        public static ItemStack setDouble(ItemStack item, String key, Double d) {
            if (d == null) return remove(item, key);
            Object nmsitem = getNMSItemStack(item);
            if (nmsitem == null) {
                return null;
            }
            Object nbttag = getNBTTagCompound(nmsitem);
            if (nbttag == null) {
                nbttag = NBTReflection.getNewNBTTag();
            }
            java.lang.reflect.Method method = nbttag.getClass().getMethod("setDouble", String.class, double.class);
            method.invoke(nbttag, key, d);
            nmsitem = NBTReflection.setNBTTag(nbttag, nmsitem);
            return getBukkitItemStack(nmsitem);
        }


        @SneakyThrows
        public static ItemStack setBoolean(ItemStack item, String key, Boolean d) {
            if (d == null) return remove(item, key);
            Object nmsitem = getNMSItemStack(item);
            if (nmsitem == null) {
                return null;
            }
            Object nbttag = getNBTTagCompound(nmsitem);
            if (nbttag == null) {
                nbttag = NBTReflection.getNewNBTTag();
            }
            java.lang.reflect.Method method = nbttag.getClass().getMethod("setBoolean", String.class, boolean.class);
            method.invoke(nbttag, key, d);
            nmsitem = NBTReflection.setNBTTag(nbttag, nmsitem);
            return getBukkitItemStack(nmsitem);
        }


        @SneakyThrows
        public static ItemStack remove(ItemStack item, String key) {
            Object nmsitem = getNMSItemStack(item);
            if (nmsitem == null) {
                return null;
            }
            Object nbttag = getNBTTagCompound(nmsitem);
            if (nbttag == null) {
                nbttag = NBTReflection.getNewNBTTag();
            }
            java.lang.reflect.Method method = nbttag.getClass().getMethod("remove", String.class);
            method.invoke(nbttag, key);
            nmsitem = NBTReflection.setNBTTag(nbttag, nmsitem);
            return getBukkitItemStack(nmsitem);
        }


        @SneakyThrows
        public static String getString(ItemStack item, String key) {
            Object nmsitem = getNMSItemStack(item);
            if (nmsitem == null) {
                return null;
            }
            Object nbttag = getNBTTagCompound(nmsitem);
            if (nbttag == null) {
                return null;
            }
            java.lang.reflect.Method method = nbttag.getClass().getMethod("getString", String.class);
            return (String) method.invoke(nbttag, key);
        }


        @SneakyThrows
        public static Integer getInt(ItemStack item, String key) {
            Object nmsitem = getNMSItemStack(item);
            if (nmsitem == null) {
                return null;
            }
            Object nbttag = getNBTTagCompound(nmsitem);
            if (nbttag == null) {
                return null;
            }
            java.lang.reflect.Method method = nbttag.getClass().getMethod("getInt", String.class);
            return (Integer) method.invoke(nbttag, key);
        }


        @SneakyThrows
        public static Double getDouble(ItemStack item, String key) {
            Object nmsitem = getNMSItemStack(item);
            if (nmsitem == null) {
                return null;
            }
            Object nbttag = getNBTTagCompound(nmsitem);
            if (nbttag == null) {
                return null;
            }
            java.lang.reflect.Method method = nbttag.getClass().getMethod("getDouble", String.class);
            return (Double) method.invoke(nbttag, key);
        }


        @SneakyThrows
        public static Boolean getBoolean(ItemStack item, String key) {
            Object nmsitem = getNMSItemStack(item);
            if (nmsitem == null) {
                return null;
            }
            Object nbttag = getNBTTagCompound(nmsitem);
            if (nbttag == null) {
                return false;
            }
            java.lang.reflect.Method method = nbttag.getClass().getMethod("getBoolean", String.class);
            return (Boolean) method.invoke(nbttag, key);
        }


        @SneakyThrows
        public static boolean hasKey(ItemStack item, String key) {
            Object nmsitem = getNMSItemStack(item);
            if (nmsitem == null) {
                return false;
            }
            Object nbttag = getNBTTagCompound(nmsitem);
            if (nbttag == null) {
                return false;
            }
            java.lang.reflect.Method method = nbttag.getClass().getMethod("hasKey", String.class);
            return (boolean) method.invoke(nbttag, key);
        }


        @SneakyThrows
        public static Set<String> getKeys(ItemStack item) {
            Object nmsitem = getNMSItemStack(item);
            if (nmsitem == null) {
                return null;
            }
            Object nbttag = getNBTTagCompound(nmsitem);
            if (nbttag == null) {
                nbttag = NBTReflection.getNewNBTTag();
            }
            java.lang.reflect.Method method = nbttag.getClass().getMethod("c");
            return (Set<String>) method.invoke(nbttag);
        }
    }
}
