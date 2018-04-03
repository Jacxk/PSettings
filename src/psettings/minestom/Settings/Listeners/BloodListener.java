package psettings.minestom.Settings.Listeners;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import psettings.minestom.Managers.SettingsManager;
import psettings.minestom.Utilities.Util;

public class BloodListener implements Listener {

    private Util util;

    public BloodListener(Util util) {
        this.util = util;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            SettingsManager settings = util.getSettingsManager(player);
            if (settings.isBloodEnabled()) {
                player.playEffect(event.getEntity().getLocation().add(0, 1, 0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
            }
        }
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            SettingsManager settings = util.getSettingsManager(player);
            if (settings.isBloodEnabled()) {
                player.playEffect(event.getEntity().getLocation().add(0, 1, 0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
            }
        }
    }
}
