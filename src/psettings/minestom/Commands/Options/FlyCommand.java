package psettings.minestom.Commands.Options;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import psettings.minestom.Managers.MessageManager;
import psettings.minestom.Managers.SettingsManager;
import psettings.minestom.PSettings;
import psettings.minestom.Utilities.MessageUtil;
import psettings.minestom.Utilities.Permissions;
import psettings.minestom.Utilities.Util;
import psettings.minestom.enums.SendSound;

public class FlyCommand implements Listener {

    private PSettings plugin;
    private Util util;
    private MessageManager messageManager;

    public FlyCommand(PSettings plugin, Util util, MessageManager messageManager) {
        this.plugin = plugin;
        this.util = util;
        this.messageManager = messageManager;
    }


    @EventHandler
    public void execute(PlayerCommandPreprocessEvent event) {
        FileConfiguration config = plugin.getConfig();
        if (event.isCancelled() && config.getBoolean("CustomCommands.Enabled")) {
            return;
        }
        Player player = event.getPlayer();
        String command = event.getMessage().replace("/", "");
        String customCommand = config.getString("CustomCommands.Options.Fly.Command");

        if (customCommand.equalsIgnoreCase("none")) {
            return;
        }
        if (command.equalsIgnoreCase(customCommand)) {
            if (!event.isCancelled()) {
                event.setCancelled(true);
            }
            if (!config.getBoolean("CustomCommands.Options.Fly.Default") && !player.hasPermission(Permissions.FLY_COMMAND)) {
                MessageUtil.message(player, messageManager.getString("CustomCommands.NoPermission", player));
                util.sendSound(player, SendSound.DENY);
                return;
            }
            SettingsManager settings = util.getSettingsManager(player);
            if (settings.isDoubleJumpEnabled()) {
                MessageUtil.message(player, messageManager.getString("MainMenu.Fly.CantFly", player));
                util.sendSound(player, SendSound.NO_FLY);
                return;
            }
            if (!settings.isFlyEnabled()) {
                settings.setFlyEnabled(true);
                player.setAllowFlight(true);
                player.setFlying(true);
                if (messageManager.getBoolean("CustomCommands.Enabled", player)) {
                    MessageUtil.message(player, messageManager.getString("CustomCommands.Options.Fly.Enabled", player));
                } else {
                    MessageUtil.message(player, messageManager.getString("MainMenu.Fly.Enabled", player));
                }
            } else {
                settings.setFlyEnabled(false);
                player.setAllowFlight(false);
                player.setFlying(false);
                if (messageManager.getBoolean("CustomCommands.Enabled", player)) {
                    MessageUtil.message(player, messageManager.getString("CustomCommands.Options.Fly.Disabled", player));
                } else {
                    MessageUtil.message(player, messageManager.getString("MainMenu.Fly.Disabled", player));
                }
            }
        }
    }
}
