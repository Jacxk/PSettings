package psettings.minestom.Settings;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import psettings.minestom.ConfigurationFIles.MenuFiles.ChatMenuFile;
import psettings.minestom.ConfigurationFIles.MenuFiles.MainMenuFile;
import psettings.minestom.ConfigurationFIles.MenuFiles.ParticlesMenuFile;
import psettings.minestom.ConfigurationFIles.MenuFiles.VisibilityMenuFile;
import psettings.minestom.Managers.MessageManager;
import psettings.minestom.Managers.SettingsManager;
import psettings.minestom.Utilities.ItemBuilder;
import psettings.minestom.Utilities.MessageUtil;
import psettings.minestom.Utilities.Util;
import psettings.minestom.hooks.icJukeBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuCreator {
    private MainMenuFile menu;
    private VisibilityMenuFile visibility;
    private ChatMenuFile chat;
    private ParticlesMenuFile particlesMenuFile;
    private MessageManager messageManager;
    private icJukeBox icJukeBox;
    private Util util;

    private Inventory mainMenu;
    private Inventory particleSelector;
    private Inventory chatMenu;
    private Inventory visibilityMenu;

    public MenuCreator(MainMenuFile menu, VisibilityMenuFile visibility, ChatMenuFile chat,
                       ParticlesMenuFile particlesMenuFile, MessageManager messageManager,
                       psettings.minestom.hooks.icJukeBox icJukeBox, Util util) {
        this.menu = menu;
        this.visibility = visibility;
        this.chat = chat;
        this.particlesMenuFile = particlesMenuFile;
        this.messageManager = messageManager;
        this.icJukeBox = icJukeBox;
        this.util = util;
    }

    private void createItem(Material material, int data, Inventory inv, int Slot, String name, List<String> lore) {
        ItemBuilder itemBuilder = new ItemBuilder(material);

        itemBuilder.setAmount(1).setDataValue((short) data);
        itemBuilder.setDisplayName(MessageUtil.color(name));
        itemBuilder.setLore(MessageUtil.colorList(lore));
        itemBuilder.setItemFlags(Arrays.asList(ItemFlag.values()));

        inv.setItem(Slot, itemBuilder.build());

    }

    private void createItem(Material material, int data, Inventory inv, int Slot, String name, List<String> lore,
                            String string, SettingsManager settings, FileConfiguration particle, Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(material);

        itemBuilder.setAmount(1).setDataValue((short) data);
        itemBuilder.setDisplayName(MessageUtil.color(name));
        itemBuilder.setItemFlags(Arrays.asList(ItemFlag.values()));

        List<String> loreList = new ArrayList<>();
        for (String lor : lore) {
            if (!particle.getBoolean("ParticlesMenu." + string + ".Default") &&
                    !player.hasPermission(particle.getString("ParticlesMenu." + string + ".Permission"))) {
                loreList.add(MessageUtil.color(lor.replace("{state}", messageManager.getString(
                        "ParticlesMenu.State.Unavailable", player))));
                continue;
            }
            if (settings.getParticle().equals(particle.getString("ParticlesMenu." + string + ".Particle").toUpperCase())) {
                loreList.add(MessageUtil.color(lor.replace("{state}", messageManager.getString(
                        "ParticlesMenu.State.Selected", player))));
            } else {
                loreList.add(MessageUtil.color(lor.replace("{state}", messageManager.getString(
                        "ParticlesMenu.State.Unselected", player))));
            }
        }
        itemBuilder.setLore(loreList);
        if (particle.getBoolean("EnchantSelection") && settings.getParticle().equals(particle.getString(
                "ParticlesMenu." + string + ".Particle").toUpperCase())) {
            itemBuilder.addEnchantments(Enchantment.DURABILITY);
        }
        inv.setItem(Slot, itemBuilder.build());
    }

    private void enableItem(Inventory inv, int slot, Player player) {
        FileConfiguration menus = menu.getConfiguration();
        ItemBuilder itemBuilder = new ItemBuilder(Material.matchMaterial(menus.getString("Enabled.Material")));

        itemBuilder.setAmount(1).setDataValue((short) menus.getInt("Enabled.Data"));
        itemBuilder.setDisplayName(MessageUtil.color(messageManager.getString("MenuItems.Enabled.Name", player)));
        itemBuilder.setItemFlags(Arrays.asList(ItemFlag.values()));
        itemBuilder.setLore(MessageUtil.colorList(messageManager.getStringList("MenuItems.Enabled.Lore", player)));

        inv.setItem(slot + 9, itemBuilder.build());
    }

    private void disableItem(Inventory inv, int slot, Player player) {
        FileConfiguration menus = menu.getConfiguration();
        ItemBuilder itemBuilder = new ItemBuilder(Material.matchMaterial(menus.getString("Disabled.Material")));

        itemBuilder.setAmount(1).setDataValue((short) menus.getInt("Disabled.Data"));
        itemBuilder.setDisplayName(MessageUtil.color(messageManager.getString("MenuItems.Disabled.Name", player)));
        itemBuilder.setItemFlags(Arrays.asList(ItemFlag.values()));
        itemBuilder.setLore(MessageUtil.colorList(messageManager.getStringList("MenuItems.Disabled.Lore", player)));

        inv.setItem(slot + 9, itemBuilder.build());
    }

    public void particleSelector(Player player) {
        FileConfiguration particle = particlesMenuFile.getConfiguration();
        Inventory inv = Bukkit.createInventory(player, particle.getInt("Slots_Menu"), particle.getString("Title_Menu"));
        ConfigurationSection section = particle.getConfigurationSection("ParticlesMenu");
        SettingsManager settings = util.getSettingsManager(player);
        for (String string : section.getKeys(false)) {
            if (particle.getBoolean("LockedItem.Enable") && !particle.getBoolean("ParticlesMenu." + string + ".Default")) {
                if (!player.hasPermission(particle.getString("ParticlesMenu." + string + ".Permission"))) {
                    createItem(Material.matchMaterial(particle.getString("LockedItem.Material")),
                            particle.getInt("LockedItem.Data"), inv, particle.getInt("ParticlesMenu." + string + ".Slot"),
                            particle.getString("ParticlesMenu." + string + ".Name"),
                            particle.getStringList("ParticlesMenu." + string + ".Lore"), string, settings, particle, player);
                    continue;
                }
            }
            createItem(Material.matchMaterial(particle.getString("ParticlesMenu." + string + ".Material")),
                    particle.getInt("ParticlesMenu." + string + ".Data"), inv, particle.getInt("ParticlesMenu." + string + ".Slot"),
                    particle.getString("ParticlesMenu." + string + ".Name"), messageManager.getStringList("ParticlesMenu.Lore", player)
                    , string, settings, particle, player);
        }
        createItem(Material.matchMaterial(particle.getString("Back.Material")), particle.getInt("Back.Data"),
                inv, particle.getInt("Back.Slot"), messageManager.getString("MenuItems.Back.Name", player),
                messageManager.getStringList("MenuItems.Back.Lore", player));
        player.openInventory(inv);
        particleSelector = inv;
    }

    public void visibilityMenu(Player player) {
        FileConfiguration menus = visibility.getConfiguration();
        SettingsManager settings = util.getSettingsManager(player);
        Inventory inv = Bukkit.createInventory(player, menus.getInt("VisibilityMenu.Slots"),
                MessageUtil.color(messageManager.getString("MenuItems.VisibilityMenu.Title", player)));
        if (menus.getBoolean("VisibilityMenu.Privileged.Enabled")) {
            createItem(Material.matchMaterial(menus.getString("VisibilityMenu.Privileged.Material")),
                    menus.getInt("VisibilityMenu.Privileged.Data"), inv, menus.getInt("VisibilityMenu.Privileged.Slot"),
                    messageManager.getString("MenuItems.VisibilityMenu.Privileged.Name", player),
                    messageManager.getStringList("MenuItems.VisibilityMenu.Privileged.Lore", player));
            if (settings.isVipVisOnlyEnabled()) {
                enableItem(inv, menus.getInt("VisibilityMenu.Privileged.Slot"), player);
            } else {
                disableItem(inv, menus.getInt("VisibilityMenu.Privileged.Slot"), player);
            }
        }
        if (menus.getBoolean("VisibilityMenu.Friends.Enabled")) {
            createItem(Material.matchMaterial(menus.getString("VisibilityMenu.Friends.Material")),
                    menus.getInt("VisibilityMenu.Friends.Data"), inv, menus.getInt("VisibilityMenu.Friends.Slot"),
                    messageManager.getString("MenuItems.VisibilityMenu.Friends.Name", player),
                    messageManager.getStringList("MenuItems.VisibilityMenu.Friends.Lore", player));
            if (settings.isFriendsVisOnlyEnabled()) {
                enableItem(inv, menus.getInt("VisibilityMenu.Friends.Slot"), player);
            } else {
                disableItem(inv, menus.getInt("VisibilityMenu.Friends.Slot"), player);
            }
        }
        if (menus.getBoolean("VisibilityMenu.Staff.Enabled")) {
            createItem(Material.matchMaterial(menus.getString("VisibilityMenu.Staff.Material")),
                    menus.getInt("VisibilityMenu.Staff.Data"), inv, menus.getInt("VisibilityMenu.Staff.Slot"),
                    messageManager.getString("MenuItems.VisibilityMenu.Staff.Name", player),
                    messageManager.getStringList("MenuItems.VisibilityMenu.Staff.Lore", player));
            if (settings.isStaffVisOnlyEnabled()) {
                enableItem(inv, menus.getInt("VisibilityMenu.Staff.Slot"), player);
            } else {
                disableItem(inv, menus.getInt("VisibilityMenu.Staff.Slot"), player);
            }
        }
        createItem(Material.matchMaterial(menus.getString("Back.Material")), menus.getInt(
                "Back.Data"), inv, menus.getInt("Back.Slot"),
                messageManager.getString("MenuItems.Back.Name", player),
                messageManager.getStringList("MenuItems.Back.Lore", player));
        player.openInventory(inv);
        visibilityMenu = inv;
    }

    public void chatMenu(Player player) {
        FileConfiguration menus = chat.getConfiguration();
        SettingsManager settings = util.getSettingsManager(player);
        Inventory inv = Bukkit.createInventory(player, menus.getInt("ChatMenu.Slots"),
                MessageUtil.color(messageManager.getString("MenuItems.ChatMenu.Title", player)));
        if (menus.getBoolean("ChatMenu.Mention.Enabled")) {
            createItem(Material.matchMaterial(menus.getString("ChatMenu.Mention.Material")),
                    menus.getInt("ChatMenu.Mention.Data"), inv, menus.getInt("ChatMenu.Mention.Slot"),
                    messageManager.getString("MenuItems.ChatMenu.Mention.Name", player),
                    messageManager.getStringList("MenuItems.ChatMenu.Mention.Lore", player));
            if (settings.isMentionEnabled()) {
                enableItem(inv, menus.getInt("ChatMenu.Mention.Slot"), player);
            } else {
                disableItem(inv, menus.getInt("ChatMenu.Mention.Slot"), player);
            }
        }
        if (menus.getBoolean("ChatMenu.FriendsOnly.Enabled")) {
            createItem(Material.matchMaterial(menus.getString("ChatMenu.FriendsOnly.Material")),
                    menus.getInt("ChatMenu.FriendsOnly.Data"), inv, menus.getInt("ChatMenu.FriendsOnly.Slot"),
                    messageManager.getString("MenuItems.ChatMenu.FriendsOnly.Name", player),
                    messageManager.getStringList("MenuItems.ChatMenu.FriendsOnly.Lore", player));
            if (settings.isFriendsChatOnlyEnabled()) {
                enableItem(inv, menus.getInt("ChatMenu.FriendsOnly.Slot"), player);
            } else {
                disableItem(inv, menus.getInt("ChatMenu.FriendsOnly.Slot"), player);
            }
        }
        if (menus.getBoolean("ChatMenu.StaffOnly.Enabled")) {
            createItem(Material.matchMaterial(menus.getString("ChatMenu.StaffOnly.Material")),
                    menus.getInt("ChatMenu.StaffOnly.Data"), inv, menus.getInt("ChatMenu.StaffOnly.Slot"),
                    messageManager.getString("MenuItems.ChatMenu.StaffOnly.Name", player),
                    messageManager.getStringList("MenuItems.ChatMenu.StaffOnly.Lore", player));
            if (settings.isStaffChatOnlyEnabled()) {
                enableItem(inv, menus.getInt("ChatMenu.StaffOnly.Slot"), player);
            } else {
                disableItem(inv, menus.getInt("ChatMenu.StaffOnly.Slot"), player);
            }
        }
        createItem(Material.matchMaterial(menus.getString("Back.Material")), menus.getInt(
                "Back.Data"), inv, menus.getInt("Back.Slot"),
                messageManager.getString("MenuItems.Back.Name", player),
                messageManager.getStringList("MenuItems.Back.Lore", player));
        player.openInventory(inv);
        chatMenu = inv;
    }

    public void mainMenu(Player player) {
        FileConfiguration menus = menu.getConfiguration();
        SettingsManager settings = util.getSettingsManager(player);
        Inventory inv = Bukkit.createInventory(player, menus.getInt("MainMenu.Slots"),
                MessageUtil.color(messageManager.getString("MenuItems.MainMenu.Title", player)));
        if (menus.getBoolean("MainMenu.Speed.Enabled")) {
            createItem(Material.matchMaterial(menus.getString("MainMenu.Speed.Material")), menus.getInt(
                    "MainMenu.Speed.Data"), inv, menus.getInt("MainMenu.Speed.Slot"),
                    messageManager.getString("MenuItems.MainMenu.Speed.Name", player),
                    messageManager.getStringList("MenuItems.MainMenu.Speed.Lore", player));
            if (settings.isSpeedEnabled()) {
                enableItem(inv, menus.getInt("MainMenu.Speed.Slot"), player);
            } else {
                disableItem(inv, menus.getInt("MainMenu.Speed.Slot"), player);
            }
        }
        if (menus.getBoolean("MainMenu.Visibility.Enabled")) {
            createItem(Material.matchMaterial(menus.getString("MainMenu.Visibility.Material")), menus.getInt(
                    "MainMenu.Visibility.Data"), inv, menus.getInt("MainMenu.Visibility.Slot"),
                    messageManager.getString("MenuItems.MainMenu.Visibility.Name", player),
                    messageManager.getStringList("MenuItems.MainMenu.Visibility.Lore", player));
            if (settings.isVisibilityEnabled()) {
                enableItem(inv, menus.getInt("MainMenu.Visibility.Slot"), player);
            } else {
                disableItem(inv, menus.getInt("MainMenu.Visibility.Slot"), player);
            }
        }
        if (menus.getBoolean("MainMenu.Fly.Enabled")) {
            createItem(Material.matchMaterial(menus.getString("MainMenu.Fly.Material")), menus.getInt(
                    "MainMenu.Fly.Data"), inv, menus.getInt("MainMenu.Fly.Slot"),
                    messageManager.getString("MenuItems.MainMenu.Fly.Name", player),
                    messageManager.getStringList("MenuItems.MainMenu.Fly.Lore", player));
            if (settings.isFlyEnabled()) {
                enableItem(inv, menus.getInt("MainMenu.Fly.Slot"), player);
            } else {
                disableItem(inv, menus.getInt("MainMenu.Fly.Slot"), player);
            }
        }
        if (menus.getBoolean("MainMenu.Jump.Enabled")) {
            createItem(Material.matchMaterial(menus.getString("MainMenu.Jump.Material")), menus.getInt(
                    "MainMenu.Jump.Data"), inv, menus.getInt("MainMenu.Jump.Slot"),
                    messageManager.getString("MenuItems.MainMenu.Jump.Name", player),
                    messageManager.getStringList("MenuItems.MainMenu.Jump.Lore", player));
            if (settings.isJumpEnabled()) {
                enableItem(inv, menus.getInt("MainMenu.Jump.Slot"), player);
            } else {
                disableItem(inv, menus.getInt("MainMenu.Jump.Slot"), player);
            }
        }
        if (menus.getBoolean("MainMenu.Stacker.Enabled")) {
            createItem(Material.matchMaterial(menus.getString("MainMenu.Stacker.Material")), menus.getInt(
                    "MainMenu.Stacker.Data"), inv, menus.getInt("MainMenu.Stacker.Slot"),
                    messageManager.getString("MenuItems.MainMenu.Stacker.Name", player),
                    messageManager.getStringList("MenuItems.MainMenu.Stacker.Lore", player));
            if (settings.isStackerEnabled()) {
                enableItem(inv, menus.getInt("MainMenu.Stacker.Slot"), player);
            } else {
                disableItem(inv, menus.getInt("MainMenu.Stacker.Slot"), player);
            }
        }
        if (menus.getBoolean("MainMenu.Chat.Enabled")) {
            createItem(Material.matchMaterial(menus.getString("MainMenu.Chat.Material")), menus.getInt(
                    "MainMenu.Chat.Data"), inv, menus.getInt("MainMenu.Chat.Slot"),
                    messageManager.getString("MenuItems.MainMenu.Chat.Name", player),
                    messageManager.getStringList("MenuItems.MainMenu.Chat.Lore", player));
            if (settings.isChatEnabled()) {
                enableItem(inv, menus.getInt("MainMenu.Chat.Slot"), player);
            } else {
                disableItem(inv, menus.getInt("MainMenu.Chat.Slot"), player);
            }
        }
        if (menus.getBoolean("MainMenu.DoubleJump.Enabled")) {
            createItem(Material.matchMaterial(menus.getString("MainMenu.DoubleJump.Material")),
                    menus.getInt("MainMenu.DoubleJump.Data"), inv, menus.getInt("MainMenu.DoubleJump.Slot"),
                    messageManager.getString("MenuItems.MainMenu.DoubleJump.Name", player),
                    messageManager.getStringList("MenuItems.MainMenu.DoubleJump.Lore", player));
            if (settings.isDoubleJumpEnabled()) {
                enableItem(inv, menus.getInt("MainMenu.DoubleJump.Slot"), player);
            } else {
                disableItem(inv, menus.getInt("MainMenu.DoubleJump.Slot"), player);
            }
        }
        if (menus.getBoolean("MainMenu.Radio.Enabled") && icJukeBox.isJukeEnabled()) {
            createItem(Material.matchMaterial(menus.getString("MainMenu.Radio.Material")),
                    menus.getInt("MainMenu.Radio.Data"), inv, menus.getInt("MainMenu.Radio.Slot"),
                    messageManager.getString("MenuItems.MainMenu.Radio.Name", player),
                    messageManager.getStringList("MenuItems.MainMenu.Radio.Lore", player));
            if (settings.isRadioEnabled()) {
                enableItem(inv, menus.getInt("MainMenu.Radio.Slot"), player);
            } else {
                disableItem(inv, menus.getInt("MainMenu.Radio.Slot"), player);
            }
        }
        if (menus.getBoolean("MainMenu.Blood.Enabled")) {
            createItem(Material.matchMaterial(menus.getString("MainMenu.Blood.Material")),
                    menus.getInt("MainMenu.Blood.Data"), inv, menus.getInt("MainMenu.Blood.Slot"),
                    messageManager.getString("MenuItems.MainMenu.Blood.Name", player),
                    messageManager.getStringList("MenuItems.MainMenu.Blood.Lore", player));
            if (settings.isBloodEnabled()) {
                enableItem(inv, menus.getInt("MainMenu.Blood.Slot"), player);
            } else {
                disableItem(inv, menus.getInt("MainMenu.Blood.Slot"), player);
            }
        }
        player.openInventory(inv);
        mainMenu = inv;
    }

    public Inventory getMainMenu() {
        return mainMenu;
    }

    public Inventory getParticleSelector() {
        return particleSelector;
    }

    public Inventory getChatMenu() {
        return chatMenu;
    }

    public Inventory getVisibilityMenu() {
        return visibilityMenu;
    }
}
