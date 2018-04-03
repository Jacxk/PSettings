package psettings.minestom.Settings.MenuListener;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import psettings.minestom.ConfigurationFIles.MenuFiles.ParticlesMenuFile;
import psettings.minestom.Managers.MessageManager;
import psettings.minestom.Managers.SettingsManager;
import psettings.minestom.PSettings;
import psettings.minestom.Settings.MenuCreator;
import psettings.minestom.Utilities.MessageUtil;
import psettings.minestom.Utilities.Util;
import psettings.minestom.enums.SendSound;

public class ParticleMenu implements Listener {

    private PSettings plugin;
    private MenuCreator menuGui;
    private ParticlesMenuFile particles;
    private MessageManager messageManager;
    private Util util;

    public ParticleMenu(PSettings plugin, MenuCreator menuGui, ParticlesMenuFile particles,
                        MessageManager messageManager, Util util) {
        this.plugin = plugin;
        this.menuGui = menuGui;
        this.particles = particles;
        this.messageManager = messageManager;
        this.util = util;
    }

    @EventHandler
    public void ParticlesMenuClick(InventoryClickEvent event) {
        FileConfiguration menus = particles.getConfiguration();
        FileConfiguration config = plugin.getConfig();
        Player player = (Player) event.getWhoClicked();
        SettingsManager settings = util.getSettingsManager(player);
        if (event.getView().getTopInventory().getTitle().equals(MessageUtil.color(menus.getString("Title_Menu")))) {
            if (!event.getCurrentItem().hasItemMeta() || event.getCurrentItem() == null || !event.getCurrentItem().getItemMeta().hasDisplayName()) {
                return;
            }
            event.setCancelled(true);
            ConfigurationSection section = menus.getConfigurationSection("ParticlesMenu");
            for (String string : section.getKeys(false)) {
                if (event.getSlot() == menus.getInt("ParticlesMenu." + string + ".Slot")) {
                    String displayname = event.getCurrentItem().getItemMeta().getDisplayName();
                    String particleSelected = MessageUtil.color(menus.getString("ParticlesMenu." + string + ".Name"));
                    if (event.getCurrentItem().hasItemMeta() && displayname.equals(particleSelected)) {
                        if (!menus.getBoolean("ParticlesMenu." + string + ".Default") && !player.hasPermission(menus.getString("ParticlesMenu." + string + ".Permission"))) {
                            MessageUtil.message(player, messageManager.getString("ParticlesMenu.NoPermission", player));
                            player.closeInventory();
                            util.sendSound(player, SendSound.DENY);
                            return;
                        }
                        util.sendSound(player, SendSound.PARTICLES_MENU);
                        settings.setParticle(menus.getString("ParticlesMenu." + string + ".Particle").toUpperCase());
                        if (menus.getString("ParticlesMenu." + string + ".Particle").equalsIgnoreCase("NONE")) {
                            MessageUtil.message(player, messageManager.getString("ParticlesMenu.Removed", player));
                            return;
                        }
                        MessageUtil.message(player, messageManager.getString("ParticlesMenu.Selected", player).replace("{selected}", particleSelected));

                        if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                        else menuGui.particleSelector(player);
                    }
                }
            }
            if (event.getSlot() == menus.getInt("Back.Slot") && event.getCurrentItem().hasItemMeta()) {
                String displayname = event.getCurrentItem().getItemMeta().getDisplayName();
                if (displayname.equals(MessageUtil.color(messageManager.getString("MenuItems.Back.Name", player)))) {
                    menuGui.mainMenu(player);
                    util.sendSound(player, SendSound.BACK);
                }
            }
        }
    }
}
