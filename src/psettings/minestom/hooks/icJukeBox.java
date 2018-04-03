package psettings.minestom.hooks;

import com.statiocraft.jukebox.scJukeBox;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class icJukeBox {


    public boolean isJukeEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("icJukeBox");
    }

    public void addToRadio(Player player) {
        if (isJukeEnabled()) {
            scJukeBox.getRadio().addPlayer(player);
        }
    }

    public void removeFromRadio(Player player) {
        if (isJukeEnabled()) {
            scJukeBox.getRadio().removePlayer(player);
        }
    }

}
