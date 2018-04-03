package psettings.minestom.DataBase;

import psettings.minestom.Managers.SettingsManager;
import psettings.minestom.PSettings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySQLManager {
    private PSettings plugin;
    private String mainTable = "psettings_main_menu";
    private String chatTable = "psettings_chat_menu";
    private String visibilityTable = "psettings_visibility_menu";
    private String particlesTable = "psettings_particles_menu";
    private String otherSettingsTable = "psettings_other_settings";

    public MySQLManager(PSettings plugin) {
        this.plugin = plugin;
    }

    public void updateSettings(UUID uuid, SettingsManager settings) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE `" + mainTable
                    + "` SET NAME=?, FLY=?, JUMP=?, RADIO=?, VISIBILITY=?, STACKER=?, DOUBLEJUMP=?, CHAT=?, SPEED=?, BLOOD=? WHERE UUID=?;");
            PreparedStatement statement1 = plugin.getConnection().prepareStatement("UPDATE `" + chatTable
                    + "` SET NAME=?, MENTION=?, FRIENDS=?, STAFF=? WHERE UUID=?;");
            PreparedStatement statement2 = plugin.getConnection().prepareStatement("UPDATE `" + visibilityTable
                    + "` SET NAME=?, PRIVILEGED=?, FRIENDS=?, STAFF=? WHERE UUID=?;");
            PreparedStatement statement3 = plugin.getConnection().prepareStatement("UPDATE `" + particlesTable
                    + "` SET NAME=?, PARTICLE=? WHERE UUID=?;");

            PreparedStatement statement4 = plugin.getConnection().prepareStatement("UPDATE `" + otherSettingsTable
                    + "` SET NAME=?, LANG=?, SPEED=?, JUMP=?, BLOODPARTICLE=? WHERE UUID=?;");

            statement.setString(1, settings.getPlayerName());
            statement.setBoolean(2, settings.isFlyEnabled());
            statement.setBoolean(3, settings.isJumpEnabled());
            statement.setBoolean(4, settings.isRadioEnabled());
            statement.setBoolean(5, settings.isVisibilityEnabled());
            statement.setBoolean(6, settings.isStackerEnabled());
            statement.setBoolean(7, settings.isDoubleJumpEnabled());
            statement.setBoolean(8, settings.isChatEnabled());
            statement.setBoolean(9, settings.isSpeedEnabled());
            statement.setBoolean(10, settings.isBloodEnabled());
            statement.setString(11, uuid.toString());

            statement1.setString(1, settings.getPlayerName());
            statement1.setBoolean(2, settings.isMentionEnabled());
            statement1.setBoolean(3, settings.isFriendsChatOnlyEnabled());
            statement1.setBoolean(4, settings.isStaffChatOnlyEnabled());
            statement1.setString(5, uuid.toString());

            statement2.setString(1, settings.getPlayerName());
            statement2.setBoolean(2, settings.isVipVisOnlyEnabled());
            statement2.setBoolean(3, settings.isFriendsVisOnlyEnabled());
            statement2.setBoolean(4, settings.isStaffVisOnlyEnabled());
            statement2.setString(5, uuid.toString());

            statement3.setString(1, settings.getPlayerName());
            statement3.setString(2, settings.getParticle());
            statement3.setString(3, uuid.toString());

            statement4.setString(1, settings.getPlayerName());
            statement4.setString(2, settings.getLanguage());
            statement4.setInt(3, settings.getSpeedAmplifier());
            statement4.setInt(4, settings.getJumpAmplifier());
            statement4.setString(5, settings.getBloodParticle());
            statement4.setString(6, uuid.toString());

            statement.executeUpdate();
            statement1.executeUpdate();
            statement2.executeUpdate();
            statement3.executeUpdate();
            statement4.executeUpdate();
            statement.close();
            statement1.close();
            statement2.close();
            statement3.close();
            statement4.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getParticle(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("SELECT PARTICLE FROM `" + particlesTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getLanguage(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("SELECT LANG FROM `" + otherSettingsTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getBloodParticle(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("SELECT BLOODPARTICLE FROM `" + otherSettingsTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getSpeedAmplifier(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("SELECT SPEED FROM `" + otherSettingsTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getJumpAmplifier(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("SELECT JUMP FROM `" + otherSettingsTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void updateFly(UUID uuid, boolean b) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("UPDATE `" + mainTable + "` SET FLY=? WHERE UUID=?;");
            statement.setBoolean(1, b);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getFly(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("SELECT FLY FROM `" + mainTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateSpeed(UUID uuid, boolean b) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("UPDATE `" + mainTable + "` SET SPEED=? WHERE UUID=?;");
            statement.setBoolean(1, b);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getSpeed(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT SPEED FROM `" + mainTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateJump(UUID uuid, boolean b) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE `" + mainTable + "` SET JUMP=? WHERE UUID=?;");
            statement.setBoolean(1, b);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getJump(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT JUMP FROM `" + mainTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateStacker(UUID uuid, boolean b) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE `" + mainTable + "` SET STACKER=? WHERE UUID=?;");
            statement.setBoolean(1, b);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getStacker(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT STACKER FROM `" + mainTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateVisibility(UUID uuid, boolean b) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE `" + mainTable + "` SET VISIBILITY=? WHERE UUID=?;");
            statement.setBoolean(1, b);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getVisibility(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT VISIBILITY FROM `" + mainTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateChat(UUID uuid, boolean b) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE `" + mainTable + "` SET CHAT=? WHERE UUID=?;");
            statement.setBoolean(1, b);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getChat(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT CHAT FROM `" + mainTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateDoubleJump(UUID uuid, boolean b) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE `" + mainTable + "` SET DOUBLEJUMP=? WHERE UUID=?;");
            statement.setBoolean(1, b);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getDoubleJump(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT DOUBLEJUMP FROM `" + mainTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateRadio(UUID uuid, boolean b) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE `" + mainTable + "` SET RADIO=? WHERE UUID=?;");
            statement.setBoolean(1, b);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getRadio(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT RADIO FROM `" + mainTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateMention(UUID uuid, boolean b) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE `" + chatTable + "` SET MENTION=? WHERE UUID=?;");
            statement.setBoolean(1, b);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getMention(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT MENTION FROM `" + chatTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateFriendsChatOnly(UUID uuid, boolean b) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE `" + chatTable + "` SET FRIENDS=? WHERE UUID=?;");
            statement.setBoolean(1, b);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getFriendsChatOnly(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT FRIENDS FROM `" + chatTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateStaffChatOnly(UUID uuid, boolean b) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE `" + chatTable + "` SET STAFF=? WHERE UUID=?;");
            statement.setBoolean(1, b);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getStaffChatOnly(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT STAFF FROM `" + chatTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateFriendsVisOnly(UUID uuid, boolean b) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE `" + visibilityTable + "` SET FRIENDS=? WHERE UUID=?;");
            statement.setBoolean(1, b);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getFriendsVisOnly(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT FRIENDS FROM `" + visibilityTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateStaffVisOnly(UUID uuid, boolean b) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE `" + visibilityTable + "` SET STAFF=? WHERE UUID=?;");
            statement.setBoolean(1, b);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getStaffVisOnly(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT STAFF FROM `" + visibilityTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateVipVisOnly(UUID uuid, boolean b) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE `" + visibilityTable + "` SET PRIVILEGED=? WHERE UUID=?;");
            statement.setBoolean(1, b);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getVipVisOnly(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT PRIVILEGED FROM `" + visibilityTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean getBlood(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT BLOOD FROM `" + mainTable + "` WHERE UUID=?;");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
