package psettings.minestom.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import psettings.minestom.ConfigurationFIles.MenuFiles.MainMenuFile;
import psettings.minestom.DataBase.MySQLManager;
import psettings.minestom.Managers.MessageManager;
import psettings.minestom.Managers.SettingsManager;
import psettings.minestom.PSettings;
import psettings.minestom.enums.SendSound;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Util {
    private PSettings plugin;
    private MainMenuFile mainMenuFile;
    private MySQLManager mySQLManager;
    private HashMap<Player, SettingsManager> playerSettings;

    public Util(PSettings plugin, MainMenuFile mainMenuFile, MySQLManager mySQLManager) {
        this.plugin = plugin;
        this.mainMenuFile = mainMenuFile;
        this.mySQLManager = mySQLManager;
        this.playerSettings = new HashMap<>();
    }

    public void addPlayerSetting(Player player, SettingsManager settingsManager) {
        playerSettings.put(player, settingsManager);
    }

    public SettingsManager getSettingsManager(Player player) {
        return playerSettings.get(player);
    }

    public void importData(SettingsManager settings, UUID uuid) {
        settings.setSpeedEnabled(mySQLManager.getSpeed(uuid));
        settings.setJumpEnabled(mySQLManager.getJump(uuid));
        settings.setFlyEnabled(mySQLManager.getFly(uuid));
        settings.setChatEnabled(mySQLManager.getChat(uuid));
        settings.setRadioEnabled(mySQLManager.getRadio(uuid));
        settings.setVisibilityEnabled(mySQLManager.getVisibility(uuid));
        settings.setStackerEnabled(mySQLManager.getStacker(uuid));
        settings.setDoubleJumpEnabled(mySQLManager.getDoubleJump(uuid));
        settings.setFriendsChatOnlyEnabled(mySQLManager.getFriendsChatOnly(uuid));
        settings.setStaffChatOnlyEnabled(mySQLManager.getStaffChatOnly(uuid));
        settings.setFriendsVisOnlyEnabled(mySQLManager.getFriendsVisOnly(uuid));
        settings.setStaffVisOnlyEnabled(mySQLManager.getStaffVisOnly(uuid));
        settings.setVipVisOnlyEnabled(mySQLManager.getVipVisOnly(uuid));
        settings.setMentionEnabled(mySQLManager.getMention(uuid));
        settings.setParticle(mySQLManager.getParticle(uuid));
        settings.setBloodEnabled(mySQLManager.getBlood(uuid));
        settings.setBloodParticle(mySQLManager.getBloodParticle(uuid));
        settings.setLanguage(mySQLManager.getLanguage(uuid));
        settings.setJumpAmplifier(mySQLManager.getJumpAmplifier(uuid));
        settings.setSpeedAmplifier(mySQLManager.getSpeedAmplifier(uuid));
    }

    public String getLocale(Player player) {
        try {
            Object invoke = Objects.requireNonNull(getMethod(player.getClass())).invoke(player, (Object[]) null);
            Field field = invoke.getClass().getDeclaredField("locale");
            field.setAccessible(true);
            return (String) field.get(invoke);
        } catch (IllegalAccessException | NoSuchFieldException | InvocationTargetException | NullPointerException e) {
            e.printStackTrace();
            return plugin.getConfig().getString("Default-Language");
        }
    }

    private Method getMethod(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) if (method.getName().equals("getHandle")) return method;

        return null;
    }

    private void spawnParticles(Player player, Location location) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.canSee(player)) {
                SettingsManager settings = playerSettings.get(player);
                if (settings.getParticle().equals("NONE")) return;

                plugin.getNMS().spawnParticle(online, settings.getParticle().toUpperCase(), location);
                plugin.getNMS().spawnParticle(online, settings.getParticle().toUpperCase(), location.subtract(0.11, 0.1, 0));
                plugin.getNMS().spawnParticle(online, settings.getParticle().toUpperCase(), location.subtract(0, 0.1, -0.11));
                plugin.getNMS().spawnParticle(online, settings.getParticle().toUpperCase(), location.add(0.11, 0.2, 0));
                plugin.getNMS().spawnParticle(online, settings.getParticle().toUpperCase(), location.add(0, 0.2, -0.11));
            }
        }
    }

    public void playParticle(Player player) {
        if (player.isInsideVehicle()) return;

        if (plugin.getConfig().getBoolean("UseParticlePermission") && !player.hasPermission(Permissions.PARTICLE_JUMP)) return;

        if (mainMenuFile.getConfiguration().getBoolean("MainMenu.DoubleJump.Particle.Use")) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                SettingsManager settings = playerSettings.get(player);
                if (online.canSee(player)) {
                    if (settings.getParticle().equals("NONE")) return;

                    plugin.getNMS().spawnParticle(player, mainMenuFile.getConfiguration().getString("MainMenu.DoubleJump.Particle.OnJump"), player.getLocation());
                }
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    spawnParticles(player, player.getLocation());
                    if (player.isOnGround() || !player.isOnline()) this.cancel();
                }
            }.runTaskTimerAsynchronously(plugin, 0, 1L);
        }
    }

    public void sendStackedMessage(Player player, int period, boolean isPlayerStacked, String message) {
        FileConfiguration config = plugin.getConfig();
        String type = config.getString("PlayerInHead.Type").toLowerCase();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isPlayerStacked) {
                    if (type.contains("title")) plugin.getNMS().sendTitle(player, message, 0, 2 * 20, 0);

                    if (type.contains("subtitle")) plugin.getNMS().sendSubtitle(player, message, 0, 2 * 20, 0);

                    if (type.contains("actionbar")) plugin.getNMS().sendActionbar(player, message);

                    if (type.contains("chat")) player.sendMessage(message);

                } else this.cancel();
            }
        }.runTaskTimerAsynchronously(plugin, 0, period * 20L);
    }

    public void sendSound(Player player, SendSound sound) {
        try {
            FileConfiguration config = plugin.getConfig();
            switch (sound) {
                case NO_PERMISSION:
                    player.playSound(player.getLocation(), Sound.valueOf(config.getString("Sounds.Back.Sound")), 2,
                            (float) config.getInt("Sounds.Back.SoundPitch"));
                    break;
                case BACK:
                    player.playSound(player.getLocation(), Sound.valueOf(config.getString("Sounds.Back.Sound")), 2,
                            (float) config.getInt("Sounds.Back.SoundPitch"));
                    break;
                case ENABLE:
                    player.playSound(player.getLocation(), Sound.valueOf(config.getString("Sounds.EnabledItem.Sound")), 2,
                            (float) config.getInt("Sounds.EnabledItem.SoundPitch"));
                    break;
                case DISABLE:
                    player.playSound(player.getLocation(), Sound.valueOf(config.getString("Sounds.DisabledItem.Sound")), 2,
                            (float) config.getInt("Sounds.DisabledItem.SoundPitch"));
                    break;
                case RELOAD:
                    player.playSound(player.getLocation(), Sound.valueOf(config.getString("Sounds.Reload.Sound")), 2,
                            (float) config.getInt("Sounds.Reload.SoundPitch"));
                    break;
                case DENY:
                    player.playSound(player.getLocation(), Sound.valueOf(config.getString("Sounds.Deny.Sound")), 2,
                            (float) config.getInt("Sounds.Deny.SoundPitch"));
                    break;
                case NO_FLY:
                    player.playSound(player.getLocation(), Sound.valueOf(config.getString("Sounds.NoFly.Sound")), 2,
                            (float) config.getInt("Sounds.NoFly.SoundPitch"));
                    break;
                case NO_CHAT:
                    player.playSound(player.getLocation(), Sound.valueOf(config.getString("Sounds.NoChat.Sound")), 2,
                            (float) config.getInt("Sounds.NoChat.SoundPitch"));
                    break;
                case NO_DOUBLE_JUMP:
                    player.playSound(player.getLocation(), Sound.valueOf(config.getString("Sounds.NoDoubleJump.Sound")), 2,
                            (float) config.getInt("Sounds.NoDoubleJump.SoundPitch"));
                    break;
                case INSIDE_MENU:
                    player.playSound(player.getLocation(), Sound.valueOf(config.getString("Sounds.InsideMenu.Sound")), 2,
                            (float) config.getInt("Sounds.InsideMenu.SoundPitch"));
                    break;
                case PARTICLES_MENU:
                    player.playSound(player.getLocation(), Sound.valueOf(config.getString("Sounds.ParticlesMenu.Sound")), 2,
                            (float) config.getInt("Sounds.ParticlesMenu.SoundPitch"));
                    break;
                case ON_DOUBLE_JUMP:
                    player.playSound(player.getLocation(), Sound.valueOf(config.getString("Sounds.OnDoubleJump.Sound")), 2,
                            (float) config.getInt("Sounds.OnDoubleJump.SoundPitch"));
                    break;
                case MENTION:
                    player.playSound(player.getLocation(), Sound.valueOf(config.getString("Sounds.OnMention.Sound")), 2,
                            (float) config.getInt("Sounds.OnMention.SoundPitch"));
                    break;
            }
        } catch (IllegalArgumentException e) {
            MessageUtil.message(player, "&c&lERROR: &7" + e.getMessage());
            MessageUtil.message(player, "&7Make sure the you are using the correct sound name according to your server version.");
        }
    }
}
