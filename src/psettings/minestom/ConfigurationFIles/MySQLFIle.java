package psettings.minestom.ConfigurationFIles;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class MySQLFIle {

    private File file;
    private FileConfiguration configuration;

    public void createMySQL(Plugin p) {
        file = new File(p.getDataFolder() + "/MySQL.yml");

        if (!file.exists()) {
            p.saveResource("MySQL.yml", false);
            Bukkit.getServer().getConsoleSender()
                    .sendMessage(ChatColor.GRAY + "Creating new file, MySQL.yml!");
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

    public void reloadMySQL() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }

}
