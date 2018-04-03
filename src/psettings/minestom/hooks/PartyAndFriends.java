package psettings.minestom.hooks;

import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import psettings.minestom.PSettings;

public class PartyAndFriends {

    private PSettings plugin;

    public PartyAndFriends(PSettings plugin) {
        this.plugin = plugin;
    }

    public boolean isFriends(Player player) {
        if (isPAFEnabled() && plugin.getConfig().getBoolean("PartyAndFriends-Hook")) {
            PAFPlayerManager pafPlayerManager = PAFPlayerManager.getInstance();
            for (Player player1 : Bukkit.getOnlinePlayers()) {
                if (pafPlayerManager.getPlayer(player1.getUniqueId()).isAFriendOf(pafPlayerManager.getPlayer(player.getUniqueId()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isPAFEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("FriendsAPIForPartyAndFriends");
    }


}
