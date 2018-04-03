package psettings.minestom.Settings.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;
import psettings.minestom.ConfigurationFIles.MenuFiles.MainMenuFile;
import psettings.minestom.Managers.MessageManager;
import psettings.minestom.Managers.SettingsManager;
import psettings.minestom.PSettings;
import psettings.minestom.Utilities.MessageUtil;
import psettings.minestom.Utilities.Util;

public class StackerListener implements Listener {

    private PSettings plugin;
    private MessageManager messageManager;
    private MainMenuFile mainMenuFile;
    private Util util;

    public StackerListener(PSettings plugin, MessageManager messageManager, MainMenuFile mainMenuFile, Util util) {
        this.plugin = plugin;
        this.messageManager = messageManager;
        this.mainMenuFile = mainMenuFile;
        this.util = util;
    }

    @EventHandler
    public void stackerSetting(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player) {
            Player player1 = event.getPlayer();
            Player player2 = (Player) event.getRightClicked();

            SettingsManager settings = util.getSettingsManager(player1);
            SettingsManager settings2 = util.getSettingsManager(player2);

            if (settings.isStackerEnabled() && settings2.isStackerEnabled()) {
                if (settings.isStacked()) return;

                util.sendStackedMessage(player1, plugin.getConfig().getInt("PlayerInHead.Period"), settings.isStacked(),
                        MessageUtil.color(messageManager.getString("Player.PlayerInHead", player1)));
                if (!player2.isInsideVehicle() && !player1.isInsideVehicle()) {
                    player1.setPassenger(player2);
                    if (plugin.getConfig().getBoolean("HidePlayerWhenStacked")) {
                        player1.hidePlayer(player2);
                        settings.setPlayerStacked(player2);
                    }
                }
            }
        }
    }

    @EventHandler
    public void stackerSetting2(PlayerToggleSneakEvent event) {
        Player player2 = event.getPlayer();
        SettingsManager settings = util.getSettingsManager(player2);
        Player player1 = settings.getPlayerStacked();

        if (player1 == null) return;

        if (plugin.getConfig().getBoolean("HidePlayerWhenStacked")) {
            player1.showPlayer(player2);
            settings.setPlayerStacked(null);
        }
    }

    @EventHandler
    public void stackerSetting3(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        SettingsManager settings = util.getSettingsManager(player);
        Player player2 = settings.getPlayerStacked();

        if (player2 == null) return;

        if (settings.isStacked() && plugin.getConfig().getBoolean("HidePlayerWhenStacked")) {
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                player2.setVelocity(new Vector(player.getEyeLocation().getDirection().getX(),
                        player.getEyeLocation().getDirection().getY(), player.getEyeLocation().getDirection().getZ())
                        .multiply(mainMenuFile.getConfiguration().getInt("MainMenu.Stacker.LaunchForce")));
                player.eject();
                player.showPlayer(player2);
                settings.setPlayerStacked(null);
            } else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                player.eject();
                player.showPlayer(player2);
                settings.setPlayerStacked(null);
            }
        }
    }
}
