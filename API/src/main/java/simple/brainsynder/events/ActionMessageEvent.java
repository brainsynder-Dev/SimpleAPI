package simple.brainsynder.events;

import org.bukkit.entity.Player;

public class ActionMessageEvent extends SimpleApiEvent {
    private Player target;
    private String text;
    public ActionMessageEvent (Player target, String message) {
        this.target = target;
        this.text = message;
    }

    public Player getTarget() {
        return target;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
