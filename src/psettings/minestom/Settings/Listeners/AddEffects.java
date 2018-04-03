package psettings.minestom.Settings.Listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffectType;
import psettings.minestom.Managers.SettingsManager;
import psettings.minestom.PSettings;
import psettings.minestom.Utilities.PlayerItems;
import psettings.minestom.Utilities.Util;
import psettings.minestom.Utilities.UtilEffects;

public class AddEffects implements Listener {
    private PSettings plugin;
    private UtilEffects utilEffects;
    private PlayerItems playerItems;
    private Util util;

    public AddEffects(PSettings plugin, UtilEffects utilEffects, Util util, PlayerItems playerItems) {
        this.plugin = plugin;
        this.utilEffects = utilEffects;
        this.util = util;
        this.playerItems = playerItems;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        SettingsManager settingsManager = util.getSettingsManager(player);

        if (settingsManager.isSpeedEnabled()) player.removePotionEffect(PotionEffectType.SPEED);

        if (settingsManager.isJumpEnabled()) player.removePotionEffect(PotionEffectType.JUMP);
    }

    @EventHandler
    public void giveEffects(PlayerTeleportEvent event) {
        FileConfiguration config = plugin.getConfig();
        Player player = event.getPlayer();
        SettingsManager settingsManager = util.getSettingsManager(player);

        if (!player.isDead()) {
            if (config.getBoolean("Join-Item.Enabled")) {
                playerItems.getItems(player);
            }
            if (settingsManager.isSpeedEnabled()) utilEffects.giveSpeed(player);

            if (settingsManager.isFlyEnabled()) player.setAllowFlight(true);

            if (settingsManager.isJumpEnabled()) utilEffects.giveJump(player);

            if (!settingsManager.isVisibilityEnabled()) utilEffects.hidePlayers(player);
            else utilEffects.showPlayers(player);

            playerItems.playerToggleItem(player);
        }
    }
}
