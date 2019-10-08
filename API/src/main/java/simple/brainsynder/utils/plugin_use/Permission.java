package simple.brainsynder.utils.plugin_use;

import org.bukkit.entity.Player;

public class Permission {

    private String perm;
    public Permission (String perm) {
        this.perm = perm;
    }

    public Validator getValidator () {
        return new Validator(perm);
    }

    public static class Validator{
        private String permission;
        public Validator (String permission) {
            this.permission = permission;
        }

        public boolean has (Player player) {
            return player.hasPermission(permission);
        }
    }
}
