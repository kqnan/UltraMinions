package io.github.Leonardo0013YT.UltraMinions.utils;

import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class MinionUtils_1_19_R1 {

    public static void damageBlock(Location l, int damage) {
        try {
            PacketPlayOutBlockBreakAnimation breaking = new PacketPlayOutBlockBreakAnimation(0,new BlockPosition(l.getX(), l.getY(), l.getZ()), damage);
            for (Entity ent : l.getWorld().getNearbyEntities(l, 4, 4, 4)) {
                if (!(ent instanceof Player)) continue;
                Player p = (Player) ent;
                sendPacket(p, breaking);
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