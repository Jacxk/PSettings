package psettings.minestom.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import psettings.minestom.ConfigurationFIles.MenuFiles.MainMenuFile;
import psettings.minestom.Managers.SettingsManager;
import psettings.minestom.hooks.PartyAndFriends;

public class UtilEffects {
    private MainMenuFile mainMenuFile;
    private PartyAndFriends partyAndFriends;
    private Util util;

    public UtilEffects(MainMenuFile menu, PartyAndFriends partyAndFriends, Util util) {
        this.mainMenuFile = menu;
        this.partyAndFriends = partyAndFriends;
        this.util = util;
    }

    public void giveSpeed(Player player) {
        FileConfiguration menus = mainMenuFile.getConfiguration();
        PotionEffect potion = new PotionEffect(PotionEffectType.SPEED, 10000 * 1000,
                menus.getInt("MainMenu.Speed.Level") - 1, false, false);
        player.addPotionEffect(potion);
    }

    public void giveJump(Player player) {
        FileConfiguration menus = mainMenuFile.getConfiguration();
        PotionEffect potion = new PotionEffect(PotionEffectType.JUMP, 10000 * 1000,
                menus.getInt("MainMenu.Jump.Level") - 1, false, false);
        player.addPotionEffect(potion);
    }

    public void hidePlayers(Player player) {
        SettingsManager settingsManager = util.getSettingsManager(player);
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!settingsManager.isFriendsVisOnlyEnabled() && !settingsManager.isStaffVisOnlyEnabled() &&
                    !settingsManager.isVipVisOnlyEnabled()) {
                player.hidePlayer(target);
            }
            if (!partyAndFriends.isFriends(player) && settingsManager.isFriendsVisOnlyEnabled()) {
                player.hidePlayer(target);
            }
            if (!target.hasPermission(Permissions.ADMIN) || !target.hasPermission(Permissions.HIDE_STAFF)) {
                if (settingsManager.isStaffVisOnlyEnabled()) {
                    player.hidePlayer(target);
                }
            }
            if (!target.hasPermission(Permissions.HIDE_BYPASS) && settingsManager.isVipVisOnlyEnabled()) {
                player.hidePlayer(target);
            }
        }
    }

    public void showPlayers(Player player) {
        for (Player target : Bukkit.getOnlinePlayers()) {
            player.showPlayer(target);
        }
    }
}
