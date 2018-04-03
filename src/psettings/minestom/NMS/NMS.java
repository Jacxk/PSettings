package psettings.minestom.NMS;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import psettings.minestom.Utilities.Util;

public interface NMS {

    void sendActionbar(Player player, String message);

    void sendTitle(Player player, String title, int fadeIn, int stay, int fadeOut);

    void sendSubtitle(Player player, String subtitle, int fadeIn, int stay, int fadeOut);

    void spawnParticle(Player player, String particle, Location location);

    ItemStack book(String old, String updated, int id, Player player);

    void openBook(String actualVersion, String newVersion, int id, Player player);

}
