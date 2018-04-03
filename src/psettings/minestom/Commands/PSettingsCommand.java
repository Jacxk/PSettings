package psettings.minestom.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import psettings.minestom.ConfigurationFIles.MenuFiles.ChatMenuFile;
import psettings.minestom.ConfigurationFIles.MenuFiles.MainMenuFile;
import psettings.minestom.ConfigurationFIles.MenuFiles.ParticlesMenuFile;
import psettings.minestom.ConfigurationFIles.MenuFiles.VisibilityMenuFile;
import psettings.minestom.Managers.MessageManager;
import psettings.minestom.PSettings;
import psettings.minestom.Settings.MenuCreator;
import psettings.minestom.Updater.Update;
import psettings.minestom.Utilities.MessageUtil;
import psettings.minestom.Utilities.Permissions;
import psettings.minestom.Utilities.PlayerItems;
import psettings.minestom.Utilities.Util;
import psettings.minestom.enums.SendSound;

import java.io.File;

public class PSettingsCommand implements CommandExecutor {

    private MenuCreator menuGui;
    private PSettings plugin;
    private MainMenuFile menu;
    private VisibilityMenuFile visibility;
    private ParticlesMenuFile particlesMenuFile;
    private ChatMenuFile chat;
    private MessageManager messageManager;
    private PlayerItems playerItems;
    private Util util;
    private Update update;

