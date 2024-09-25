package dev.lone.ScreenEffects.NMS.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import dev.lone.ScreenEffects.NMS.GamemodeNMS;
import dev.lone.ScreenEffects.NMS.IGamemodeNMS;
import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GamemodeNMS_v1_21_1 implements IGamemodeNMS
{
    Method method;
    {
        try
        {
            // https://mappings.dev/1.21.1/net/minecraft/world/entity/player/Player.html
            method = EntityHuman.class.getMethod("z"); // onUpdateAbilities() sends PacketPlayOutAbilities.
        }
        catch (NoSuchMethodException e)
        {
            System.err.println("Failed to load ScreenEffects for 1.21.1");
            e.printStackTrace();
        }
    }

    @Override
    public void setGamemode(Player player, float gamemode)
    {
        try {
            // Crea il pacchetto GAME_STATE_CHANGE
            PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.GAME_STATE_CHANGE);

            // Imposta il tipo di cambiamento: 3 è per il cambio della modalità di gioco
            packet.getIntegers().write(0, 3);

            // Imposta la nuova modalità di gioco
            packet.getFloat().write(0, gamemode);

            // Invia il pacchetto al giocatore
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshAbilities(Player player)
    {
        if(method == null)
            return;
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        try
        {
            method.invoke(nmsPlayer);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }
}