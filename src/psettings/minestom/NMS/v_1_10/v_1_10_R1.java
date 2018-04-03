package psettings.minestom.NMS.v_1_10;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_10_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import psettings.minestom.NMS.NMS;
import psettings.minestom.Utilities.MessageUtil;

import java.util.List;

public class v_1_10_R1 implements NMS {

    @Override
    public void sendActionbar(Player p, String message) {

        IChatBaseComponent chatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");

        PacketPlayOutChat bar = new PacketPlayOutChat(chatBaseComponent, (byte) 2);

        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(bar);
    }

    @Override
    public void sendTitle(Player player, String text, int fadeIn, int stay, int fadeOut) {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + text + "\",\"color\":\"" + ChatColor.GOLD.name().toLowerCase() + "\"}");

        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, stay, fadeOut);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
    }

    @Override
    public void sendSubtitle(Player player, String text, int fadeIn, int stay, int fadeOut) {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + text + "\",\"color\":\"" + ChatColor.GOLD.name().toLowerCase() + "\"}");

        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, null);
        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, stay, fadeOut);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitle);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
    }

    @Override
    public void spawnParticle(Player player, String particle, Location location) {
        float x = (float) location.getX();
        float y = (float) location.getY();
        float z = (float) location.getZ();
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.valueOf(particle), true, x, y, z, 0, 0, 0, 1, 1);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public org.bukkit.inventory.ItemStack book(String old, String updated, int id, Player player) {
        org.bukkit.inventory.ItemStack book = new org.bukkit.inventory.ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        List<IChatBaseComponent> pages;

        try {
            pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bookMeta);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return null;
        }

        TextComponent click = new TextComponent(MessageUtil.color("\n\n      &2&lCLICK HERE"));
        click.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/" + id + "/"));
        click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(MessageUtil.color("&aYour version: &d"
                + old + "\n&aNewest version: &d" + updated)).create()));

        TextComponent disable = new TextComponent(MessageUtil.color("\n\n &5Want to disable this?"));
        disable.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/psettings updater false"));
        disable.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(MessageUtil.color("&7Click here to disable\nthe auto updater")).create()));

        TextComponent text = new TextComponent(MessageUtil.color("\n  &6&lUpdate Available\n\n"));
        text.addExtra(MessageUtil.color("&0Hey &9" + player.getName() + " &0there is a new update available for the plugin &4&lPSettings&0 you can download it right now."));
        text.addExtra(click);
        text.addExtra(disable);

        IChatBaseComponent page = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(text));

        pages.add(page);

        bookMeta.setTitle("New Update");
        bookMeta.setAuthor("The Update Guy");

        book.setItemMeta(bookMeta);
        return book;
    }

    @Override
    public void openBook(String actualVersion, String newVersion, int id, Player player) {
        int slot = player.getInventory().getHeldItemSlot();
        ItemStack old = player.getInventory().getItem(slot);
        player.getInventory().setItem(slot, book(actualVersion, newVersion, id, player));

        ByteBuf buf = Unpooled.buffer(256);
        buf.setByte(0, (byte) 0);
        buf.writerIndex(1);

        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(buf));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        player.getInventory().setItem(slot, old);
    }
}