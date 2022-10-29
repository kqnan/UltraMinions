package io.github.Leonardo0013YT.UltraMinions.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import net.minecraft.network.protocol.Packet;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class MinionUtils {

    public static void damageBlock(Location l, int damage) {
        try {
            PacketContainer packet=new PacketContainer(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
            packet.getModifier().writeDefaults();
            com.comphenix.protocol.wrappers.BlockPosition blockPosition=new com.comphenix.protocol.wrappers.BlockPosition(l.getBlockX(),l.getBlockY(),l.getBlockZ());
            packet.getIntegers().write(0,0);
            packet.getIntegers().write(1,damage);
            packet.getBlockPositionModifier().write(0,blockPosition);


            // PacketPlayOutBlockBreakAnimation breaking = new PacketPlayOutBlockBreakAnimation(0,new BlockPosition(l.getX(), l.getY(), l.getZ()), damage);
          //  System.out.println("发包...........");
            for (Entity ent : l.getWorld().getNearbyEntities(l, 4, 4, 4)) {
                if (!(ent instanceof Player)) continue;
                Player p = (Player) ent;
                //sendPacket(p, breaking);
                ProtocolLibrary.getProtocolManager().sendServerPacket(p,packet);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendPacket(Player player, Packet<?> packet) {
        try {
            ((CraftPlayer) player).getHandle().b.a(packet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}