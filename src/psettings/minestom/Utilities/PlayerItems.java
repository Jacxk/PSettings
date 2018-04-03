package psettings.minestom.Utilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import psettings.minestom.Managers.MessageManager;
import psettings.minestom.Managers.SettingsManager;
import psettings.minestom.PSettings;

public class PlayerItems {
    private PSettings plugin;
    private Util util;
    private MessageManager messageManager;

    public PlayerItems(PSettings plugin, Util util, MessageManager messageManager) {
        this.plugin = plugin;
        this.util = util;
        this.messageManager = messageManager;
    }

    public void getItems(Player player) {
        player.getInventory().setItem(plugin.getConfig().getInt("Join-Item.Slot"), settingsItem(player));
        playerToggleItem(player);
    }

    private ItemStack settingsItem(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.getMaterial(plugin.getConfig().getString("Join-Item.Material")));

        itemBuilder.setAmount(1).setDataValue((short) plugin.getConfig().getInt("Join-Item.Data"));
        itemBuilder.setLore(MessageUtil.colorList(messageManager.getStringList("Join-Item.Lore", player)));
        itemBuilder.setDisplayName(MessageUtil.color(messageManager.getString("Join-Item.Name", player)));

        return itemBuilder.build();
    }

    private ItemStack playerToggleShow(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.getMaterial(plugin.getConfig().getString("PlayerToggle-Item.Show.Material")));

        itemBuilder.setAmount(1).setDataValue((short) plugin.getConfig().getInt("PlayerToggle-Item.Show.Data"));
        itemBuilder.setLore(MessageUtil.colorList(messageManager.getStringList("PlayerToggle-Item.Show.Lore", player)));
        itemBuilder.setDisplayName(MessageUtil.color(messageManager.getString("PlayerToggle-Item.Show.Name", player)));

        return itemBuilder.build();
    }

    private ItemStack playerToggleHide(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.getMaterial(plugin.getConfig().getString("PlayerToggle-Item.Hide.Material")));

        itemBuilder.setAmount(1).setDataValue((short) plugin.getConfig().getInt("PlayerToggle-Item.Hide.Data"));
        itemBuilder.setLore(MessageUtil.colorList(messageManager.getStringList("PlayerToggle-Item.Hide.Lore", player)));
        itemBuilder.setDisplayName(MessageUtil.color(messageManager.getString("PlayerToggle-Item.Hide.Name", player)));

        return itemBuilder.build();
    }

    public void playerToggleItem(Player player) {
        if (plugin.getConfig().getBoolean("PlayerToggle-Item.Use")) {
            SettingsManager settingsManager = util.getSettingsManager(player);

            if (settingsManager.isVisibilityEnabled())
                player.getInventory().setItem(plugin.getConfig().getInt("PlayerToggle-Item.Slot"), playerToggleHide(player));
            else
                player.getInventory().setItem(plugin.getConfig().getInt("PlayerToggle-Item.Slot"), playerToggleShow(player));
        }

    }

    public ItemStack getSettingsItem(Player player) {
        return settingsItem(player);
    }

    public ItemStack getShowPlayerItem(Player player) {
        return playerToggleShow(player);
    }

    public ItemStack getHidePlayerItem(Player player) {
        return playerToggleHide(player);
    }
}
