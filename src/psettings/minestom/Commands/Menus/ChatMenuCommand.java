package psettings.minestom.Commands.Menus;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import psettings.minestom.Managers.MessageManager;
import psettings.minestom.PSettings;
import psettings.minestom.Settings.MenuCreator;
import psettings.minestom.Utilities.MessageUtil;
import psettings.minestom.Utilities.Permissions;

public class ChatMenuCommand implements Listener {

    private PSettings plugin;
    private MenuCreator menuGUI;
    private MessageManager messageManager;

    public ChatMenuCommand(PSettings plugin, MenuCreator menuGUI, MessageManager messageManager) {
        this.plugin = plugin;
        this.menuGUI = menuGUI;
        this.messageManager = messageManager;
    }


    @EventHandler
    public void execute(PlayerCommandPreprocessEvent event) {
        FileConfiguration config = plugin.getConfig();
        Player player = event.getPlayer();

        if (event.isCancelled() || !config.getBoolean("CustomCommands.Enabled")) {
            return;
        }

        String command = event.getMessage().replace("/", "");
        String customCommand = config.getString("CustomCommands.Menus.ChatMenu.Command");
        if (customCommand.equalsIgnoreCase("none")) {
            return;
        }
        if (command.equalsIgnoreCase(customCommand)) {
            if (!event.isCancelled()) {
                event.setCancelled(true);
            }
            if (!config.getBoolean("CustomCommands.Menus.ChatMenu.Default") && !player.hasPermission(Permissions.CHAT_MENU_COMMAND)) {
                MessageUtil.message(player, messageManager.getString("CustomCommands.Menus.ChatMenu.NoPermission", player));
                return;
            }
            menuGUI.chatMenu(player);
        }
    }
}
