package psettings.minestom.ConfigurationFIles.MenuFiles;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class VisibilityMenuFile {

    private File file;
    private FileConfiguration configuration;

    public void createMenuFile(Plugin p) {
        file = new File(p.getDataFolder() + "/Menus", "/VisibilityMenu.yml");

        if (!file.exists()) {
            p.saveResource("Menus/VisibilityMenu.yml", false);
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "Creating new file, VisibilityMenu.yml!");
        }
        configuration = new YamlConfiguration();
        try {
            configuration.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefaults();
    }

    public FileConfiguration getConfiguration() {
        return this.configuration;
    }

    public void reloadMenu() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    private void saveMenu() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save the VisibilityMenu.yml file!");
        }
    }

    private void setDefaults() {
        configuration.addDefault("VisibilityMenu.Slots", 18);
        configuration.addDefault("VisibilityMenu.Privileged.Enabled", true);
        configuration.addDefault("VisibilityMenu.Privileged.Material", "MAP");
        configuration.addDefault("VisibilityMenu.Privileged.Data", 0);
        configuration.addDefault("VisibilityMenu.Privileged.Slot", 2);
        configuration.addDefault("VisibilityMenu.Privileged.UsePermission", false);
        configuration.addDefault("VisibilityMenu.Privileged.Permission", "psettings.privileged");
        configuration.addDefault("VisibilityMenu.Friends.Enabled", true);
        configuration.addDefault("VisibilityMenu.Friends.Material", "BOOK_AND_QUILL");
        configuration.addDefault("VisibilityMenu.Friends.Data", 0);
        configuration.addDefault("VisibilityMenu.Friends.Slot", 4);
        configuration.addDefault("VisibilityMenu.Friends.UsePermission", false);
        configuration.addDefault("VisibilityMenu.Friends.Permission", "psettings.friends");
        configuration.addDefault("VisibilityMenu.Staff.Enabled", true);
        configuration.addDefault("VisibilityMenu.Staff.Material", "PAPER");
        configuration.addDefault("VisibilityMenu.Staff.Data", 0);
        configuration.addDefault("VisibilityMenu.Staff.Slot", 6);
        configuration.addDefault("VisibilityMenu.Staff.UsePermission", false);
        configuration.addDefault("VisibilityMenu.Staff.Permission", "psettings.staff");
        configuration.addDefault("Back.Material", "ARROW");
        configuration.addDefault("Back.Data", 0);
        configuration.addDefault("Back.Slot", 9);
        configuration.options().copyDefaults(true);
        saveMenu();
    }
}
