package psettings.minestom.Settings.MenuListener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.potion.PotionEffectType;
import psettings.minestom.ConfigurationFIles.MenuFiles.MainMenuFile;
import psettings.minestom.Managers.MessageManager;
import psettings.minestom.Managers.SettingsManager;
import psettings.minestom.PSettings;
import psettings.minestom.Settings.MenuCreator;
import psettings.minestom.Utilities.*;
import psettings.minestom.enums.SendSound;
import psettings.minestom.hooks.icJukeBox;

public class MainMenu implements Listener {

    private PSettings plugin;
    private UtilEffects effects;
    private MenuCreator menuGui;
    private MainMenuFile menu;
    private PlayerItems playerItems;
    private MessageManager messageManager;
    private icJukeBox icJukeBox;
    private Util util;

    public MainMenu(PSettings plugin, UtilEffects effects, MenuCreator menuGui, MainMenuFile menu,
                    PlayerItems playerItems, MessageManager messageManager, psettings.minestom.hooks.icJukeBox icJukeBox, Util util) {
        this.plugin = plugin;
        this.effects = effects;
        this.menuGui = menuGui;
        this.menu = menu;
        this.playerItems = playerItems;
        this.messageManager = messageManager;
        this.icJukeBox = icJukeBox;
        this.util = util;
    }


