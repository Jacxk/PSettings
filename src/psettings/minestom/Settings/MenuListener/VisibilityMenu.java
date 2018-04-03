package psettings.minestom.Settings.MenuListener;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import psettings.minestom.ConfigurationFIles.MenuFiles.MainMenuFile;
import psettings.minestom.ConfigurationFIles.MenuFiles.VisibilityMenuFile;
import psettings.minestom.Managers.MessageManager;
import psettings.minestom.Managers.SettingsManager;
import psettings.minestom.PSettings;
import psettings.minestom.Settings.MenuCreator;
import psettings.minestom.Utilities.MessageUtil;
import psettings.minestom.Utilities.Permissions;
import psettings.minestom.Utilities.Util;
import psettings.minestom.enums.SendSound;
import psettings.minestom.hooks.PartyAndFriends;

public class VisibilityMenu implements Listener {

    private PSettings plugin;
    private VisibilityMenuFile visibility;
    private MenuCreator menuGui;
    private MainMenuFile menu;
    private MessageManager messageManager;
    private PartyAndFriends partyAndFriends;
    private Util util;

    public VisibilityMenu(PSettings plugin, VisibilityMenuFile visibility, MenuCreator menuGui, MainMenuFile menu,
                          MessageManager messageManager, PartyAndFriends partyAndFriends, Util util) {
        this.plugin = plugin;
        this.visibility = visibility;
        this.menuGui = menuGui;
        this.menu = menu;
        this.messageManager = messageManager;
        this.partyAndFriends = partyAndFriends;
        this.util = util;
    }

