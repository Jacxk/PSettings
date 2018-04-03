package psettings.minestom.Settings.Listeners;

import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import psettings.minestom.ConfigurationFIles.MenuFiles.MainMenuFile;
import psettings.minestom.Managers.CooldownManager;
import psettings.minestom.Managers.MessageManager;
import psettings.minestom.Managers.SettingsManager;
import psettings.minestom.Utilities.MessageUtil;
import psettings.minestom.Utilities.Permissions;
import psettings.minestom.Utilities.Util;
import psettings.minestom.enums.SendSound;

import java.util.UUID;

public class DoubleJumpListener implements Listener {
    private MessageManager messageManager;
    private MainMenuFile menu;
    private Util util;

    public DoubleJumpListener(MessageManager messageManager, MainMenuFile menu, Util util) {
        this.messageManager = messageManager;
        this.menu = menu;
        this.util = util;
    }

    @EventHandler
    public void doubleJump(PlayerToggleFlightEvent event) {
        FileConfiguration menus = menu.getConfiguration();
        Player player = event.getPlayer();
        SettingsManager settings = util.getSettingsManager(player);
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR
                || player.isFlying()) return;

        if (player.isInsideVehicle()) {
            event.setCancelled(true);
            player.setFlying(false);
            player.setAllowFlight(false);
            return;
        }
        UUID uuid = player.getUniqueId();
        int cooldowntime = menus.getInt("MainMenu.DoubleJump.Cooldown");
        if (!settings.isFlyEnabled() && settings.isDoubleJumpEnabled()) {
            event.setCancelled(true);
            player.setFlying(false);
            if (!CooldownManager.isInCooldown(uuid, "doublejump")) {
                CooldownManager cooldownManager = new CooldownManager(uuid, "doublejump", cooldowntime);
                if (!player.hasPermission(Permissions.ADMIN) || !player.hasPermission(Permissions.DOUBLEJUMP_COOLDOWN_BYPASS))
                    cooldownManager.start();
            } else {
                MessageUtil.message(player, messageManager.getString("MainMenu.DoubleJump.Cooldown", player)
                        .replace("{seconds}", CooldownManager.getTimeLeft(uuid, "doublejump") + ""));
                return;
            }
            player.setVelocity(player.getLocation().getDirection().multiply(menus.getLong("MainMenu.DoubleJump.Force")).setY(1));
            util.sendSound(player, SendSound.ON_DOUBLE_JUMP);
            player.setAllowFlight(false);
            util.playParticle(player);
        }
    }

    @EventHandler
    public void doubleJump2(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        SettingsManager settings = util.getSettingsManager(player);
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;

        if (!settings.isFlyEnabled())
            if (settings.isDoubleJumpEnabled() && player.isOnGround() && !player.isFlying())
                player.setAllowFlight(true);
    }

    @EventHandler
    public void doubleJump3(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        SettingsManager settings = util.getSettingsManager(player);
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL)
            if (settings.isDoubleJumpEnabled() && !event.isCancelled())
                event.setCancelled(true);
    }
}
