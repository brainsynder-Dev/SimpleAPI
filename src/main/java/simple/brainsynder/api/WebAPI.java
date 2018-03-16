package simple.brainsynder.api;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import simple.brainsynder.storage.ExpireHashMap;
import simple.brainsynder.utils.AdvMap;
import simple.brainsynder.utils.Base64Wrapper;
import simple.brainsynder.utils.Reflection;
import simple.brainsynder.utils.WebConnector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * This class is used for connecting to my websiteAPI that makes it easier to get the data from the MojangAPI
 */
public class WebAPI {
    private static AdvMap<String, ExpireHashMap<Type, String>> cache = new AdvMap<>();
    private static String steveTexture = Base64Wrapper.encodeString("{'textures':{'SKIN':{'url':'http://textures.minecraft.net/texture/456eec1c2169c8c60a7ae436abcd2dc5417d56f8adef84f11343dc1188fe138'}}}");
    
    /**
     * @param type
     *         Search type
     * @param playerName
     *         Name of the player you are getting the data for
     *
     * @return Will return null of the data could not be retrieved
     */
    @SneakyThrows
    public static String getData(@NonNull Type type, @NonNull String playerName) {
        if (cache.containsKey(playerName)) {
            ExpireHashMap<Type, String> cacheData = cache.getKey(playerName);
            if (cacheData.containsKey(type)) {
                return cacheData.get(type);
            } else {
                System.out.println("[SimpleAPI Notice] Loading the " + type.getType() + " of " + playerName + " for the first time... Might Lag a little from adding the data to a Cache...");
                String link = "https://v2.minecraftchar.us/" + type.getType() + "/?user=" + playerName;
                String textureURL = steveTexture;
                if (type == Type.SKIN_URL) {
                    String texture = getTexture(playerName);
                    if (!texture.equals(steveTexture))
                        cacheData.put(type, texture, 1, TimeUnit.HOURS);
                } else if (type == Type.CURRENT_NAME) {
                    String name = getUsername(link, playerName);
                    if (!name.equals("Steve"))
                    cacheData.put(type, name, 24, TimeUnit.HOURS);
                } else {
                    String history = getHistory(link, playerName);
                    if (!history.contains("Steve"))
                    cacheData.put(type, history, 24, TimeUnit.HOURS);
                }
                cache.put(playerName, cacheData);
                return cacheData.get(type);
            }
        } else {
            System.out.println("[SimpleAPI Notice] Loading the " + type.getType() + " of " + playerName + " for the first time... Might Lag a little from adding the data to a Cache...");
            ExpireHashMap<Type, String> cacheData = new ExpireHashMap<>();
            if (cacheData.containsKey(type)) {
                return cacheData.get(type);
            } else {
                String link = "https://v2.minecraftchar.us/" + type.getType() + "/?user=" + playerName;
                String textureURL = steveTexture;
                if (type == Type.SKIN_URL) {
                    String texture = getTexture(playerName);
                    if (!texture.equals(steveTexture))
                    cacheData.put(type, texture, 1, TimeUnit.HOURS);
                } else if (type == Type.CURRENT_NAME) {
                    String name = getUsername(link, playerName);
                    if (!name.equals("Steve"))
                        cacheData.put(type, name, 24, TimeUnit.HOURS);
                } else {
                    String history = getHistory(link, playerName);
                    if (!history.contains("Steve"))
                        cacheData.put(type, history, 24, TimeUnit.HOURS);
                }
                cache.put(playerName, cacheData);
                return cacheData.get(type);
            }
        }
    }
    
    private static String getTextureVIAGameProfile(Player player) {
        try {
            Class<?> strClass = Class.forName("org.bukkit.craftbukkit." + Reflection.getVersion() + ".entity.CraftPlayer");
            GameProfile profile = (GameProfile) strClass.cast(player).getClass().getMethod("getProfile").invoke(strClass.cast(player));
            Collection<Property> textures = profile.getProperties().get("textures");
            String text = "";
            for (Property texture : textures) {
                text = texture.getValue();
                break;
            }
            String decoded = Base64Wrapper.decodeString(text);
            JSONObject texture = (JSONObject) JSONValue.parseWithException(decoded);
            JSONObject SKIN = (JSONObject) texture.get("textures");
            JSONObject dlurl = (JSONObject) SKIN.get("SKIN");
            String result = String.valueOf(dlurl.get("url"));
            return Base64Wrapper.encodeString("{\"textures\":{\"SKIN\":{\"url\":\"" + result + "\"}}}");
        } catch (Exception e) {
            System.out.println("SimpleAPI >> It seems that " + player.getName() + " does not have a skin saved/setup. Using default Steve skin...");
            return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6IiJ9fX0=";
        }
    }
    
