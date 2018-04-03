package psettings.minestom.ConfigurationFIles.MenuFiles;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class MainMenuFile {

    private File file;
    private FileConfiguration configuration;

    public void createMenuFile(Plugin p) {
        file = new File(p.getDataFolder() + "/Menus", "/MainMenu.yml");

        if (!file.exists()) {
            p.saveResource("Menus/MainMenu.yml", false);
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "Creating new file, MainMenu.yml!");
        }
        configuration = new YamlConfiguration();
        try {
            configuration.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfiguration() {
        return this.configuration;
    }

    public void reloadMenu() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    private void setDefults() {
        configuration.addDefault("MainMenu.Slots", 18);
        configuration.addDefault("MainMenu.Speed.Enabled", true);
        configuration.addDefault("MainMenu.Speed.Material", "SUGAR");
        configuration.addDefault("MainMenu.Speed.Data", 0);
        configuration.addDefault("MainMenu.Speed.Slot", 0);
        configuration.addDefault("MainMenu.Speed.Level", 1);
        configuration.addDefault("MainMenu.Speed.UsePermission", false);
        configuration.addDefault("MainMenu.Speed.Permission", "psettings.speed");
        configuration.addDefault("MainMenu.Slots", 18);
        configuration.addDefault("MainMenu.Chat.Enabled", true);
        configuration.addDefault("MainMenu.Chat.Material", "NAME_TAG");
        configuration.addDefault("MainMenu.Chat.Data", 0);
        configuration.addDefault("MainMenu.Chat.Slot", 1);
        configuration.addDefault("MainMenu.Chat.UsePermission", false);
        configuration.addDefault("MainMenu.Chat.Permission", "psettings.chat");
        configuration.addDefault("MainMenu.Jump.Enabled", true);
        configuration.addDefault("MainMenu.Jump.Material", "FEATHER");
        configuration.addDefault("MainMenu.Jump.Data", 0);
        configuration.addDefault("MainMenu.Jump.Slot", 2);
        configuration.addDefault("MainMenu.Jump.Level", 1);
        configuration.addDefault("MainMenu.Jump.UsePermission", false);
        configuration.addDefault("MainMenu.Jump.Permission", "psettings.jump");
        configuration.addDefault("MainMenu.Fly.Enabled", true);
        configuration.addDefault("MainMenu.Fly.Material", "DIAMOND_BOOTS");
        configuration.addDefault("MainMenu.Fly.Data", 0);
        configuration.addDefault("MainMenu.Fly.Slot", 3);
        configuration.addDefault("MainMenu.Fly.UsePermission", false);
        configuration.addDefault("MainMenu.Fly.Permission", "psettings.fly");
        configuration.addDefault("MainMenu.Blood.Enabled", true);
        configuration.addDefault("MainMenu.Blood.Material", "REDSTONE");
        configuration.addDefault("MainMenu.Blood.Data", 0);
        configuration.addDefault("MainMenu.Blood.Slot", 4);
        configuration.addDefault("MainMenu.Blood.UsePermission", false);
        configuration.addDefault("MainMenu.Blood.Permission", "psettings.blood");
        configuration.addDefault("MainMenu.Radio.Enabled", true);
        configuration.addDefault("MainMenu.Radio.Material", "JUKEBOX");
        configuration.addDefault("MainMenu.Radio.Data", 0);
        configuration.addDefault("MainMenu.Radio.Slot", 5);
        configuration.addDefault("MainMenu.Radio.UsePermission", false);
        configuration.addDefault("MainMenu.Radio.Permission", "psettings.radio");
        configuration.addDefault("MainMenu.Visibility.Enabled", true);
        configuration.addDefault("MainMenu.Visibility.Material", "WATCH");
        configuration.addDefault("MainMenu.Visibility.Data", 0);
        configuration.addDefault("MainMenu.Visibility.Slot", 6);
        configuration.addDefault("MainMenu.Visibility.UsePermission", false);
        configuration.addDefault("MainMenu.Visibility.Permission", "psettings.visibility");
        configuration.addDefault("MainMenu.DoubleJump.Enabled", true);
        configuration.addDefault("MainMenu.DoubleJump.Material", "JUKEBOX");
        configuration.addDefault("MainMenu.DoubleJump.Data", 0);
        configuration.addDefault("MainMenu.DoubleJump.Slot", 7);
        configuration.addDefault("MainMenu.DoubleJump.UsePermission", false);
        configuration.addDefault("MainMenu.DoubleJump.Permission", "psettings.doubleJump");
        configuration.addDefault("MainMenu.Stacker.Enabled", true);
        configuration.addDefault("MainMenu.Stacker.Material", "SADDLE");
        configuration.addDefault("MainMenu.Stacker.Data", 0);
        configuration.addDefault("MainMenu.Stacker.Slot", 8);
        configuration.addDefault("MainMenu.Stacker.UsePermission", false);
        configuration.addDefault("MainMenu.Stacker.Permission", "psettings.stacker");
        configuration.addDefault("Enabled.Material", "INK_SACK");
        configuration.addDefault("Enabled.Data", 10);
        configuration.addDefault("Disabled.Material", "INK_SACK");
        configuration.addDefault("Disabled.Data", 8);
        configuration.options().copyDefaults(true);

        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
