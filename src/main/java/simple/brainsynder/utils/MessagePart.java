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

import com.google.gson.stream.JsonWriter;
import org.bukkit.ChatColor;

public final class MessagePart {
    public ChatColor color = null;
    public ChatColor[] styles = null;
    public String clickActionName = null;
    public String clickActionData = null;
    public String hoverActionName = null;
    public String hoverActionData = null;
    public final String text;

    public MessagePart(String text) {
        this.text = text;
    }

    public JsonWriter writeJson(JsonWriter json) {
        try {
            json.beginObject().name("text").value(this.text);
            if (this.color != null) {
                json.name("color").value(this.color.name().toLowerCase());
            }
            if (this.styles != null) {
                for (ChatColor style : this.styles) {
                    json.name(style.name().toLowerCase()).value(true);
                }
            }
            if ((this.clickActionName != null) && (this.clickActionData != null)) {
                json.name("clickEvent").beginObject().name("action").value(this.clickActionName).name("value").value(this.clickActionData).endObject();
            }

            if ((this.hoverActionName != null) && (this.hoverActionData != null)) {
                json.name("hoverEvent").beginObject().name("action").value(this.hoverActionName).name("value").value(this.hoverActionData).endObject();
            }

            return json.endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}