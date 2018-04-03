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

public class DoubleJumpCommand implements Listener {

    private PSettings plugin;
    private Util util;
    private MessageManager messageManager;

    public DoubleJumpCommand(PSettings plugin, Util util, MessageManager messageManager) {
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
        String customCommand = config.getString("CustomCommands.Options.DoubleJump.Command");

        if (customCommand.equalsIgnoreCase("none")) {
            return;
        }
        if (command.equalsIgnoreCase(customCommand)) {
            if (!event.isCancelled()) {
                event.setCancelled(true);
            }
            if (!config.getBoolean("CustomCommands.Options.DoubleJump.Default") && !player.hasPermission(Permissions.DOUBLE_JUMP_COMMAND)) {
                MessageUtil.message(player, messageManager.getString("CustomCommands.NoPermission", player));
                util.sendSound(player, SendSound.DENY);
                return;
            }
            SettingsManager settings = util.getSettingsManager(player);
            if (settings.isFlyEnabled()) {
                MessageUtil.message(player, messageManager.getString("MainMenu.DoubleJump.CantDoubleJump", player));
                util.sendSound(player, SendSound.NO_DOUBLE_JUMP);
                return;
            }
            if (!settings.isDoubleJumpEnabled()) {
                settings.setDoubleJumpEnabled(true);
                player.setAllowFlight(true);
                if (messageManager.getBoolean("CustomCommands.Enabled", player)) {
                    MessageUtil.message(player, messageManager.getString("CustomCommands.Options.DoubleJump.Enabled", player));
                } else {
                    MessageUtil.message(player, messageManager.getString("MainMenu.DoubleJump.Enabled", player));
                }
            } else {
                settings.setDoubleJumpEnabled(false);
                player.setAllowFlight(false);
                if (messageManager.getBoolean("CustomCommands.Enabled", player)) {
                    MessageUtil.message(player, messageManager.getString("CustomCommands.Options.DoubleJump.Disabled", player));
                } else {
                    MessageUtil.message(player, messageManager.getString("MainMenu.DoubleJump.Disabled", player));
                }
            }
        }
    }
}
