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

public class ChatCommand implements Listener {

    private PSettings plugin;
    private Util util;
    private MessageManager messageManager;

    public ChatCommand(PSettings plugin, Util util, MessageManager messageManager) {
        this.plugin = plugin;
        this.util = util;
        this.messageManager = messageManager;
    }


    @EventHandler
    public void execute(PlayerCommandPreprocessEvent event) {
        FileConfiguration config = plugin.getConfig();

        if (event.isCancelled() || !config.getBoolean("CustomCommands.Enabled")) return;

        Player player = event.getPlayer();
        String command = event.getMessage().replace("/", "");
        String customCommand = config.getString("CustomCommands.Options.Chat.Command");

        if (customCommand.equalsIgnoreCase("none")) return;

        if (command.equalsIgnoreCase(customCommand)) {
            if (!event.isCancelled()) {
                event.setCancelled(true);
            }
            if (!config.getBoolean("CustomCommands.Options.Chat.Default") && !player.hasPermission(Permissions.CHAT_COMMAND)) {
                MessageUtil.message(player, messageManager.getString("CustomCommands.NoPermission", player));
                util.sendSound(player, SendSound.DENY);
                return;
            }
            SettingsManager settings = util.getSettingsManager(player);
            if (!settings.isChatEnabled()) {
                settings.setChatEnabled(true);
                if (messageManager.getBoolean("CustomCommands.Enabled", player)) {
                    MessageUtil.message(player, messageManager.getString("CustomCommands.Options.Chat.Enabled", player));
                } else {
                    MessageUtil.message(player, messageManager.getString("MainMenu.Chat.Enabled", player));
                }
            } else {
                settings.setChatEnabled(false);
                if (messageManager.getBoolean("CustomCommands.Enabled", player)) {
                    MessageUtil.message(player, messageManager.getString("CustomCommands.Options.Chat.Disabled", player));
                } else {
                    MessageUtil.message(player, messageManager.getString("MainMenu.Chat.Disabled", player));
                }
            }
        }
    }
}
