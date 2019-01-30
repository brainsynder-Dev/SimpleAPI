package simple.brainsynder.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class SkullTextureEvent extends SimpleApiEvent {
    @Getter @Setter private String url;
    @Getter private Player player;

    public SkullTextureEvent (Player player, String url) {
        this.url = url;
        this.player = player;
    }
}