    @EventHandler
    public void VisibilityMenuClick(InventoryClickEvent event) {
        FileConfiguration menus = visibility.getConfiguration();
        FileConfiguration menuDE = menu.getConfiguration();
        FileConfiguration config = plugin.getConfig();
        Player player = (Player) event.getWhoClicked();
        SettingsManager settings = util.getSettingsManager(player);
        if (event.getView().getTopInventory().getTitle().equals(MessageUtil.color(messageManager.getString("MenuItems.VisibilityMenu.Title", player)))) {
            event.setCancelled(true);
            if (menus.getBoolean("VisibilityMenu.Privileged.Enabled")) {
                if (event.getSlot() == menus.getInt("VisibilityMenu.Privileged.Slot") + 9) {
                    if (event.getCurrentItem().hasItemMeta()) {
                        String displayname = event.getCurrentItem().getItemMeta().getDisplayName();
                        if (displayname.equals(MessageUtil.color(messageManager.getString("MenuItems.Enabled.Name", player))) || displayname.equals(MessageUtil.color(messageManager.getString("MenuItems.Disabled.Name", player)))) {
                            if (!settings.isVipVisOnlyEnabled()) {
                                if (menus.getBoolean("VisibilityMenu.Privileged.UsePermission")) {
                                    if (!player.hasPermission(menus.getString("VisibilityMenu.Privileged.Permission")) || !Permissions.hasAllOptionsPerm(player)) {
                                        MessageUtil.message(player, menus.getString("VisibilityMenu.Privileged.NoPermission"));
                                        player.closeInventory();
                                        util.sendSound(player, SendSound.DENY);
                                        return;
                                    }
                                }
                                MessageUtil.message(player, messageManager.getString("VisibilityMenu.Privileged.Enabled", player));
                                util.sendSound(player, SendSound.ENABLE);
                                settings.setVipVisOnlyEnabled(true);

                                if (settings.isFriendsVisOnlyEnabled()) settings.setFriendsVisOnlyEnabled(false);
                                if (settings.isStaffVisOnlyEnabled()) settings.setStaffVisOnlyEnabled(false);

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.visibilityMenu(player);
                            } else {
                                MessageUtil.message(player, messageManager.getString("VisibilityMenu.Privileged.Disabled", player));
                                util.sendSound(player, SendSound.DISABLE);
                                settings.setVipVisOnlyEnabled(false);

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.visibilityMenu(player);
                            }
                        }
                    }
                }
            }
            if (menus.getBoolean("VisibilityMenu.Friends.Enabled")) {
                if (event.getSlot() == menus.getInt("VisibilityMenu.Friends.Slot") + 9) {
                    if (event.getCurrentItem().hasItemMeta()) {
                        String displayname = event.getCurrentItem().getItemMeta().getDisplayName();
                        if (displayname.equals(MessageUtil.color(messageManager.getString("MenuItems.Enabled.Name", player)))
                                || displayname.equals(MessageUtil.color(messageManager.getString("MenuItems.Disabled.Name", player)))) {
                            if (!partyAndFriends.isPAFEnabled()) {
                                MessageUtil.message(player, messageManager.getString("PluginNotFound.Friends", player));
                                player.closeInventory();
                                return;
                            }
                            if (!settings.isFriendsVisOnlyEnabled()) {
                                if (menus.getBoolean("VisibilityMenu.Friends.UsePermission")) {
                                    if (!player.hasPermission(menus.getString("VisibilityMenu.Friends.Permission")) || !Permissions.hasAllOptionsPerm(player)) {
                                        MessageUtil.message(player, menus.getString("VisibilityMenu.Friends.NoPermission"));
                                        player.closeInventory();
                                        util.sendSound(player, SendSound.DENY);
                                        return;
                                    }
                                }
                                MessageUtil.message(player, messageManager.getString("VisibilityMenu.Friends.Enabled", player));
                                util.sendSound(player, SendSound.ENABLE);
                                settings.setFriendsVisOnlyEnabled(true);

                                if (settings.isVipVisOnlyEnabled()) settings.setVipVisOnlyEnabled(false);
                                if (settings.isStaffVisOnlyEnabled()) settings.setStaffVisOnlyEnabled(false);

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.visibilityMenu(player);

                            } else {
                                MessageUtil.message(player, messageManager.getString("VisibilityMenu.Friends.Disabled", player));
                                util.sendSound(player, SendSound.DISABLE);
                                settings.setFriendsVisOnlyEnabled(false);

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.visibilityMenu(player);

                            }
                        }
                    }
                }
            }
            if (menus.getBoolean("VisibilityMenu.Staff.Enabled")) {
                if (event.getSlot() == menus.getInt("VisibilityMenu.Staff.Slot") + 9) {
                    if (event.getCurrentItem().hasItemMeta()) {
                        String displayname = event.getCurrentItem().getItemMeta().getDisplayName();
                        if (displayname.equals(MessageUtil.color(messageManager.getString("MenuItems.Enabled.Name", player)))
                                || displayname.equals(MessageUtil.color(messageManager.getString("MenuItems.Disabled.Name", player)))) {
                            if (!settings.isStaffVisOnlyEnabled()) {
                                if (menus.getBoolean("VisibilityMenu.Staff.UsePermission")) {
                                    if (!player.hasPermission(menus.getString("VisibilityMenu.Staff.Permission")) || !Permissions.hasAllOptionsPerm(player)) {
                                        MessageUtil.message(player, menus.getString("VisibilityMenu.Staff.NoPermission"));
                                        player.closeInventory();
                                        util.sendSound(player, SendSound.DENY);
                                        return;
                                    }
                                }
                                MessageUtil.message(player, messageManager.getString("VisibilityMenu.Staff.Enabled", player));
                                util.sendSound(player, SendSound.ENABLE);
                                settings.setStaffVisOnlyEnabled(true);

                                if (settings.isFriendsVisOnlyEnabled()) settings.setFriendsVisOnlyEnabled(false);
                                if (settings.isVipVisOnlyEnabled()) settings.setVipVisOnlyEnabled(false);

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.visibilityMenu(player);

                            } else {
                                MessageUtil.message(player, messageManager.getString("VisibilityMenu.Staff.Disabled", player));
                                util.sendSound(player, SendSound.DISABLE);
                                settings.setStaffVisOnlyEnabled(false);

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.visibilityMenu(player);
                            }
                        }
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