    public PSettingsCommand(MenuCreator menuGui, PSettings plugin, MainMenuFile menu, VisibilityMenuFile visibility,
                            ParticlesMenuFile particlesMenuFile, ChatMenuFile chat, MessageManager messageManager, PlayerItems playerItems, Util util, Update update) {
        this.menuGui = menuGui;
        this.plugin = plugin;
        this.menu = menu;
        this.visibility = visibility;
        this.particlesMenuFile = particlesMenuFile;
        this.chat = chat;
        this.messageManager = messageManager;
        this.playerItems = playerItems;
        this.util = util;
        this.update = update;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("psettings")) {
            if (args.length == 0) {
                sender.sendMessage("");
                sender.sendMessage(MessageUtil.color("&c&lPSettings &7made by &cBy_Jack"));
                sender.sendMessage(MessageUtil.color("&7Use &c/psettings help &7to see all available commands."));
                sender.sendMessage(MessageUtil.color("&7Version: &c" + plugin.getDescription().getVersion()));
                sender.sendMessage("");
            } else {
                if (args[0].equalsIgnoreCase("help")) {
                    sender.sendMessage(MessageUtil.color("&7&m-----------------------------------"));
                    sender.sendMessage(MessageUtil.color("&c&l                     PSettings"));
                    sender.sendMessage(MessageUtil.color(""));
                    sender.sendMessage(MessageUtil.color("&8» &e/PSettings help &7- Shows this page"));
                    sender.sendMessage(MessageUtil.color("&8» &e/PSettings open &7<player> - Opens the setting menu."));
                    sender.sendMessage(MessageUtil.color("&8» &e/PSettings reload &7- Reload the config file"));
                    sender.sendMessage(MessageUtil.color("&8» &e/PSettings permissions &7- See all available permissions"));
                    sender.sendMessage(MessageUtil.color("&8» &e/PSettings getItems &7<player> - Get the item menu"));
                    sender.sendMessage(MessageUtil.color(""));
                    sender.sendMessage(MessageUtil.color("&7&m-----------------------------------"));
                }
                if (args[0].equalsIgnoreCase("open")) {
                    if (args.length == 1 && sender instanceof Player) {
                        Player player = (Player) sender;
                        menuGui.mainMenu(player);
                        return true;
                    }
                    if (args.length == 2) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target == null) {
                            MessageUtil.message(sender, messageManager.getString("PlayerNotFound", sender));
                            return true;
                        }
                        if (sender.hasPermission(Permissions.ADMIN) || sender.hasPermission(Permissions.OPEN_OTHER)) {
                            menuGui.mainMenu(target);
                            return true;
                        } else {
                            MessageUtil.message(sender, messageManager.getString("NoPermission", sender));
                            if (sender instanceof Player) {
                                Player player = (Player) sender;
                                util.sendSound(player, SendSound.DENY);
                            }
                            return true;
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("permissions")) {
                    if (sender.hasPermission("psettings.admin")) {
                        sender.sendMessage(MessageUtil.color("&7&m-----------------------------------"));
                        sender.sendMessage(MessageUtil.color("              &c&lPSettings Permissions"));
                        sender.sendMessage(MessageUtil.color(""));
                        if (args.length == 1) {
                            sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.ADMIN));
                            sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.ALL_SETTINGS));
                            sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.CHAT_COMMAND));
                            sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.CHAT_MENU_COMMAND));
                            sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.DOUBLE_JUMP_COMMAND));
                            sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.DOUBLEJUMP_COOLDOWN_BYPASS));
                            sender.sendMessage(MessageUtil.color(""));
                            sender.sendMessage(MessageUtil.color("            &c&lPage &e1&7/&e4"));
                        } else if (args.length == 2) {
                            switch (args[1].toLowerCase()) {
                                case "1":
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.ADMIN));
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.ALL_SETTINGS));
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.CHAT_COMMAND));
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.CHAT_MENU_COMMAND));
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.DOUBLE_JUMP_COMMAND));
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.DOUBLEJUMP_COOLDOWN_BYPASS));
                                    sender.sendMessage(MessageUtil.color(""));
                                    sender.sendMessage(MessageUtil.color("            &c&lPage &e1&7/&e4"));
                                    break;
                                case "2":
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.FLY_COMMAND));
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.GET_ITEM));
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.HIDE_BYPASS));
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.HIDE_STAFF));
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.JUMP_COMMAND));
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.MAIN_MENU_COMMAND));
                                    sender.sendMessage(MessageUtil.color(""));
                                    sender.sendMessage(MessageUtil.color("            &c&lPage &e2&7/&e4"));
                                    break;
                                case "3":
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.OPEN_OTHER));
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.PARTICLE_JUMP));
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.PARTICLE_MENU_COMMAND));
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.RADIO_COMMAND));
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.SPEED_COMMAND));
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.STACKER_COMMAND));
                                    sender.sendMessage(MessageUtil.color(""));
                                    sender.sendMessage(MessageUtil.color("            &c&lPage &e3&7/&e4"));
                                    break;
                                case "4":
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.VISIBILITY_COMMAND));
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.VISIBILITY_MENU_COMMAND));
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.CHAT_STAFF));
                                    sender.sendMessage(MessageUtil.color("&8» &e" + Permissions.COOLDOWN_BYPASS));
                                    sender.sendMessage(MessageUtil.color(""));
                                    sender.sendMessage(MessageUtil.color("            &c&lPage &e4&7/&e4"));
                                    break;
                                default:
                                    sender.sendMessage(MessageUtil.color("&c&lERROR: PAGE NOT FOUND..."));
                                    break;
                            }
                        }
                        sender.sendMessage(MessageUtil.color(""));
                        sender.sendMessage(MessageUtil.color("&7&m-----------------------------------"));

                        return true;
                    } else {
                        MessageUtil.message(sender, messageManager.getString("NoPermission", sender));
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            util.sendSound(player, SendSound.DENY);
                        }
                        return true;
                    }

                }
                if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("psettings.admin")) {
                        plugin.reloadConfig();
                        menu.reloadMenu();
                        visibility.reloadMenu();
                        chat.reloadMenu();
                        particlesMenuFile.reloadMenu();
                        messageManager.setFiles(new File(plugin.getDataFolder() + "/Languages").listFiles());
                        messageManager.loadLocales();
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            MessageUtil.message(sender, messageManager.getString("Reload", player));
                            util.sendSound(player, SendSound.RELOAD);
                        } else MessageUtil.message(sender, messageManager.getString("Reload", sender));
                    } else {
                        MessageUtil.message(sender, messageManager.getString("NoPermission", sender));
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            util.sendSound(player, SendSound.DENY);
                        }
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("getItems")) {
                    if (args.length == 1) return true;
                    if (sender.hasPermission(Permissions.ADMIN) || sender.hasPermission(Permissions.GET_ITEM)) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (plugin.getConfig().getBoolean("Join-Item.Enabled")) {
                            if (target == null) {
                                MessageUtil.message(sender, messageManager.getString("Player.NotFound", sender));
                                return true;
                            }
                            playerItems.getItems(target);
                            MessageUtil.message(target, messageManager.getString("Item.Got", sender));
                            MessageUtil.message(sender, messageManager.getString("Item.Given", sender)
                                    .replace("{target}", target.getName()));
                            return true;
                        } else {
                            MessageUtil.message(sender, messageManager.getString("Item.Disabled", sender));
                            return true;
                        }
                    } else {
                        MessageUtil.message(sender, messageManager.getString("NoPermission", sender));
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            util.sendSound(player, SendSound.DENY);
                        }
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("update")) {
                    if (sender.hasPermission(Permissions.ADMIN)) update.sendUpdateMessage(sender);
                    else {
                        MessageUtil.message(sender, messageManager.getString("NoPermission", sender));
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            util.sendSound(player, SendSound.DENY);
                        }
                        return true;
                    }
                }
            }
        }
        return true;
    }
}
