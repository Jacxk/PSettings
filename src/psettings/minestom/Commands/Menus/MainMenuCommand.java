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

public class MainMenuCommand implements Listener {

    private PSettings plugin;
    private MenuCreator menuGUI;
    private MessageManager messageManager;

    public MainMenuCommand(PSettings plugin, MenuCreator menuGUI, MessageManager messageManager) {
        this.plugin = plugin;
        this.menuGUI = menuGUI;
        this.messageManager = messageManager;
    }


    @EventHandler
    public void execute(PlayerCommandPreprocessEvent event) {
        FileConfiguration config = plugin.getConfig();

        if (event.isCancelled() || !config.getBoolean("CustomCommands.Enabled")) {
            return;
        }

        Player player = event.getPlayer();
        String command = event.getMessage().replace("/", "");
        String customCommand = config.getString("CustomCommands.Menus.MainMenu.Command");
        if (customCommand.equalsIgnoreCase("none")) {
            return;
        }
        if (command.equalsIgnoreCase(customCommand)) {
            if (!event.isCancelled()) {
                event.setCancelled(true);
            }
            if (!config.getBoolean("CustomCommands.Menus.MainMenu.Default") && !player.hasPermission(Permissions.MAIN_MENU_COMMAND)) {
                MessageUtil.message(player, messageManager.getString("CustomCommands.Menus.MainMenu.NoPermission", player));
                return;
            }
            menuGUI.mainMenu(player);
        }
    }
}
