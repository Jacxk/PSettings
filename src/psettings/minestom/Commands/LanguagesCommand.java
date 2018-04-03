package psettings.minestom.Commands;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import psettings.minestom.Managers.MessageManager;
import psettings.minestom.PSettings;
import psettings.minestom.Utilities.MessageUtil;
import psettings.minestom.Utilities.PlayerItems;
import psettings.minestom.Utilities.Util;

public class LanguagesCommand implements Listener {

    private PSettings plugin;
    private MessageManager messageManager;
    private PlayerItems playerItems;
    private Util util;

    public LanguagesCommand(PSettings plugin, Util util, MessageManager messageManager, PlayerItems playerItems) {
        this.plugin = plugin;
        this.util = util;
        this.messageManager = messageManager;
        this.playerItems = playerItems;
    }


    @EventHandler
    public void execute(PlayerCommandPreprocessEvent event) {
        FileConfiguration config = plugin.getConfig();
        Player player = event.getPlayer();

        if (event.isCancelled() || !config.getBoolean("CustomCommands.Enabled")) {
            return;
        }
        String command = event.getMessage().replace("/", "");
        String[] args = command.split(" ");
        if (args[0].equalsIgnoreCase(config.getString("CustomCommands.Options.Lang"))) {
            if (!event.isCancelled()) event.setCancelled(true);

            if (args.length == 2) {
                for (String fileName : messageManager.availableLanguages()) {
                    if (args[1].equalsIgnoreCase(fileName)) {
                        util.getSettingsManager(player).setLanguage(fileName);
                        String translator = messageManager.getString("Language.Translator", player);
                        String languageString = messageManager.getString("Language.Locale", player);

                        MessageUtil.message(player, messageManager.getString("Language.Chosen", player).replace(
                                "{translator}", translator).replace("{locale}", languageString));
                        playerItems.getItems(player);
                        return;
                    }
                }
                MessageUtil.message(player, messageManager.getString("Language.NotValid", player).replace("{locale}", args[1]));
                return;
            }
            messageManager.sendLanguagesComponent(player);
        }
    }
}
