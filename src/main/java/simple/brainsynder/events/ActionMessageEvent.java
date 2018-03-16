package simple.brainsynder.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class ActionMessageEvent extends SimpleApiEvent {
    @Getter private Player target;
    @Getter @Setter private String text;
    public ActionMessageEvent (Player target, String message) {
        this.target = target;
        this.text = message;
    }
}
