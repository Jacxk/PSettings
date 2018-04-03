package psettings.minestom.Settings.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import psettings.minestom.Managers.MessageManager;
import psettings.minestom.Managers.SettingsManager;
import psettings.minestom.PSettings;
import psettings.minestom.Utilities.MessageUtil;
import psettings.minestom.Utilities.Util;
import psettings.minestom.enums.SendSound;
import psettings.minestom.hooks.PartyAndFriends;

import java.util.List;

public class ChatListener implements Listener {

    private PSettings plugin;
    private Util util;
    private MessageManager messageManager;
    private PartyAndFriends partyAndFriends;

    public ChatListener(PSettings plugin, Util util, MessageManager messageManager, PartyAndFriends partyAndFriends) {
        this.plugin = plugin;
        this.util = util;
        this.messageManager = messageManager;
        this.partyAndFriends = partyAndFriends;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player messenger = event.getPlayer();
        String message = event.getMessage().toLowerCase();

        if (!util.getSettingsManager(messenger).isChatEnabled()) {
            event.getRecipients().remove(messenger);
            if (!event.isCancelled()) {
                event.setCancelled(true);
                MessageUtil.message(messenger, messageManager.getString("MainMenu.Chat.CantChat", messenger));
                util.sendSound(messenger, SendSound.NO_CHAT);
            }
        } else for (Player online : Bukkit.getOnlinePlayers()) {
            SettingsManager settings2 = util.getSettingsManager(online);
            if (partyAndFriends.isPAFEnabled() && !partyAndFriends.isFriends(messenger) && settings2.isFriendsChatOnlyEnabled()) {
                event.getRecipients().remove(online);
            }
            if (settings2.isStaffChatOnlyEnabled() && !messenger.hasPermission("psettings.chat.staff")) {
                event.getRecipients().remove(online);
            }
            if (!settings2.isChatEnabled()) {
                event.getRecipients().remove(online);
            }

            SettingsManager settings = util.getSettingsManager(online);

            if (message.contains(online.getName().toLowerCase()) && settings.isMentionEnabled() && messenger != online) {
                String mentionMessage = messageManager.getString("ChatMenu.Mention.Mentioned.Message", online, messenger);
                String title = messageManager.getString("ChatMenu.Mention.Mentioned.Title", online, messenger);
                String subtitle = messageManager.getString("ChatMenu.Mention.Mentioned.Subtitle", online, messenger);
                List<String> types = plugin.getConfig().getStringList("ChatMenu.Mention.Mentioned.Type");

                if (!settings.isChatEnabled() && plugin.getConfig().getBoolean("ChatMenu.Mention.PlayerChatDisabled")) {
                    MessageUtil.message(messenger, MessageUtil.color(messageManager.getString("ChatMenu.Mention.PlayerChatDisabled", messenger, online)));
                    return;
                }
                if (!partyAndFriends.isFriends(messenger) && settings.isFriendsChatOnlyEnabled()
                        && plugin.getConfig().getBoolean("ChatMenu.Mention.FriendsOnlyMode")) {
                    MessageUtil.message(messenger, messageManager.getString("ChatMenu.Mention.FriendsOnlyMode", messenger, online));
                    return;
                }
                if (settings.isStaffChatOnlyEnabled() && plugin.getConfig().getBoolean("ChatMenu.Mention.StaffOnlyMode")
                        && !messenger.hasPermission("psettings.chat.staff")) {
                    MessageUtil.message(messenger, messageManager.getString("ChatMenu.Mention.StaffOnlyMode", messenger, online));
                    return;
                }
                if (types.contains("chat")) MessageUtil.message(online, mentionMessage);

                if (types.contains("actionbar"))
                    plugin.getNMS().sendActionbar(online, MessageUtil.color(mentionMessage));

                if (types.contains("title and subtitle")) {
                    int stay = plugin.getConfig().getInt("ChatMenu.Mention.Mentioned.StayTime");
                    plugin.getNMS().sendSubtitle(online, MessageUtil.color(subtitle), 20, stay * 20, 20);
                    plugin.getNMS().sendTitle(online, MessageUtil.color(title), 20, stay * 20, 20);
                }

                util.sendSound(online, SendSound.MENTION);
            }
        }
        event.getRecipients().add(messenger);
    }
}