    @EventHandler
    public void settingsClick(InventoryClickEvent event) {
        FileConfiguration menus = menu.getConfiguration();
        FileConfiguration config = plugin.getConfig();
        Player player = (Player) event.getWhoClicked();
        SettingsManager settings = util.getSettingsManager(player);
        if (event.getView().getTopInventory().getTitle().equals(MessageUtil.color(messageManager.getString("MenuItems.MainMenu.Title", player)))) {
            event.setCancelled(true);
            if (menus.getBoolean("MainMenu.Speed.Enabled")) {
                if (event.getSlot() == menus.getInt("MainMenu.Speed.Slot") + 9) {
                    if (event.getCurrentItem().hasItemMeta()) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageUtil.color(messageManager.getString("MenuItems.Enabled.Name", player)))
                                || event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageUtil.color(messageManager.getString("MenuItems.Disabled.Name", player)))) {
                            if (!settings.isSpeedEnabled()) {
                                if (menus.getBoolean("MainMenu.Speed.UsePermission")) {
                                    if (!player.hasPermission(menus.getString("MainMenu.Speed.Permission")) || !Permissions.hasAllOptionsPerm(player)) {
                                        MessageUtil.message(player, menus.getString("MainMenu.Speed.NoPermission"));
                                        player.closeInventory();
                                         util.sendSound(player, SendSound.DENY);
                                        return;
                                    }
                                }
                                effects.giveSpeed(player);
                                util.sendSound(player, SendSound.ENABLE);
                                settings.setSpeedEnabled(true);
                                MessageUtil.message(player, messageManager.getString("MainMenu.Speed.Enabled", player));

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.mainMenu(player);
                            } else {
                                player.removePotionEffect(PotionEffectType.SPEED);
                                 util.sendSound(player, SendSound.DISABLE);
                                settings.setSpeedEnabled(false);
                                MessageUtil.message(player, messageManager.getString("MainMenu.Speed.Disabled", player));

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.mainMenu(player);
                            }
                        }
                    }
                }
            }
            if (menus.getBoolean("MainMenu.Visibility.Enabled")) {
                if (event.getSlot() == menus.getInt("MainMenu.Visibility.Slot")) {
                    menuGui.visibilityMenu(player);
                    util.sendSound(player, SendSound.INSIDE_MENU);
                }
                if (event.getSlot() == menus.getInt("MainMenu.Visibility.Slot") + 9) {
                    if (event.getCurrentItem().hasItemMeta()) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageUtil.color(messageManager.getString("MenuItems.Enabled.Name", player)))
                                || event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageUtil.color(messageManager.getString("MenuItems.Disabled.Name", player)))) {
                            if (settings.isVisibilityEnabled()) {
                                if (menus.getBoolean("MainMenu.Visibility.UsePermission")) {
                                    if (!player.hasPermission(menus.getString("MainMenu.Visibility.Permission")) || !Permissions.hasAllOptionsPerm(player)) {
                                        MessageUtil.message(player, menus.getString("MainMenu.Visibility.NoPermission"));
                                        player.closeInventory();
                                         util.sendSound(player, SendSound.DENY);
                                        return;
                                    }
                                }
                                effects.hidePlayers(player);
                                settings.setVisibilityEnabled(false);
                                playerItems.playerToggleItem(player);
                                 util.sendSound(player, SendSound.DISABLE);
                                MessageUtil.message(player, messageManager.getString("MainMenu.Visibility.Disabled", player));

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.mainMenu(player);
                            } else {
                                effects.showPlayers(player);
                                settings.setVisibilityEnabled(true);
                                playerItems.playerToggleItem(player);
                                util.sendSound(player, SendSound.ENABLE);
                                MessageUtil.message(player, messageManager.getString("MainMenu.Visibility.Enabled", player));

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.mainMenu(player);
                            }
                        }
                    }
                }
            }
            if (menus.getBoolean("MainMenu.Fly.Enabled")) {
                if (event.getSlot() == menus.getInt("MainMenu.Fly.Slot") + 9) {
                    if (event.getCurrentItem().hasItemMeta()) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageUtil.color(messageManager.getString("MenuItems.Enabled.Name", player)))
                                || event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageUtil.color(messageManager.getString("MenuItems.Disabled.Name", player)))) {
                            if (settings.isDoubleJumpEnabled()) {
                                player.closeInventory();
                                MessageUtil.message(player, messageManager.getString("MainMenu.Fly.CantFly", player));
                                util.sendSound(player, SendSound.NO_FLY);
                                return;
                            }
                            if (settings.isFlyEnabled()) {
                                player.setAllowFlight(false);
                                settings.setFlyEnabled(false);
                                 util.sendSound(player, SendSound.DISABLE);
                                MessageUtil.message(player, messageManager.getString("MainMenu.Fly.Disabled", player));

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.mainMenu(player);
                            } else {
                                if (menus.getBoolean("MainMenu.Fly.UsePermission")) {
                                    if (!player.hasPermission(menus.getString("MainMenu.Fly.Permission")) || !Permissions.hasAllOptionsPerm(player)) {
                                        MessageUtil.message(player, menus.getString("MainMenu.Fly.NoPermission"));
                                        player.closeInventory();
                                         util.sendSound(player, SendSound.DENY);
                                        return;
                                    }
                                }
                                player.setAllowFlight(true);
                                settings.setFlyEnabled(true);
                                util.sendSound(player, SendSound.ENABLE);
                                MessageUtil.message(player, messageManager.getString("MainMenu.Fly.Enabled", player));

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.mainMenu(player);
                                return;
                            }
                        }
                    }
                }
            }
            if (menus.getBoolean("MainMenu.Jump.Enabled")) {
                if (event.getSlot() == menus.getInt("MainMenu.Jump.Slot") + 9) {
                    if (event.getCurrentItem().hasItemMeta()) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageUtil.color(messageManager.getString("MenuItems.Enabled.Name", player)))
                                || event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageUtil.color(messageManager.getString("MenuItems.Disabled.Name", player)))) {
                            if (!settings.isJumpEnabled()) {
                                if (menus.getBoolean("MainMenu.Jump.UsePermission")) {
                                    if (!player.hasPermission(menus.getString("MainMenu.Jump.Permission")) || !Permissions.hasAllOptionsPerm(player)) {
                                        MessageUtil.message(player, menus.getString("MainMenu.Jump.NoPermission"));
                                        player.closeInventory();
                                         util.sendSound(player, SendSound.DENY);
                                        return;
                                    }
                                }
                                effects.giveJump(player);
                                settings.setJumpEnabled(true);
                                util.sendSound(player, SendSound.ENABLE);
                                MessageUtil.message(player, messageManager.getString("MainMenu.Jump.Enabled", player));

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.mainMenu(player);
                            } else {
                                player.removePotionEffect(PotionEffectType.JUMP);
                                settings.setJumpEnabled(false);
                                 util.sendSound(player, SendSound.DISABLE);
                                MessageUtil.message(player, messageManager.getString("MainMenu.Jump.Disabled", player));

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.mainMenu(player);
                            }
                        }
                    }
                }
            }
            if (menus.getBoolean("MainMenu.Blood.Enabled")) {
                if (event.getSlot() == menus.getInt("MainMenu.Blood.Slot") + 9) {
                    if (event.getCurrentItem().hasItemMeta()) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageUtil.color(messageManager.getString("MenuItems.Enabled.Name", player)))
                                || event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageUtil.color(messageManager.getString("MenuItems.Disabled.Name", player)))) {
                            if (!settings.isBloodEnabled()) {
                                if (menus.getBoolean("MainMenu.Blood.UsePermission")) {
                                    if (!player.hasPermission(menus.getString("MainMenu.Blood.Permission")) || !Permissions.hasAllOptionsPerm(player)) {
                                        MessageUtil.message(player, menus.getString("MainMenu.Blood.NoPermission"));
                                        player.closeInventory();
                                         util.sendSound(player, SendSound.DENY);
                                        return;
                                    }
                                }
                                settings.setBloodEnabled(true);
                                util.sendSound(player, SendSound.ENABLE);
                                MessageUtil.message(player, messageManager.getString("MainMenu.Blood.Enabled", player));

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.mainMenu(player);
                            } else {
                                settings.setBloodEnabled(false);
                                 util.sendSound(player, SendSound.DISABLE);
                                MessageUtil.message(player, messageManager.getString("MainMenu.Blood.Disabled", player));

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.mainMenu(player);
                            }
                        }
                    }
                }
            }
            if (menus.getBoolean("MainMenu.Stacker.Enabled")) {
                if (event.getSlot() == menus.getInt("MainMenu.Stacker.Slot") + 9) {
                    if (event.getCurrentItem().hasItemMeta()) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageUtil.color(messageManager.getString("MenuItems.Enabled.Name", player)))
                                || event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageUtil.color(messageManager.getString("MenuItems.Disabled.Name", player)))) {
                            if (!settings.isStackerEnabled()) {
                                if (menus.getBoolean("MainMenu.Stacker.UsePermission")) {
                                    if (!player.hasPermission(menus.getString("MainMenu.Stacker.Permission")) || !Permissions.hasAllOptionsPerm(player)) {
                                        MessageUtil.message(player, menus.getString("MainMenu.Stacker.NoPermission"));
                                        player.closeInventory();
                                         util.sendSound(player, SendSound.DENY);
                                        return;
                                    }
                                }
                                util.sendSound(player, SendSound.ENABLE);
                                settings.setStackerEnabled(true);
                                MessageUtil.message(player, messageManager.getString("MainMenu.Stacker.Enabled", player));

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.mainMenu(player);
                            } else {
                                 util.sendSound(player, SendSound.DISABLE);
                                settings.setStackerEnabled(false);
                                MessageUtil.message(player, messageManager.getString("MainMenu.Stacker.Disabled", player));

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.mainMenu(player);
                            }
                        }
                    }
                }
            }
            if (menus.getBoolean("MainMenu.Chat.Enabled")) {
                if (event.getSlot() == menus.getInt("MainMenu.Chat.Slot")) {
                    menuGui.chatMenu(player);
                    util.sendSound(player, SendSound.INSIDE_MENU);
                }
                if (event.getSlot() == menus.getInt("MainMenu.Chat.Slot") + 9) {
                    if (event.getCurrentItem().hasItemMeta()) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageUtil.color(messageManager.getString("MenuItems.Enabled.Name", player)))
                                || event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageUtil.color(messageManager.getString("MenuItems.Disabled.Name", player)))) {
                            if (!settings.isChatEnabled()) {
                                if (menus.getBoolean("MainMenu.Chat.UsePermission")) {
                                    if (!player.hasPermission(menus.getString("MainMenu.Chat.Permission")) || !Permissions.hasAllOptionsPerm(player)) {
                                        MessageUtil.message(player, menus.getString("MainMenu.Chat.NoPermission"));
                                        player.closeInventory();
                                         util.sendSound(player, SendSound.DENY);
                                        return;
                                    }
                                }
                                util.sendSound(player, SendSound.ENABLE);
                                settings.setChatEnabled(true);
                                MessageUtil.message(player, messageManager.getString("MainMenu.Chat.Enabled", player));

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.mainMenu(player);
                            } else {
                                 util.sendSound(player, SendSound.DISABLE);
                                settings.setChatEnabled(false);
                                MessageUtil.message(player, messageManager.getString("MainMenu.Chat.Disabled", player));

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.mainMenu(player);
                            }
                        }
                    }
                }
            }
            if (menus.getBoolean("MainMenu.DoubleJump.Enabled")) {
                if (event.getSlot() == menus.getInt("MainMenu.DoubleJump.Slot")) {
                    menuGui.particleSelector(player);
                    util.sendSound(player, SendSound.INSIDE_MENU);
                }
                if (event.getSlot() == menus.getInt("MainMenu.DoubleJump.Slot") + 9) {
                    if (event.getCurrentItem().hasItemMeta()) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageUtil.color(messageManager.getString("MenuItems.Enabled.Name", player)))
                                || event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageUtil.color(messageManager.getString("MenuItems.Disabled.Name", player)))) {
                            if (!settings.isDoubleJumpEnabled()) {
                                if (settings.isFlyEnabled()) {
                                    player.closeInventory();
                                    MessageUtil.message(player, messageManager.getString("MainMenu.DoubleJump.CantDoubleJump", player));
                                    util.sendSound(player, SendSound.NO_DOUBLE_JUMP);
                                    return;
                                }
                                if (menus.getBoolean("MainMenu.DoubleJump.UsePermission")) {
                                    if (!player.hasPermission(menus.getString("MainMenu.DoubleJump.Permission")) || !Permissions.hasAllOptionsPerm(player)) {
                                        MessageUtil.message(player, menus.getString("MainMenu.DoubleJump.NoPermission"));
                                        player.closeInventory();
                                        util.sendSound(player, SendSound.DENY);
                                        return;
                                    }
                                }
                                util.sendSound(player, SendSound.ENABLE);
                                settings.setDoubleJumpEnabled(true);
                                player.setAllowFlight(true);
                                MessageUtil.message(player, messageManager.getString("MainMenu.DoubleJump.Enabled", player));

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.mainMenu(player);
                            } else {
                                util.sendSound(player, SendSound.DISABLE);
                                settings.setDoubleJumpEnabled(false);
                                player.setAllowFlight(false);
                                MessageUtil.message(player, messageManager.getString("MainMenu.DoubleJump.Disabled", player));

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.mainMenu(player);
                            }
                        }
                    }
                }
            }
            if (menus.getBoolean("MainMenu.Radio.Enabled") && icJukeBox.isJukeEnabled()) {
                if (event.getSlot() == menus.getInt("MainMenu.Radio.Slot") + 9) {
                    if (event.getCurrentItem().hasItemMeta()) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageUtil.color(messageManager.getString("MenuItems.Enabled.Name", player)))
                                || event.getCurrentItem().getItemMeta().getDisplayName().equals(MessageUtil.color(messageManager.getString("MenuItems.Disabled.Name", player)))) {
                            if (!settings.isRadioEnabled()) {
                                if (menus.getBoolean("MainMenu.Radio.UsePermission")) {
                                    if (!player.hasPermission(menus.getString("MainMenu.Radio.Permission")) || !Permissions.hasAllOptionsPerm(player)) {
                                        MessageUtil.message(player, menus.getString("MainMenu.Radio.NoPermission"));
                                        player.closeInventory();
                                        util.sendSound(player, SendSound.DENY);
                                        return;
                                    }
                                }
                                util.sendSound(player, SendSound.ENABLE);
                                settings.setRadioEnabled(true);
                                icJukeBox.addToRadio(player);
                                MessageUtil.message(player, messageManager.getString("MainMenu.Radio.Enabled", player));

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.mainMenu(player);
                            } else {
                                util.sendSound(player, SendSound.DISABLE);
                                settings.setRadioEnabled(false);
                                icJukeBox.removeFromRadio(player);
                                MessageUtil.message(player, messageManager.getString("MainMenu.Radio.Disabled", player));

                                if (config.getBoolean("CloseMenuOnClick")) player.closeInventory();
                                else menuGui.mainMenu(player);
                            }
                        }
                    }
                }
            }
        }
    }
}
