package io.github.Leonardo0013YT.UltraMinions.minions.animations;

import io.github.Leonardo0013YT.UltraMinions.interfaces.BlockAnimation;
import io.github.Leonardo0013YT.UltraMinions.utils.MinionUtils_1_19_R1;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockBreakAnimation implements BlockAnimation {

    private int damage;
    private Block block;
    private boolean finished = false, execute = false;

    public BlockBreakAnimation(Block block) {
        this.block = block;
        this.damage = 0;
    }

    @Override
    public void update() {
        execute = !execute;
        if (!execute) return;
        if (damage > 9) {
            finished = true;
            return;
        }
        if (block != null && !block.getType().equals(Material.AIR)) {
            MinionUtils_1_19_R1.damageBlock(block.getLocation(), damage);
        }
        damage++;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

}