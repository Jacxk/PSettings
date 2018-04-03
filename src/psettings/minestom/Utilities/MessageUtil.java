package psettings.minestom.Utilities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import psettings.minestom.PSettings;

import java.util.ArrayList;
import java.util.List;

public class MessageUtil {

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> colorList(List<String> list) {
        List<String> coloredList = new ArrayList<>();
        list.forEach(string -> coloredList.add(color(string)));
        return coloredList;
    }

    public static void message(Player player, String string) {
        String prefix = ChatColor.translateAlternateColorCodes('&', PSettings.prefix);
        if (prefix.equalsIgnoreCase("none")) player.sendMessage(color(string.replace("{player}",
                player.getName()).replace("{player_displayname}", player.getDisplayName())));
        else player.sendMessage(prefix + color(string));
    }

    public static void message(CommandSender sender, String string) {
        String prefix = ChatColor.translateAlternateColorCodes('&', PSettings.prefix);
        if (prefix.equalsIgnoreCase("none")) sender.sendMessage(color(string.replace("{player}", sender.getName())));
        else sender.sendMessage(prefix + color(string));
    }

}
