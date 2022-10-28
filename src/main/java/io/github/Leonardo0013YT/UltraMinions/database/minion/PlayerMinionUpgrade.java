package io.github.Leonardo0013YT.UltraMinions.database.minion;

import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeAutoSell;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeAutoSmelt;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeCompressor;
import io.github.Leonardo0013YT.UltraMinions.upgrades.UpgradeFuel;

public class PlayerMinionUpgrade {

    private UpgradeFuel fuel;
    private UpgradeAutoSell autoSell;
    private UpgradeCompressor compressor;
    private UpgradeAutoSmelt autoSmelt;
    private PlayerMinion pm;

    public PlayerMinionUpgrade(PlayerMinion pm) {
        this.pm = pm;
    }

    public UpgradeFuel getFuel() {
        return fuel;
    }

    public void setFuel(UpgradeFuel fuel) {
        this.fuel = fuel;
    }

    public UpgradeAutoSell getAutoSell() {
        return autoSell;
    }

    public void setAutoSell(UpgradeAutoSell autoSell) {
        this.autoSell = autoSell;
    }

    public UpgradeCompressor getCompressor() {
        return compressor;
    }

    public void setCompressor(UpgradeCompressor compressor) {
        this.compressor = compressor;
    }

    public UpgradeAutoSmelt getAutoSmelt() {
        return autoSmelt;
    }

    public void setAutoSmelt(UpgradeAutoSmelt autoSmelt) {
        this.autoSmelt = autoSmelt;
    }

}