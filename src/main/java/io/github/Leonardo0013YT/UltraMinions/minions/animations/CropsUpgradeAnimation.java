package io.github.Leonardo0013YT.UltraMinions.minions.animations;

import io.github.Leonardo0013YT.UltraMinions.interfaces.BlockAnimation;
import io.github.Leonardo0013YT.UltraMinions.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;

public class CropsUpgradeAnimation implements BlockAnimation {

    private Block block;
    private boolean finished = false, execute = false;

    public CropsUpgradeAnimation(Block block) {
        this.block = block;
    }

    @Override
    public void update() {
        if (block == null || block.getType().equals(Material.AIR)) {
            return;
        }
        execute = !execute;
        if (!execute) return;
        if (!(block.getBlockData() instanceof Ageable) || Utils.isMax(block)) {
            finished = true;
            return;
        }
        Ageable age = (Ageable) block.getBlockData();
        age.setAge(age.getAge() + 1);
        block.setBlockData(age);
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}