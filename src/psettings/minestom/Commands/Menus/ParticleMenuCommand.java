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

public class ParticleMenuCommand implements Listener {

    private PSettings plugin;
    private MenuCreator menuCreator;
    private MessageManager messageManager;

    public ParticleMenuCommand(PSettings plugin, MenuCreator menuCreator, MessageManager messageManager) {
        this.plugin = plugin;
        this.menuCreator = menuCreator;
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
        String customCommand = config.getString("CustomCommands.Menus.ParticlesMenu.Command");
        if (customCommand.equalsIgnoreCase("none")) {
            return;
        }
        if (command.equalsIgnoreCase(customCommand)) {
            if (!event.isCancelled()) {
                event.setCancelled(true);
            }
            if (!config.getBoolean("CustomCommands.Menus.Particle.Default") && !player.hasPermission(Permissions.PARTICLE_MENU_COMMAND)) {
                MessageUtil.message(player, messageManager.getString("CustomCommands.Menus.Particles.NoPermission", player));
                return;
            }
            menuCreator.particleSelector(player);
        }
    }
}
