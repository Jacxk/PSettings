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
import psettings.minestom.hooks.icJukeBox;

public class RadioCommand implements Listener {

    private PSettings plugin;
    private Util util;
    private MessageManager messageManager;
    private icJukeBox icJukeBox;

    public RadioCommand(PSettings plugin, Util util, MessageManager messageManager, psettings.minestom.hooks.icJukeBox icJukeBox) {
        this.plugin = plugin;
        this.util = util;
        this.messageManager = messageManager;
        this.icJukeBox = icJukeBox;
    }


    @EventHandler
    public void execute(PlayerCommandPreprocessEvent event) {
        FileConfiguration config = plugin.getConfig();
        if (event.isCancelled() && config.getBoolean("CustomCommands.Enabled")) {
            return;
        }
        Player player = event.getPlayer();
        String command = event.getMessage().replace("/", "");
        String customCommand = config.getString("CustomCommands.Options.Radio.Command");

        if (customCommand.equalsIgnoreCase("none")) {
            return;
        }
        if (command.equalsIgnoreCase(customCommand)) {
            if (!event.isCancelled()) {
                event.setCancelled(true);
            }
            if (!config.getBoolean("CustomCommands.Options.Radio.Default") && !player.hasPermission(Permissions.RADIO_COMMAND)) {
                MessageUtil.message(player, messageManager.getString("CustomCommands.NoPermission", player));
                util.sendSound(player, SendSound.DENY);
                return;
            }
            SettingsManager settings = util.getSettingsManager(player);

            if (!settings.isRadioEnabled()) {
                icJukeBox.addToRadio(player);
                settings.setRadioEnabled(true);
                if (messageManager.getBoolean("CustomCommands.Enabled", player)) {
                    MessageUtil.message(player, messageManager.getString("CustomCommands.Options.Radio.Enabled", player));
                } else {
                    MessageUtil.message(player, messageManager.getString("MainMenu.Radio.Enabled", player));
                }
            } else {
                icJukeBox.removeFromRadio(player);
                settings.setRadioEnabled(false);
                if (messageManager.getBoolean("CustomCommands.Enabled", player)) {
                    MessageUtil.message(player, messageManager.getString("CustomCommands.Options.Radio.Disabled", player));
                } else {
                    MessageUtil.message(player, messageManager.getString("MainMenu.Radio.Disabled", player));
                }
            }
        }
    }
}
