package psettings.minestom.DataBase;

import org.bukkit.Bukkit;
import psettings.minestom.PSettings;
import psettings.minestom.Utilities.MessageUtil;
import psettings.minestom.Utilities.Util;

import java.sql.SQLException;

public class TableCreation {

    private PSettings plugin;

    public TableCreation(PSettings plugin) {
        this.plugin = plugin;
    }

    private void executeStatement(String statement, String table) {
        try {
            if (plugin.getConnection() != null && !plugin.getConnection().isClosed()) {
                plugin.getConnection().createStatement().execute(statement);
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(MessageUtil.color("&cCould not create table " + table + " report this to the developer"));
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
        }
    }

    public void createSettingsTable() {
        String table = "psettings_other_settings";
        String sql = "CREATE TABLE IF NOT EXISTS " + table + " ("
                + "	UUID text,"
                + "	NAME text,"
                + "	LANG text,"
                + " SPEED int,"
                + " JUMP int,"
                + " BLOODPARTICLE text"
                + ");";
        executeStatement(sql, table);
    }

    public void createParticlesTable() {
        String table = "psettings_particles_menu";
        String sql = "CREATE TABLE IF NOT EXISTS " + table + " ("
                + "	UUID text,"
                + "	NAME text,"
                + "	PARTICLE text"
                + ");";
        executeStatement(sql, table);
    }

    public void createMainTable() {
        String table = "psettings_main_menu";
        String sql = "CREATE TABLE IF NOT EXISTS " + table + " ("
                + "	UUID text,"
                + "	NAME text,"
                + "	SPEED boolean,"
                + "	JUMP boolean,"
                + "	FLY boolean,"
                + "	VISIBILITY boolean,"
                + "	STACKER boolean,"
                + "	CHAT boolean,"
                + "	DOUBLEJUMP boolean,"
                + "	RADIO boolean,"
                + "	BLOOD boolean"
                + ");";
        executeStatement(sql, table);
    }

    public void createChatTable() {
        String table = "psettings_chat_menu";
        String sql = "CREATE TABLE IF NOT EXISTS " + table + " ("
                + "	UUID text,"
                + "	NAME text,"
                + "	MENTION boolean,"
                + "	FRIENDS boolean,"
                + "	STAFF boolean"
                + ");";
        executeStatement(sql, table);
    }

    public void createVisTable() {
        String table = "psettings_visibility_menu";
        String sql = "CREATE TABLE IF NOT EXISTS " + table + " ("
                + "	UUID text,"
                + "	NAME text,"
                + "	PRIVILEGED boolean,"
                + "	FRIENDS boolean,"
                + "	STAFF boolean"
                + ");";
        executeStatement(sql, table);
    }
}
