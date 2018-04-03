package psettings.minestom.Managers;

import org.bukkit.entity.Player;

public class SettingsManager {

    private String playerName = null;
    private String particle = null;
    private String bloodParticle = null;
    private String language = null;
    private int speedAmplifier = 0;
    private int jumpAmplifier = 0;
    private boolean speedEnabled = false;
    private boolean jumpEnabled = false;
    private boolean bloodEnabled = false;
    private boolean flyEnabled = false;
    private boolean doubleJumpEnabled = false;
    private boolean stackerEnabled = false;
    private boolean visibilityEnabled = false;
    private boolean radioEnabled = false;
    private boolean chatEnabled = false;
    private boolean mentionEnabled = false;
    private boolean friendsChatOnlyEnabled = false;
    private boolean staffChatOnlyEnabled = false;
    private boolean friendsVisOnlyEnabled = false;
    private boolean staffVisOnlyEnabled = false;
    private boolean vipVisOnlyEnabled = false;
    private boolean stacked = false;
    private Player playerStacked = null;

    public boolean isStacked() {
        return stacked;
    }

    public void setStacked(boolean stacked) {
        this.stacked = stacked;
    }

    public Player getPlayerStacked() {
        return playerStacked;
    }

    public void setPlayerStacked(Player playerStacked) {
        this.playerStacked = playerStacked;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getBloodParticle() {
        return bloodParticle;
    }

    public void setBloodParticle(String bloodParticle) {
        this.bloodParticle = bloodParticle;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getSpeedAmplifier() {
        return speedAmplifier;
    }

    public void setSpeedAmplifier(int speedAmplifier) {
        this.speedAmplifier = speedAmplifier;
    }

    public int getJumpAmplifier() {
        return jumpAmplifier;
    }

    public void setJumpAmplifier(int jumpAmplifier) {
        this.jumpAmplifier = jumpAmplifier;
    }

    public String getParticle() {
        return particle;
    }

    public boolean isBloodEnabled() {
        return bloodEnabled;
    }

    public void setBloodEnabled(boolean bloodEnabled) {
        this.bloodEnabled = bloodEnabled;
    }

    public void setParticle(String particle) {
        this.particle = particle;
    }

    public boolean isFriendsChatOnlyEnabled() {
        return friendsChatOnlyEnabled;
    }

    public boolean isFriendsVisOnlyEnabled() {
        return friendsVisOnlyEnabled;
    }

    public boolean isMentionEnabled() {
        return mentionEnabled;
    }

    public boolean isStaffChatOnlyEnabled() {
        return staffChatOnlyEnabled;
    }

    public boolean isStaffVisOnlyEnabled() {
        return staffVisOnlyEnabled;
    }

    public boolean isVipVisOnlyEnabled() {
        return vipVisOnlyEnabled;
    }

    public void setFriendsChatOnlyEnabled(boolean friendsChatOnlyEnabled) {
        this.friendsChatOnlyEnabled = friendsChatOnlyEnabled;
    }

    public void setFriendsVisOnlyEnabled(boolean friendsVisOnlyEnabled) {
        this.friendsVisOnlyEnabled = friendsVisOnlyEnabled;
    }

    public void setMentionEnabled(boolean mentionEnabled) {
        this.mentionEnabled = mentionEnabled;
    }

    public void setStaffChatOnlyEnabled(boolean staffChatOnlyEnabled) {
        this.staffChatOnlyEnabled = staffChatOnlyEnabled;
    }

    public void setStaffVisOnlyEnabled(boolean staffVisOnlyEnabled) {
        this.staffVisOnlyEnabled = staffVisOnlyEnabled;
    }

    public void setVipVisOnlyEnabled(boolean vipVisOnlyEnabled) {
        this.vipVisOnlyEnabled = vipVisOnlyEnabled;
    }

    public boolean isChatEnabled() {
        return chatEnabled;
    }

    public boolean isDoubleJumpEnabled() {
        return doubleJumpEnabled;
    }

    public boolean isFlyEnabled() {
        return flyEnabled;
    }

    public boolean isJumpEnabled() {
        return jumpEnabled;
    }

    public boolean isRadioEnabled() {
        return radioEnabled;
    }

    public boolean isSpeedEnabled() {
        return speedEnabled;
    }

    public boolean isStackerEnabled() {
        return stackerEnabled;
    }

    public boolean isVisibilityEnabled() {
        return visibilityEnabled;
    }

    public void setChatEnabled(boolean chatEnabled) {
        this.chatEnabled = chatEnabled;
    }

    public void setDoubleJumpEnabled(boolean doubleJumpEnabled) {
        this.doubleJumpEnabled = doubleJumpEnabled;
    }

    public void setFlyEnabled(boolean flyEnabled) {
        this.flyEnabled = flyEnabled;
    }

    public void setJumpEnabled(boolean jumpEnabled) {
        this.jumpEnabled = jumpEnabled;
    }

    public void setRadioEnabled(boolean radioEnabled) {
        this.radioEnabled = radioEnabled;
    }

    public void setStackerEnabled(boolean stackerEnabled) {
        this.stackerEnabled = stackerEnabled;
    }

    public void setVisibilityEnabled(boolean visibilityEnabled) {
        this.visibilityEnabled = visibilityEnabled;
    }

    public void setSpeedEnabled(boolean radioEnabled) {
        this.speedEnabled = radioEnabled;
    }


}
