package psettings.minestom;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import psettings.minestom.Commands.PSettingsCommand;
import psettings.minestom.ConfigurationFIles.MenuFiles.ChatMenuFile;
import psettings.minestom.ConfigurationFIles.MenuFiles.MainMenuFile;
import psettings.minestom.ConfigurationFIles.MenuFiles.ParticlesMenuFile;
import psettings.minestom.ConfigurationFIles.MenuFiles.VisibilityMenuFile;
import psettings.minestom.ConfigurationFIles.MySQLFIle;
import psettings.minestom.DataBase.MySQLManager;
import psettings.minestom.DataBase.PlayersCreation;
import psettings.minestom.DataBase.TableCreation;
import psettings.minestom.Managers.AutoReconnectSQL;
import psettings.minestom.Managers.MessageManager;
import psettings.minestom.Managers.SettingsManager;
import psettings.minestom.NMS.NMS;
import psettings.minestom.NMS.v_1_10.v_1_10_R1;
import psettings.minestom.NMS.v_1_11.v_1_11_R1;
import psettings.minestom.NMS.v_1_12.v_1_12_R1;
import psettings.minestom.NMS.v_1_8.v_1_8_R1;
import psettings.minestom.NMS.v_1_8.v_1_8_R2;
import psettings.minestom.NMS.v_1_8.v_1_8_R3;
import psettings.minestom.NMS.v_1_9.v_1_9_R1;
import psettings.minestom.NMS.v_1_9.v_1_9_R2;
import psettings.minestom.Settings.MenuCreator;
import psettings.minestom.Updater.Update;
import psettings.minestom.Utilities.*;
import psettings.minestom.hooks.PartyAndFriends;
import psettings.minestom.hooks.icJukeBox;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PSettings extends JavaPlugin {

    public static String prefix;
    private ConsoleCommandSender log = Bukkit.getServer().getConsoleSender();
    private MenuCreator menuCreator;
    private ParticlesMenuFile particlesMenuFile;
    private MessageManager messageManager;
    private VisibilityMenuFile visibilityMenuFile;
    private PlayerItems playerItems;
    private MySQLFIle mySQLFIle;
    private UtilEffects utilEffects;
    private ChatMenuFile chatMenuFile;
    private MainMenuFile mainMenuFile;
    private MySQLManager mySQLManager;
    private PlayersCreation playersCreation;
    private Update updater;
    private psettings.minestom.hooks.icJukeBox icJukeBox;
    private PartyAndFriends partyAndFriends;
    private Connection connection;
    private Util util;
    private NMS nms;

    public Connection getConnection() {
        return connection;
    }

    private void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Util getUtil() {
        return util;
    }

    @Override
    public void onEnable() {
        initializeClasses();
        SetupPlugin setupPlugin = new SetupPlugin(this, menuCreator, particlesMenuFile, messageManager,
                visibilityMenuFile, playerItems, mainMenuFile, mySQLFIle, mySQLManager, playersCreation, icJukeBox, partyAndFriends, updater, utilEffects, chatMenuFile, util);
        getCommand("psettings").setExecutor(new PSettingsCommand(menuCreator, this, mainMenuFile, visibilityMenuFile,
                particlesMenuFile, chatMenuFile, messageManager, playerItems, util, updater));
        log.sendMessage(MessageUtil.color("&8&m----------------------------------------"));
        log.sendMessage(MessageUtil.color("                &c&lPSettings"));
        log.sendMessage(MessageUtil.color(""));
        log.sendMessage(MessageUtil.color("&7Version: &e" + getDescription().getVersion()));
        log.sendMessage(MessageUtil.color("&7Author: &e" + getDescription().getAuthors().get(0)));
        setupPlugin.createFiles();
        setupPlugin.loadDependencies(log);
        setupPlugin.setupCustomCommands();
        setupPlugin.setupListener();
        if (!mysqlSetup()) return;
        if (setupNMS()) {
            log.sendMessage(MessageUtil.color("&7Actionbar, Titles, Subtitles and Particles support was successful!"));
        } else {
            log.sendMessage(MessageUtil.color("&cFailed to setup Actionbar, Titles, Subtitles and Particles support!"));
            log.sendMessage(MessageUtil.color("&cYour server version is not compatible with this plugin!"));
            log.sendMessage(MessageUtil.color("&cThe plugin will not function correctly!"));
        }
        updater.sendUpdateMessage();
        log.sendMessage(MessageUtil.color(""));
        log.sendMessage(MessageUtil.color("&8&m----------------------------------------"));
        for (Player player : Bukkit.getOnlinePlayers()) {
            SettingsManager settings = new SettingsManager();
            util.importData(settings, player.getUniqueId());
            util.addPlayerSetting(player, settings);
        }
        messageManager.setFiles(new File(getDataFolder() + "/Languages").listFiles());
        messageManager.loadLocales();

        prefix = getConfig().getString("Prefix");
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory().equals(menuCreator.getMainMenu()) || player.getOpenInventory().equals(menuCreator.getChatMenu())
                    || player.getOpenInventory().equals(menuCreator.getVisibilityMenu()) || player.getOpenInventory().equals(menuCreator.getParticleSelector())) {
                player.closeInventory();
            }
            SettingsManager settings = util.getSettingsManager(player);
            mySQLManager.updateSettings(player.getUniqueId(), settings);
        }

        try {
            if (getConnection() != null && !getConnection().isClosed()) getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        log.sendMessage(MessageUtil.color("&8&m----------------------------------------"));
        log.sendMessage(MessageUtil.color("                &c&lPSettings"));
        log.sendMessage(MessageUtil.color(""));
        log.sendMessage(MessageUtil.color("&7Disabling &cPSettings"));
        log.sendMessage(MessageUtil.color("&7Version: &e" + getDescription().getVersion()));
        log.sendMessage(MessageUtil.color("&7Author: &e" + getDescription().getAuthors().get(0)));
        log.sendMessage(MessageUtil.color(""));
        log.sendMessage(MessageUtil.color("&8&m----------------------------------------"));
    }

    private void initializeClasses() {
        this.icJukeBox = new icJukeBox();
        this.partyAndFriends = new PartyAndFriends(this);
        this.mySQLManager = new MySQLManager(this);
        this.mainMenuFile = new MainMenuFile();
        this.mySQLFIle = new MySQLFIle();
        this.chatMenuFile = new ChatMenuFile();
        this.visibilityMenuFile = new VisibilityMenuFile();
        this.particlesMenuFile = new ParticlesMenuFile();
        this.util = new Util(this, mainMenuFile, mySQLManager);
        this.messageManager = new MessageManager(util, this);
        this.menuCreator = new MenuCreator(mainMenuFile, visibilityMenuFile, chatMenuFile, particlesMenuFile, messageManager, icJukeBox, util);
        this.playerItems = new PlayerItems(this, util, messageManager);
        this.utilEffects = new UtilEffects(mainMenuFile, partyAndFriends, util);
        this.playersCreation = new PlayersCreation(this);
        this.updater = new Update(this);
    }

    private boolean setupNMS() {
        String version;
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException exception) {
            return false;
        }

        log.sendMessage(MessageUtil.color("&7Your server is running version " + version));

        switch (version) {
            case "v1_8_R1":
                nms = new v_1_8_R1();
                break;
            case "v1_8_R2":
                nms = new v_1_8_R2();
                break;
            case "v1_8_R3":
                nms = new v_1_8_R3();
                break;
            case "v1_9_R1":
                nms = new v_1_9_R1();
                break;
            case "v1_9_R2":
                nms = new v_1_9_R2();
                break;
            case "v1_10_R1":
                nms = new v_1_10_R1();
                break;
            case "v1_11_R1":
                nms = new v_1_11_R1();
                break;
            case "v1_12_R1":
                nms = new v_1_12_R1();
                break;
        }
        return nms != null;
    }

    public NMS getNMS() {
        return nms;
    }

    public boolean mysqlSetup() {
        TableCreation tableCreation = new TableCreation(this);
        String host = mySQLFIle.getConfiguration().getString("MySQL.Host");
        int port = mySQLFIle.getConfiguration().getInt("MySQL.Port");
        String database = mySQLFIle.getConfiguration().getString("MySQL.Database");
        String username = mySQLFIle.getConfiguration().getString("MySQL.Username");
        String password = mySQLFIle.getConfiguration().getString("MySQL.Password");

        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return false;
                }
                if (mySQLFIle.getConfiguration().getString("Type").equalsIgnoreCase("sqlite")) {
                    Class.forName("org.sqlite.JDBC");
                    String url = "jdbc:sqlite:" + getDataFolder() + "/SQLite.db";
                    setConnection(DriverManager.getConnection(url));
                    log.sendMessage(MessageUtil.color("&7Successfully connected to &cSQLite &7database."));
                } else if (mySQLFIle.getConfiguration().getString("Type").equalsIgnoreCase("mysql")) {
                    int period = mySQLFIle.getConfiguration().getInt("MySQL.AutoReconnectTime") * 60;
                    new AutoReconnectSQL(this).runTaskTimer(this, period * 20L, period * 20L);
                    Class.forName("com.mysql.jdbc.Driver");
                    setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password));
                    log.sendMessage(MessageUtil.color("&7Successfully connected to &cMySQL &7database."));
                } else {
                    log.sendMessage(MessageUtil.color("&4Could not connect to any database. Please use sqlite or mysql in your MySQL.yml file."));
                    Bukkit.getScheduler().cancelTasks(this);
                    Bukkit.getPluginManager().disablePlugin(this);
                    return false;
                }
                tableCreation.createMainTable();
                tableCreation.createChatTable();
                tableCreation.createVisTable();
                tableCreation.createParticlesTable();
                tableCreation.createSettingsTable();
                return true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            log.sendMessage(MessageUtil.color("&cCould not connect to MySQL database, check yor credentials."));
            log.sendMessage(e.getMessage());
            Bukkit.getScheduler().cancelTasks(this);
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
    }
}
