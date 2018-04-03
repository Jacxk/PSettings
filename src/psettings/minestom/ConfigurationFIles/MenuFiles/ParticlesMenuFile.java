package psettings.minestom.ConfigurationFIles.MenuFiles;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class ParticlesMenuFile {

    private File file;
    private FileConfiguration configuration;

    public void createMenuFile(Plugin p) {
        file = new File(p.getDataFolder() + "/Menus", "/ParticlesMenu.yml");

        if (!file.exists()) {
            p.saveResource("Menus/ParticlesMenu.yml", false);
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "Creating new file, ParticlesMenu.yml!");
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

}
