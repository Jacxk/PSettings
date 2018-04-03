package psettings.minestom.Utilities;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import psettings.minestom.Commands.LanguagesCommand;
import psettings.minestom.Commands.Menus.ChatMenuCommand;
import psettings.minestom.Commands.Menus.MainMenuCommand;
import psettings.minestom.Commands.Menus.ParticleMenuCommand;
import psettings.minestom.Commands.Menus.VisibilityMenuCommand;
import psettings.minestom.Commands.Options.*;
import psettings.minestom.ConfigurationFIles.MenuFiles.ChatMenuFile;
import psettings.minestom.ConfigurationFIles.MenuFiles.MainMenuFile;
import psettings.minestom.ConfigurationFIles.MenuFiles.ParticlesMenuFile;
import psettings.minestom.ConfigurationFIles.MenuFiles.VisibilityMenuFile;
import psettings.minestom.ConfigurationFIles.MySQLFIle;
import psettings.minestom.DataBase.MySQLManager;
import psettings.minestom.DataBase.PlayersCreation;
import psettings.minestom.Managers.MessageManager;
import psettings.minestom.PSettings;
import psettings.minestom.Settings.Listeners.*;
import psettings.minestom.Settings.MenuCreator;
import psettings.minestom.Settings.MenuListener.ChatMenu;
import psettings.minestom.Settings.MenuListener.MainMenu;
import psettings.minestom.Settings.MenuListener.ParticleMenu;
import psettings.minestom.Settings.MenuListener.VisibilityMenu;
import psettings.minestom.Updater.Update;
import psettings.minestom.hooks.PartyAndFriends;
import psettings.minestom.hooks.icJukeBox;

import java.io.File;

public class SetupPlugin {

    private PSettings plugin;
    private MenuCreator menuCreator;
    private ParticlesMenuFile particlesMenuFile;
    private MessageManager messageManager;
    private VisibilityMenuFile visibilityMenuFile;
    private PlayerItems playerItems;
    private MainMenuFile mainMenuFile;
    private MySQLFIle mySQLFIle;
    private MySQLManager mySQLManager;
    private PlayersCreation playersCreation;
    private icJukeBox icJukeBox;
    private PartyAndFriends partyAndFriends;
    private Update updater;

    private UtilEffects utilEffects;
    private ChatMenuFile chatMenuFile;
    private Util util;

    public SetupPlugin(PSettings plugin, MenuCreator menuCreator, ParticlesMenuFile particlesMenuFile,
                       MessageManager messageManager, VisibilityMenuFile visibilityMenuFile, PlayerItems playerItems,
                       MainMenuFile mainMenuFile, MySQLFIle mySQLFIle, MySQLManager mySQLManager,
                       PlayersCreation playersCreation, psettings.minestom.hooks.icJukeBox icJukeBox,
                       PartyAndFriends partyAndFriends, Update updater, UtilEffects utilEffects, ChatMenuFile chatMenuFile, Util util) {
        this.plugin = plugin;
        this.menuCreator = menuCreator;
        this.particlesMenuFile = particlesMenuFile;
        this.messageManager = messageManager;
        this.visibilityMenuFile = visibilityMenuFile;
        this.playerItems = playerItems;
        this.mainMenuFile = mainMenuFile;
        this.mySQLFIle = mySQLFIle;
        this.mySQLManager = mySQLManager;
        this.playersCreation = playersCreation;
        this.icJukeBox = icJukeBox;
        this.partyAndFriends = partyAndFriends;
        this.updater = updater;
        this.utilEffects = utilEffects;
        this.chatMenuFile = chatMenuFile;
        this.util = util;
    }

