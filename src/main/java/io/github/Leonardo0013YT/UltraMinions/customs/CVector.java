package io.github.Leonardo0013YT.UltraMinions.customs;

import lombok.Getter;
import org.bukkit.util.Vector;

public class CVector {

    @Getter
    private int x, y, z;

    public CVector(Vector v) {
        this.x = v.getBlockX();
        this.y = v.getBlockY();
        this.z = v.getBlockZ();
    }

}