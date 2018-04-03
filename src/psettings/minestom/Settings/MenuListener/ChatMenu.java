package psettings.minestom.Settings.MenuListener;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import psettings.minestom.ConfigurationFIles.MenuFiles.ChatMenuFile;
import psettings.minestom.ConfigurationFIles.MenuFiles.MainMenuFile;
import psettings.minestom.Managers.MessageManager;
import psettings.minestom.Managers.SettingsManager;
import psettings.minestom.PSettings;
import psettings.minestom.Settings.MenuCreator;
import psettings.minestom.Utilities.MessageUtil;
import psettings.minestom.Utilities.Permissions;
import psettings.minestom.Utilities.Util;
import psettings.minestom.enums.SendSound;
import psettings.minestom.hooks.PartyAndFriends;

public class ChatMenu implements Listener {

    private PSettings plugin;
    private MenuCreator menuGui;
    private ChatMenuFile chat;
    private MainMenuFile menu;
    private MessageManager messageManager;
    private PartyAndFriends partyAndFriends;
    private Util util;

    public ChatMenu(PSettings plugin, MenuCreator menuGui, ChatMenuFile chat, MainMenuFile menu,
                    MessageManager messageManager, PartyAndFriends partyAndFriends, Util util) {
        this.plugin = plugin;
        this.menuGui = menuGui;
        this.chat = chat;
        this.menu = menu;
        this.messageManager = messageManager;
        this.partyAndFriends = partyAndFriends;
        this.util = util;
    }

    @EventHandler
    public void chatMenuClick(InventoryClickEvent event) {
        FileConfiguration menus = chat.getConfiguration();
        FileConfiguration menuDE = menu.getConfiguration();
        FileConfiguration config = plugin.getConfig();
        Player player = (Player) event.getWhoClicked();
        SettingsManager settings = util.getSettingsManager(player);
        if (event.getView().getTopInventory().getTitle().equals(MessageUtil.color(messageManager.getString("MenuItems.ChatMenu.Title", player)))) {
            event.setCancelled(true);
            if (menus.getBoolean("ChatMenu.Mention.Enabled")) {
                if (event.getSlot() == menus.getInt("ChatMenu.Mention.Slot") + 9) {
                    if (event.getCurrentItem().hasItemMeta()) {
                        String displayname = event.getCurrentItem().getItemMeta().getDisplayName();
                        if (displayname.equals(MessageUtil.color(messageManager.getString("MenuItems.Enabled.Name", player))) || displayname.equals(MessageUtil.color(messageManager.getString("MenuItems.Disabled.Name", player)))) {
                            if (!settings.isMentionEnabled()) {
                                if (menus.getBoolean("ChatMenu.Mention.UsePermission")) {
                                    if (!player.hasPermission(menus.getString("ChatMenu.Mention.Permission")) || !Permissions.hasAllOptionsPerm(player)) {
                                        MessageUtil.message(player, menus.getString("ChatMenu.Mention.NoPermission"));
                                        player.closeInventory();
                                         util.sendSound(player, SendSound.DENY);
                                        return;
                                    }
                                }
                                MessageUtil.message(player, messageManager.getString("ChatMenu.Mention.Enabled", player));
                                util.sendSound(player, SendSound.ENABLE);
                                settings.setMentionEnabled(true);

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.chatMenu(player);

                            } else {
                                MessageUtil.message(player, messageManager.getString("ChatMenu.Mention.Disabled", player));
                                util.sendSound(player, SendSound.DISABLE);
                                settings.setMentionEnabled(false);

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.chatMenu(player);
                            }
                        }
                    }
                }
            }
            if (menus.getBoolean("ChatMenu.FriendsOnly.Enabled")) {
                if (event.getSlot() == menus.getInt("ChatMenu.FriendsOnly.Slot") + 9) {
                    if (event.getCurrentItem().hasItemMeta()) {
                        String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
                        if (displayName.equals(MessageUtil.color(messageManager.getString("MenuItems.Enabled.Name", player))) || displayName.equals(MessageUtil.color(messageManager.getString("MenuItems.Disabled.Name", player)))) {
                            if (!partyAndFriends.isPAFEnabled()) {
                                MessageUtil.message(player, messageManager.getString("PluginNotFound.Friends", player));
                                player.closeInventory();
                                return;
                            }
                            if (!settings.isFriendsChatOnlyEnabled()) {
                                if (menus.getBoolean("ChatMenu.FriendsOnly.UsePermission")) {
                                    if (!player.hasPermission(menus.getString("ChatMenu.FriendsOnly.Permission")) || !Permissions.hasAllOptionsPerm(player)) {
                                        MessageUtil.message(player, menus.getString("ChatMenu.FriendsOnly.NoPermission"));
                                        player.closeInventory();
                                        util.sendSound(player, SendSound.DENY);
                                        return;
                                    }
                                }
                                MessageUtil.message(player, messageManager.getString("ChatMenu.FriendsOnly.Enabled", player));
                                util.sendSound(player, SendSound.ENABLE);
                                settings.setFriendsChatOnlyEnabled(true);

                                if (settings.isStaffChatOnlyEnabled()) settings.setStaffChatOnlyEnabled(false);

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.chatMenu(player);
                            } else {
                                MessageUtil.message(player, messageManager.getString("ChatMenu.FriendsOnly.Disabled", player));
                                util.sendSound(player, SendSound.DISABLE);
                                settings.setFriendsChatOnlyEnabled(false);
                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.chatMenu(player);
                            }
                        }
                    }
                }
            }
            if (menus.getBoolean("ChatMenu.StaffOnly.Enabled")) {
                if (event.getSlot() == menus.getInt("ChatMenu.StaffOnly.Slot") + 9) {
                    if (event.getCurrentItem().hasItemMeta()) {
                        String displayname = event.getCurrentItem().getItemMeta().getDisplayName();
                        if (displayname.equals(MessageUtil.color(messageManager.getString("MenuItems.Enabled.Name", player))) || displayname.equals(MessageUtil.color(messageManager.getString("MenuItems.Disabled.Name", player)))) {
                            if (!settings.isStaffChatOnlyEnabled()) {
                                if (menus.getBoolean("ChatMenu.StaffOnly.UsePermission")) {
                                    if (!player.hasPermission(menus.getString("ChatMenu.StaffOnly.Permission")) || !Permissions.hasAllOptionsPerm(player)) {
                                        MessageUtil.message(player, menus.getString("ChatMenu.StaffOnly.NoPermission"));
                                        player.closeInventory();
                                        util.sendSound(player, SendSound.DENY);
                                        return;
                                    }
                                }
                                MessageUtil.message(player, messageManager.getString("ChatMenu.StaffOnly.Enabled", player));
                                util.sendSound(player, SendSound.ENABLE);
                                settings.setStaffChatOnlyEnabled(true);
                                if (settings.isFriendsChatOnlyEnabled()) settings.setFriendsChatOnlyEnabled(false);

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.chatMenu(player);

                            } else {
                                MessageUtil.message(player, messageManager.getString("ChatMenu.StaffOnly.Disabled", player));
                                util.sendSound(player, SendSound.DISABLE);
                                settings.setStaffChatOnlyEnabled(false);
                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.chatMenu(player);
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