    public void setupListener() {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new PlayerItemListener(plugin, menuCreator, util, playerItems, utilEffects, messageManager), plugin);
        pm.registerEvents(new MainMenu(plugin, utilEffects, menuCreator, mainMenuFile, playerItems, messageManager, icJukeBox, util), plugin);
        pm.registerEvents(new AddEffects(plugin, utilEffects, util, playerItems), plugin);
        pm.registerEvents(new StackerListener(plugin, messageManager, mainMenuFile, util), plugin);
        pm.registerEvents(new ChatMenu(plugin, menuCreator, chatMenuFile, mainMenuFile, messageManager, partyAndFriends, util), plugin);
        pm.registerEvents(new VisibilityMenu(plugin, visibilityMenuFile, menuCreator, mainMenuFile, messageManager, partyAndFriends, util), plugin);
        pm.registerEvents(new PlayerApplySetting(mySQLManager, playersCreation, updater, plugin, playerItems, utilEffects, icJukeBox, util), plugin);
        pm.registerEvents(new ParticleMenu(plugin, menuCreator, particlesMenuFile, messageManager, util), plugin);
        pm.registerEvents(new DoubleJumpListener(messageManager, mainMenuFile, util), plugin);
        pm.registerEvents(new ChatListener(plugin, util, messageManager, partyAndFriends), plugin);
        pm.registerEvents(new BloodListener(util), plugin);
    }

    public void setupCustomCommands() {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new LanguagesCommand(plugin, util, messageManager, playerItems), plugin);
        pm.registerEvents(new SpeedCommand(plugin, util, utilEffects, messageManager), plugin);
        pm.registerEvents(new ChatCommand(plugin, util, messageManager), plugin);
        pm.registerEvents(new FlyCommand(plugin, util, messageManager), plugin);
        pm.registerEvents(new StackerCommand(plugin, util, messageManager), plugin);
        pm.registerEvents(new JumpCommand(plugin, util, utilEffects, messageManager), plugin);
        pm.registerEvents(new RadioCommand(plugin, util, messageManager, icJukeBox), plugin);
        pm.registerEvents(new DoubleJumpCommand(plugin, util, messageManager), plugin);
        pm.registerEvents(new VisibilityCommand(plugin, util, utilEffects, messageManager), plugin);
        pm.registerEvents(new ParticleMenuCommand(plugin, menuCreator, messageManager), plugin);
        pm.registerEvents(new MainMenuCommand(plugin, menuCreator, messageManager), plugin);
        pm.registerEvents(new ChatMenuCommand(plugin, menuCreator, messageManager), plugin);
        pm.registerEvents(new VisibilityMenuCommand(plugin, menuCreator, messageManager), plugin);
    }

    public void createFiles() {
        if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
            plugin.saveDefaultConfig();
            plugin.getServer().getConsoleSender().sendMessage(MessageUtil.color("&7Creating new file, config.yml!"));
        }
        mainMenuFile.createMenuFile(plugin);
        mySQLFIle.createMySQL(plugin);
        visibilityMenuFile.createMenuFile(plugin);
        chatMenuFile.createMenuFile(plugin);
        particlesMenuFile.createMenuFile(plugin);
        createLangFiles();
    }

    private void createLangFiles() {
        File english = new File(plugin.getDataFolder() + "/Languages/en_us.yml");
        File spanish = new File(plugin.getDataFolder() + "/Languages/es_es.yml");
        if (!english.exists()) {
            plugin.saveResource("Languages/en_us.yml", false);
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "Creating new language file, en_us.yml!");
        }
        if (!spanish.exists()) {
            plugin.saveResource("Languages/es_es.yml", false);
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.GRAY + "Creating new language file, es_es.yml!");
        }
    }

    public void loadDependencies(ConsoleCommandSender log) {
        if (icJukeBox.isJukeEnabled()) log.sendMessage(MessageUtil.color("&7icJukeBox found enabling the radio setting."));
        else log.sendMessage(MessageUtil.color("&7icJukeBox not found disabling the radio setting."));

        if (partyAndFriends.isPAFEnabled() && plugin.getConfig().getBoolean("PartyAndFriends-Hook"))
            log.sendMessage(MessageUtil.color("&7PartyAndFriends-Hook found enabling hook."));
        else if (plugin.getConfig().getBoolean("PartyAndFriends-Hook")) {
            log.sendMessage(MessageUtil.color("&7PartyAndFriends-Hook not found disabling hook."));
        }
    }
}
