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
import psettings.minestom.Utilities.UtilEffects;
import psettings.minestom.enums.SendSound;

public class VisibilityCommand implements Listener {

    private PSettings plugin;
    private Util util;
    private UtilEffects utilEffects;
    private MessageManager messageManager;

    public VisibilityCommand(PSettings plugin, Util util, UtilEffects utilEffects, MessageManager messageManager) {
        this.plugin = plugin;
        this.util = util;
        this.utilEffects = utilEffects;
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
        String customCommand = config.getString("CustomCommands.Options.Visibility.Command");

        if (customCommand.equalsIgnoreCase("none")) {
            return;
        }
        if (command.equalsIgnoreCase(customCommand)) {
            if (!event.isCancelled()) {
                event.setCancelled(true);
            }
            if (!config.getBoolean("CustomCommands.Options.Visibility.Default") && !player.hasPermission(Permissions.VISIBILITY_COMMAND)) {
                MessageUtil.message(player, messageManager.getString("CustomCommands.NoPermission", player));
                util.sendSound(player, SendSound.DENY);
                return;
            }
            SettingsManager settings = util.getSettingsManager(player);

            if (!settings.isVisibilityEnabled()) {
                settings.setVisibilityEnabled(true);
                utilEffects.showPlayers(player);
                if (messageManager.getBoolean("CustomCommands.Enabled", player)) {
                    MessageUtil.message(player, messageManager.getString("CustomCommands.Options.Visibility.Enabled", player));
                } else {
                    MessageUtil.message(player, messageManager.getString("MainMenu.Visibility.Enabled", player));
                }
            } else {
                settings.setVisibilityEnabled(false);
                utilEffects.hidePlayers(player);
                if (messageManager.getBoolean("CustomCommands.Enabled", player)) {
                    MessageUtil.message(player, messageManager.getString("CustomCommands.Options.Visibility.Disabled", player));
                } else {
                    MessageUtil.message(player, messageManager.getString("MainMenu.Visibility.Disabled", player));
                }
            }
        }
    }
}
