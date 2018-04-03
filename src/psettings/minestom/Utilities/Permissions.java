package psettings.minestom.Utilities;

import org.bukkit.entity.Player;

public class Permissions {

    public static final String HIDE_STAFF = "psettings.hide.staff";
    public static final String CHAT_STAFF = "psettings.chat.staff";
    public static final String HIDE_BYPASS = "psettings.hide.bypass";
    public static final String ADMIN = "psettings.admin";
    public static final String ALL_SETTINGS = "psettings.settings.all";
    public static final String COOLDOWN_BYPASS = "psettings.cooldown.bypass";
    public static final String DOUBLEJUMP_COOLDOWN_BYPASS = "psettings.cooldown.doublejump.bypass";
    public static final String PARTICLE_JUMP = "psettings.doublejump.particle";
    public static final String PARTICLE_MENU_COMMAND = "psettings.command.menus.particles";
    public static final String MAIN_MENU_COMMAND = "psettings.command.menus.main";
    public static final String CHAT_MENU_COMMAND = "psettings.command.menus.chat";
    public static final String VISIBILITY_MENU_COMMAND = "psettings.command.menus.visibility";
    public static final String CHAT_COMMAND = "psettings.command.options.chat";
    public static final String SPEED_COMMAND = "psettings.command.options.chat";
    public static final String JUMP_COMMAND = "psettings.command.options.jump";
    public static final String DOUBLE_JUMP_COMMAND = "psettings.command.options.doublejump";
    public static final String FLY_COMMAND = "psettings.command.options.fly";
    public static final String STACKER_COMMAND = "psettings.command.options.stacker";
    public static final String RADIO_COMMAND = "psettings.command.options.radio";
    public static final String VISIBILITY_COMMAND = "psettings.command.options.visibility";
    public static final String OPEN_OTHER = "psettings.open.other";
    public static final String GET_ITEM = "psettings.getitem";

    public static boolean hasAllOptionsPerm(Player player) {
        return player.hasPermission(ALL_SETTINGS) || player.hasPermission(ADMIN);
    }
}
