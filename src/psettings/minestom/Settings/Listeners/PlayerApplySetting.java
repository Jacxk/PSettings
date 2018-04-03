package psettings.minestom.Settings.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import psettings.minestom.DataBase.MySQLManager;
import psettings.minestom.DataBase.PlayersCreation;
import psettings.minestom.Managers.MessageManager;
import psettings.minestom.Managers.SettingsManager;
import psettings.minestom.PSettings;
import psettings.minestom.Updater.Update;
import psettings.minestom.Utilities.PlayerItems;
import psettings.minestom.Utilities.Util;
import psettings.minestom.Utilities.UtilEffects;
import psettings.minestom.hooks.icJukeBox;

import java.util.UUID;

public class PlayerApplySetting implements Listener {

    private MySQLManager mySQLManager;
    private PlayersCreation playersCreation;
    private Update updater;
    private PSettings plugin;
    private PlayerItems playerItems;
    private UtilEffects utilEffects;
    private icJukeBox icJukeBox;
    private Util util;

    public PlayerApplySetting(MySQLManager mySQLManager, PlayersCreation playersCreation, Update updater, PSettings plugin,
                              PlayerItems playerItems, UtilEffects utilEffects,
                              psettings.minestom.hooks.icJukeBox icJukeBox, Util util) {
        this.mySQLManager = mySQLManager;
        this.playersCreation = playersCreation;
        this.updater = updater;
        this.plugin = plugin;
        this.playerItems = playerItems;
        this.utilEffects = utilEffects;
        this.icJukeBox = icJukeBox;
        this.util = util;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        SettingsManager settings = new SettingsManager();

        playersCreation.mainCreatePlayer(player);
        playersCreation.chatCreatePlayer(player);
        playersCreation.visCreatePlayer(player);
        playersCreation.pSelectCreatePlayer(player);
        playersCreation.otherSettingsCreatePlayer(player, util.getLocale(player));

        util.importData(settings, uuid);
        settings.setPlayerName(player.getName());
        util.addPlayerSetting(player, settings);

        if (plugin.getConfig().getBoolean("Join-Item.Enabled")) playerItems.getItems(player);
        addEffects(player);
        Bukkit.getScheduler().runTaskLater(plugin, () -> updater.sendUpdateMessage(player), 20L);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        SettingsManager settings = util.getSettingsManager(player);

        mySQLManager.updateSettings(uuid, settings);
    }

    private void addEffects(Player player) {
        SettingsManager settingsManager = util.getSettingsManager(player);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (settingsManager.isFlyEnabled()) {
                if (!player.getAllowFlight()) {
                    player.setAllowFlight(true);
                    if (!player.isFlying()) {
                        player.teleport(new Location(player.getWorld(), player.getLocation().getX(),
                                player.getLocation().getY() + 2, player.getLocation().getZ(),
                                player.getLocation().getYaw(), player.getLocation().getPitch()));
                        player.setFlying(true);
                    }
                }
            }
            if (settingsManager.isDoubleJumpEnabled() && player.isFlying()) player.setFlying(false);

            if (settingsManager.isSpeedEnabled()) utilEffects.giveSpeed(player);

            if (settingsManager.isJumpEnabled()) utilEffects.giveJump(player);

            if (settingsManager.isVisibilityEnabled()) utilEffects.showPlayers(player);
            else utilEffects.hidePlayers(player);

            playerItems.playerToggleItem(player);

            if (settingsManager.isRadioEnabled()) icJukeBox.addToRadio(player);
            else icJukeBox.removeFromRadio(player);

        }, 5L);
    }
}