    private static String getTexture(String playerName) {
        String textureURL = steveTexture;
        Player isOnline = Bukkit.getPlayerExact(playerName);
        if (isOnline != null) {
            textureURL = getTextureVIAGameProfile(isOnline);
        } else {
            try {
                InputStream inputStream = WebConnector.getInputStream("https://v2.minecraftchar.us/textureurl/?user=" + playerName + "&form=true").get();
                JSONObject main = (JSONObject) JSONValue.parseWithException(IOUtils.toString(inputStream));
                if (!main.isEmpty()) {
                    JSONObject texture = (JSONObject) main.get("textures");
                    JSONObject skin = (JSONObject) texture.get("SKIN");
                    String link = String.valueOf(skin.get("url"));
                    if (link == null) {
                        link = "http://textures.minecraft.net/texture/456eec1c2169c8c60a7ae436abcd2dc5417d56f8adef84f11343dc1188fe138";
                    }
                    textureURL = Base64Wrapper.encodeString("{'textures':{'SKIN':{'url':'%url%'}}}".replace("%url%", link));
                }
            } catch (Exception ignored) {
            }
        }
        if (textureURL.equals("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6IiJ9fX0="))
            textureURL = steveTexture;
        if (textureURL.equals("eyd0ZXh0dXJlcyc6eydTS0lOJzp7J3VybCc6Jyd9fX0="))
            textureURL = steveTexture;
        
        return textureURL;
    }
    
    private static String getUsername(String link, String playerName) {
        String out = "Steve";
        Player isOnline = Bukkit.getPlayerExact(playerName);
        if (isOnline != null) {
            return playerName;
        } else {
            try {

                InputStream inputStream = WebConnector.getInputStream(link).get();
                InputStreamReader input = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(input);
                String currentLine = reader.readLine();
                if (currentLine != null) {
                    if (!currentLine.startsWith("{"))
                        out = currentLine;
                }
            } catch (Exception ignored) {
            }
        }
        return out;
    }
    
    private static String getHistory(String link, String playerName) {
        String out = "Steve";
        StringBuilder builder = new StringBuilder();
        try {
            InputStream inputStream = WebConnector.getInputStream(link).get();
            JSONObject main = (JSONObject) JSONValue.parseWithException(IOUtils.toString(inputStream));
            if (!main.isEmpty()) {
                if (main.containsKey("History")) {
                    JSONArray history = (JSONArray) main.get("History");
                    for (Object obj : history) {
                        JSONObject value = (JSONObject) obj;
                        if (value.containsKey("username")) builder.append(value.get("username"));
                        if (value.containsKey("First Username")) {
                            builder.append("  |  (First Username)");
                        }else{
                            if (value.containsKey("changedOn"))
                                builder.append("  |  ").append(String.valueOf(value.get("changedOn")).replace("-", "/"));
                        }
                        builder.append('\n');
                    }
                    out = builder.toString();
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return out;
    }
    
    public static JSONObject getProfile(String playerName) {
        JSONObject out = new JSONObject();
        StringBuilder builder = new StringBuilder();
        try {
            InputStream inputStream = WebConnector.getInputStream("https://v2.minecraftchar.us/profile/?user=" + playerName).get();
            JSONObject main = (JSONObject) JSONValue.parseWithException(IOUtils.toString(inputStream));
            if (!main.isEmpty()) {
                out = main;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return out;
    }
    
    
    /**
     * The type of search you are doing
     */
    public enum Type {
        SKIN_URL("textureurl"),
        CURRENT_NAME("recent"),
        PREVIOUS_NAMES("history"),
        PROFILE_INFO("profile");
        
        private String type;
        
        Type(String type) {
            this.type = type;
        }
        
        /**
         * @return String value of search type
         */
        public String getType() {
            return type;
        }
    }
}
