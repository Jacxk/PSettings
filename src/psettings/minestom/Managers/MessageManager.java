package psettings.minestom.Managers;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import psettings.minestom.PSettings;
import psettings.minestom.Utilities.MessageUtil;
import psettings.minestom.Utilities.Util;

import java.io.File;
import java.util.*;

public class MessageManager {

    private Util util;
    private Map<String, Map<String, String>> messages;
    private File[] files;
    private String defaultLocale;

    public MessageManager(Util util, PSettings plugin) {
        this.util = util;
        this.defaultLocale = plugin.getConfig().getString("Default-Language");
        this.messages = new HashMap<>();
    }

    public void sendLanguagesComponent(Player player) {
        int i = 0;
        TextComponent component = new TextComponent(MessageUtil.color(getString("Language.Current", player).replace(
                "{current}", util.getSettingsManager(player).getLanguage()) + "&e\n"));
        component.addExtra(MessageUtil.color(getString("Language.Available", player)));

        for (File file : files) {
            i++;
            if (!file.isDirectory() && file.getName().endsWith(".yml")) {
                String fileName = FilenameUtils.getBaseName(file.getName());
                Map<String, String> messages = this.messages.get(fileName);
                TextComponent textComponent = new TextComponent(MessageUtil.color("&e" + fileName));

                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(MessageUtil.color("&aTranslator: &7" + messages.get("Language.Translator")
                                + "\n&aLanguage: &7" + messages.get("Language.Locale")
                                + "\n\n&7Click here to change\n&7the language")).create()));
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lang " + fileName));


                component.addExtra(textComponent);
                if (i < files.length) component.addExtra(", ");
            }
        }
        player.spigot().sendMessage(component);
    }

    public List<String> availableLanguages() {
        List<String> languages = new ArrayList<>();
        for (File file : files)
            if (!file.isDirectory() && file.getName().endsWith(".yml"))
                languages.add(FilenameUtils.getBaseName(file.getName()));

        return languages;
    }

    public void loadLocales() {
        if (!messages.isEmpty()) {
            messages.clear();
        }
        for (File file : files) {
            if (!file.isDirectory() && file.getName().toLowerCase().endsWith(".yml")) {

                FileConfiguration localeFile = YamlConfiguration.loadConfiguration(file);
                String locale = FilenameUtils.getBaseName(file.getName().toLowerCase());
                ConfigurationSection configurationSection = localeFile.getConfigurationSection("");

                if (configurationSection == null) {
                    continue;
                }

                messages.put(locale, new HashMap<>());
                Set<String> keys = configurationSection.getKeys(false);
                for (String key : keys) {
                    saveKeyToLocale(localeFile, locale, key);
                }
            }
        }
    }

    private void saveKeyToLocale(FileConfiguration file, String locale, String path) {
        ConfigurationSection configurationSection = file.getConfigurationSection(path);

        if (configurationSection == null) {
            String message = file.getString(path);
            messages.get(locale).put(path, message);
            return;
        }

        Set<String> keys = configurationSection.getKeys(false);
        for (String key : keys) {
            saveKeyToLocale(file, locale, path + "." + key);
        }
    }

    public String getString(String messagePath, Player player, Player target) {
        String locale = util.getSettingsManager(player).getLanguage();
        Map<String, String> messages = getMessagesForLocale(locale);

        if (messages.containsKey(messagePath)) return messages.get(messagePath).replace("{player}",
                target == null ? player.getName() : target.getName()).replace("{player_displayname}",
                target == null ? player.getDisplayName() : target.getDisplayName());

        return MessageUtil.color("&7Your file is missing the path &c&l" + messagePath);
    }

    public int getInt(String messagePath, Player player) {
        return Integer.valueOf(getString(messagePath, player));
    }

    public boolean getBoolean(String messagePath, Player player) {
        return Boolean.valueOf(getString(messagePath, player));
    }

    public List<String> getStringList(String messagePath, Player player) {
        String listStr = getString(messagePath, player).replaceAll("(^\\[|\\]$)", "");
        return Arrays.asList(listStr.split("\\s*, \\s*"));
    }

    public String getString(String messagePath, CommandSender sender) {
        if (!(sender instanceof Player)) {
            Map<String, String> messages = getMessagesForLocale(defaultLocale);
            return messages.get(messagePath);
        }
        Player player = (Player) sender;
        String locale = util.getSettingsManager(player).getLanguage();
        Map<String, String> messages = getMessagesForLocale(locale);

        if (messages.containsKey(messagePath)) {
            return messages.get(messagePath);
        }

        return MessageUtil.color("&7Your file is missing the path &c&l" + messagePath);
    }

    private Map<String, String> getMessagesForLocale(String locale) {
        if (messages.containsKey(locale)) {
            return messages.get(locale);
        }
        return messages.get(defaultLocale);
    }

    public void setFiles(File[] files) {
        this.files = files;
    }
}
