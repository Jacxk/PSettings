package psettings.minestom.DataBase;

import org.bukkit.entity.Player;
import psettings.minestom.PSettings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayersCreation {

    private PSettings plugin;

    public PlayersCreation(PSettings plugin) {
        this.plugin = plugin;
    }

    private synchronized boolean playerDoesNotExists(UUID uuid, String table) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM `" + table + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            boolean containsPlayer = results.next();
            statement.close();
            results.close();
            return !containsPlayer;
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public synchronized void mainCreatePlayer(Player player) {
        try {
            UUID uuid = player.getUniqueId();
            String mainTable = "psettings_main_menu";
            if (playerDoesNotExists(uuid, mainTable)) {
                PreparedStatement insert = plugin.getConnection().prepareStatement(
                        "INSERT INTO `" + mainTable + "` (UUID,NAME,FLY,VISIBILITY,JUMP,STACKER,SPEED,CHAT,DOUBLEJUMP,RADIO,BLOOD)" +
                                " VALUES (?,?,?,?,?,?,?,?,?,?,?);");
                insert.setString(1, uuid.toString());
                insert.setString(2, player.getName());
                insert.setBoolean(3, plugin.getConfig().getBoolean("JoinDefault.MainMenu.Fly"));
                insert.setBoolean(4, plugin.getConfig().getBoolean("JoinDefault.MainMenu.Visibility"));
                insert.setBoolean(5, plugin.getConfig().getBoolean("JoinDefault.MainMenu.Jump"));
                insert.setBoolean(6, plugin.getConfig().getBoolean("JoinDefault.MainMenu.Stacker"));
                insert.setBoolean(7, plugin.getConfig().getBoolean("JoinDefault.MainMenu.Speed"));
                insert.setBoolean(8, plugin.getConfig().getBoolean("JoinDefault.MainMenu.Chat"));
                insert.setBoolean(9, plugin.getConfig().getBoolean("JoinDefault.MainMenu.DoubleJump"));
                insert.setBoolean(10, plugin.getConfig().getBoolean("JoinDefault.MainMenu.Radio"));
                insert.setBoolean(11, plugin.getConfig().getBoolean("JoinDefault.MainMenu.Blood"));
                insert.execute();
                insert.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void chatCreatePlayer(Player player) {
        try {
            UUID uuid = player.getUniqueId();
            String chatTable = "psettings_chat_menu";
            if (playerDoesNotExists(uuid, chatTable)) {
                PreparedStatement insert = plugin.getConnection().prepareStatement(
                        "INSERT INTO `" + chatTable + "` (UUID,NAME,MENTION,FRIENDS,STAFF)" +
                                " VALUES (?,?,?,?,?);");
                insert.setString(1, uuid.toString());
                insert.setString(2, player.getName());
                insert.setBoolean(3, plugin.getConfig().getBoolean("JoinDefault.ChatMenu.Mention"));
                insert.setBoolean(4, plugin.getConfig().getBoolean("JoinDefault.ChatMenu.FriendsOnly"));
                insert.setBoolean(5, plugin.getConfig().getBoolean("JoinDefault.ChatMenu.StaffOnly"));
                insert.execute();
                insert.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void visCreatePlayer(Player player) {
        try {
            UUID uuid = player.getUniqueId();
            String visibilityTable = "psettings_visibility_menu";
            if (playerDoesNotExists(uuid, visibilityTable)) {
                PreparedStatement insert = plugin.getConnection().prepareStatement(
                        "INSERT INTO `" + visibilityTable + "` (UUID,NAME,PRIVILEGED,FRIENDS,STAFF)" +
                                " VALUES (?,?,?,?,?);");
                insert.setString(1, uuid.toString());
                insert.setString(2, player.getName());
                insert.setBoolean(3, plugin.getConfig().getBoolean("JoinDefault.VisibilityMenu.Privileged"));
                insert.setBoolean(4, plugin.getConfig().getBoolean("JoinDefault.VisibilityMenu.Friends"));
                insert.setBoolean(5, plugin.getConfig().getBoolean("JoinDefault.VisibilityMenu.Staff"));
                insert.execute();
                insert.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void pSelectCreatePlayer(Player player) {
        try {
            UUID uuid = player.getUniqueId();
            String particlesTable = "psettings_particles_menu";
            if (playerDoesNotExists(uuid, particlesTable)) {
                PreparedStatement insert = plugin.getConnection().prepareStatement(
                        "INSERT INTO `" + particlesTable + "` (UUID,NAME,PARTICLE)" +
                                " VALUES (?,?,?);");
                insert.setString(1, uuid.toString());
                insert.setString(2, player.getName());
                insert.setString(3, plugin.getConfig().getString("JoinDefault.ParticlesMenu.FirstParticle"));
                insert.execute();
                insert.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void otherSettingsCreatePlayer(Player player, String lang) {
        try {
            UUID uuid = player.getUniqueId();
            String otherSettings = "psettings_other_settings";
            if (playerDoesNotExists(uuid, otherSettings)) {
                PreparedStatement insert = plugin.getConnection().prepareStatement(
                        "INSERT INTO `" + otherSettings + "` (UUID,NAME,LANG,SPEED,JUMP,BLOODPARTICLE)" +
                                " VALUES (?,?,?,?,?,?);");
                insert.setString(1, uuid.toString());
                insert.setString(2, player.getName());
                insert.setString(3, lang);
                insert.setInt(4, 1);
                insert.setInt(5, 2);
                insert.setString(6, "REDSTONE");
                insert.execute();
                insert.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
