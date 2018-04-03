package psettings.minestom.Settings.Listeners;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import psettings.minestom.Managers.CooldownManager;
import psettings.minestom.Managers.MessageManager;
import psettings.minestom.Managers.SettingsManager;
import psettings.minestom.PSettings;
import psettings.minestom.Settings.MenuCreator;
import psettings.minestom.Utilities.MessageUtil;
import psettings.minestom.Utilities.PlayerItems;
import psettings.minestom.Utilities.Util;
import psettings.minestom.Utilities.UtilEffects;
import psettings.minestom.enums.SendSound;

import java.util.List;
import java.util.UUID;

public class PlayerItemListener implements Listener {
    private PSettings plugin;
    private MenuCreator menuCreator;
    private Util util;
    private PlayerItems playerItems;
    private UtilEffects utilEffects;
    private MessageManager messageManager;

    public PlayerItemListener(PSettings plugin, MenuCreator menuCreator, Util util, PlayerItems playerItems,
                              UtilEffects utilEffects, MessageManager messageManager) {
        this.plugin = plugin;
        this.menuCreator = menuCreator;
        this.util = util;
        this.playerItems = playerItems;
        this.utilEffects = utilEffects;
        this.messageManager = messageManager;
    }

    @EventHandler
    public void itemDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        FileConfiguration config = plugin.getConfig();
        if (item == null || !item.hasItemMeta() || event.getItemDrop() == null)
            return;

        if (item.isSimilar(playerItems.getSettingsItem(event.getPlayer())) && config.getBoolean("Join-Item.Enabled"))
            event.setCancelled(true);
        else if (item.isSimilar(playerItems.getShowPlayerItem(event.getPlayer()))
                || item.isSimilar(playerItems.getHidePlayerItem(event.getPlayer()))
                && config.getBoolean("PlayerToggle-Item.Use")) event.setCancelled(true);
    }

    @EventHandler
    public void itemInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        FileConfiguration config = plugin.getConfig();
        UUID uuid = player.getUniqueId();
        SettingsManager settings = util.getSettingsManager(player);

        if (!event.hasItem() || event.getItem().getItemMeta().hasEnchants() || !event.getItem().getItemMeta().hasDisplayName() || !item.hasItemMeta())
            return;

        if (item.isSimilar(playerItems.getSettingsItem(event.getPlayer())) && config.getBoolean("Join-Item.Enabled"))
            if (!CooldownManager.isInCooldown(uuid, "opener")) {
                CooldownManager cooldownManager = new CooldownManager(uuid, "opener",
                        config.getInt("Join-Item.Cooldown"));
                cooldownManager.start();
                menuCreator.mainMenu(player);
            } else MessageUtil.message(player, messageManager.getString("Player.OptionOpenCooldown", player)
                    .replace("{seconds}", CooldownManager.getTimeLeft(uuid, "opener") + ""));

        else if (item.isSimilar(playerItems.getShowPlayerItem(event.getPlayer()))
                || item.isSimilar(playerItems.getHidePlayerItem(event.getPlayer()))
                && config.getBoolean("PlayerToggle-Item.Use")) {
            if (!CooldownManager.isInCooldown(uuid, "visibility")) {
                CooldownManager cooldownManager = new CooldownManager(uuid, "visibility",
                        config.getInt("PlayerToggle-Item.Cooldown"));
                cooldownManager.start();
                if (settings.isVisibilityEnabled()) {
                    settings.setVisibilityEnabled(false);
                    utilEffects.hidePlayers(player);
                    util.sendSound(player, SendSound.DISABLE);
                } else {
                    settings.setVisibilityEnabled(true);
                    utilEffects.showPlayers(player);
                    util.sendSound(player, SendSound.ENABLE);
                }
                playerItems.playerToggleItem(player);
            } else {
                MessageUtil.message(player, messageManager.getString("Player.VisibilityItemCooldown", player)
                        .replace("{seconds}", CooldownManager.getTimeLeft(uuid, "visibility") + ""));
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        playerItems.getItems(player);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        List<ItemStack> itemStacks = event.getDrops();
        FileConfiguration config = plugin.getConfig();

        for (ItemStack itemStack : itemStacks) {
            if (itemStack.isSimilar(playerItems.getSettingsItem(player)) && config.getBoolean("Join-Item.Enabled"))
                itemStacks.remove(itemStack);
            else if (itemStack.isSimilar(playerItems.getShowPlayerItem(player))
                    || itemStack.isSimilar(playerItems.getHidePlayerItem(player))
                    && config.getBoolean("PlayerToggle-Item.Use"))
                itemStacks.remove(itemStack);
        }
    }
}