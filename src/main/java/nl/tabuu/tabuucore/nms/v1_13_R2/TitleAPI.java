package nl.tabuu.tabuucore.nms.v1_13_R2;

import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.PacketPlayOutTitle;
import nl.tabuu.tabuucore.nms.wrapper.ITitleAPI;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitleAPI implements ITitleAPI {
    public void sendSubTitle(Player player, String json, int fadeIn, int stay, int fadeOut) {
        send(player,PacketPlayOutTitle.EnumTitleAction.SUBTITLE, json, fadeIn, stay, fadeOut);
    }

    public void sendTitle(Player player, String json, int fadeIn, int stay, int fadeOut) {
        send(player,PacketPlayOutTitle.EnumTitleAction.TITLE, json, fadeIn, stay, fadeOut);
    }

    public void sendActionbar(Player player, String json, int fadeIn, int stay, int fadeOut) {
        send(player,PacketPlayOutTitle.EnumTitleAction.ACTIONBAR, json, fadeIn, stay, fadeOut);
    }

    private void send(Player player, PacketPlayOutTitle.EnumTitleAction type, String json, int fadeIn, int stay, int fadeOut){
        PacketPlayOutTitle packet = new PacketPlayOutTitle(type, IChatBaseComponent.ChatSerializer.a(json), fadeIn, stay, fadeOut);

        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().playerConnection.sendPacket(packet);
    }
}
