package psettings.minestom.ConfigurationFIles.MenuFiles;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class ChatMenuFile {

    private File file;
    private FileConfiguration configuration;

    public void createMenuFile(Plugin p) {
        file = new File(p.getDataFolder() + "/Menus", "/ChatMenu.yml");

        if (!file.exists()) {
            p.saveResource("Menus/ChatMenu.yml", false);
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "Creating new file, ChatMenu.yml!");
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

    private void setDefaults() {
        configuration.addDefault("ChatMenu.Slots", 18);
        configuration.addDefault("ChatMenu.Mention.Enabled", true);
        configuration.addDefault("ChatMenu.Mention.Material", "MAP");
        configuration.addDefault("ChatMenu.Mention.Data", 0);
        configuration.addDefault("ChatMenu.Mention.Slot", 2);
        configuration.addDefault("ChatMenu.Mention.UsePermission", false);
        configuration.addDefault("ChatMenu.Mention.Permission", "psettings.mention");
        configuration.addDefault("ChatMenu.FriendsOnly.Enabled", true);
        configuration.addDefault("ChatMenu.FriendsOnly.Material", "BOOK_AND_QUILL");
        configuration.addDefault("ChatMenu.FriendsOnly.Data", 0);
        configuration.addDefault("ChatMenu.FriendsOnly.Slot", 4);
        configuration.addDefault("ChatMenu.FriendsOnly.UsePermission", false);
        configuration.addDefault("ChatMenu.FriendsOnly.Permission", "psettings.friendsonly");
        configuration.addDefault("ChatMenu.StaffOnly.Enabled", true);
        configuration.addDefault("ChatMenu.StaffOnly.Material", "BOOK_AND_QUILL");
        configuration.addDefault("ChatMenu.StaffOnly.Data", 0);
        configuration.addDefault("ChatMenu.StaffOnly.Slot", 4);
        configuration.addDefault("ChatMenu.StaffOnly.UsePermission", false);
        configuration.addDefault("ChatMenu.StaffOnly.Permission", "psettings.friendsonly");
        configuration.addDefault("Back.Material", "ARROW");
        configuration.addDefault("Back.Data", 0);
        configuration.addDefault("Back.Slot", 9);
        configuration.options().copyDefaults(true);

        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
