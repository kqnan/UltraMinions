package io.github.Leonardo0013YT.UltraMinions.managers;

import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import io.github.Leonardo0013YT.UltraMinions.setup.*;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SetupManager {

    private HashMap<Player, PlayerMinion> setupChest = new HashMap<>();
    private HashMap<Player, SetupMinion> setupMinion = new HashMap<>();
    private HashMap<Player, SetupAutoSell> setupAutoSell = new HashMap<>();
    private HashMap<Player, SetupAutoSmelt> setupAutoSmelt = new HashMap<>();
    private HashMap<Player, SetupCompressor> setupCompressor = new HashMap<>();
    private HashMap<Player, SetupFuel> setupFuel = new HashMap<>();
    private HashMap<Player, String> setupName = new HashMap<>();
    private HashMap<Player, SetupFood> setupFood = new HashMap<>();

    public void setSetupFood(Player p, SetupFood sm) {
        setupFood.put(p, sm);
    }

    public SetupFood getSetupFood(Player p) {
        return setupFood.get(p);
    }

    public boolean isSetupFood(Player p) {
        return setupFood.containsKey(p);
    }

    public void removeSetupFood(Player p) {
        setupFood.remove(p);
    }

    public void setSetupChest(Player p, PlayerMinion sm) {
        setupChest.put(p, sm);
    }

    public PlayerMinion getSetupChest(Player p) {
        return setupChest.get(p);
    }

    public boolean isSetupChest(Player p) {
        return setupChest.containsKey(p);
    }

    public void removeSetupChest(Player p) {
        setupChest.remove(p);
    }

    public void setSetupName(Player p, String sm) {
        setupName.put(p, sm);
    }

    public String getSetupName(Player p) {
        return setupName.get(p);
    }

    public boolean isSetupName(Player p) {
        return setupName.containsKey(p);
    }

    public void removeSetupName(Player p) {
        setupName.remove(p);
    }

    public void setSetupMinion(Player p, SetupMinion sm) {
        setupMinion.put(p, sm);
    }

    public SetupMinion getSetupMinion(Player p) {
        return setupMinion.get(p);
    }

    public boolean isSetupMinion(Player p) {
        return setupMinion.containsKey(p);
    }

    public void removeSetupMinion(Player p) {
        setupMinion.remove(p);
    }

    public void setSetupAutoSell(Player p, SetupAutoSell sm) {
        setupAutoSell.put(p, sm);
    }

    public SetupAutoSell getSetupAutoSell(Player p) {
        return setupAutoSell.get(p);
    }

    public boolean isSetupAutoSell(Player p) {
        return setupAutoSell.containsKey(p);
    }

    public void removeSetupAutoSell(Player p) {
        setupAutoSell.remove(p);
    }

    public void setSetupAutoSmelt(Player p, SetupAutoSmelt sm) {
        setupAutoSmelt.put(p, sm);
    }

    public SetupAutoSmelt getSetupAutoSmelt(Player p) {
        return setupAutoSmelt.get(p);
    }

    public boolean isSetupAutoSmelt(Player p) {
        return setupAutoSmelt.containsKey(p);
    }

    public void removeSetupAutoSmelt(Player p) {
        setupAutoSmelt.remove(p);
    }

    public void setSetupCompressor(Player p, SetupCompressor sm) {
        setupCompressor.put(p, sm);
    }

    public SetupCompressor getSetupCompressor(Player p) {
        return setupCompressor.get(p);
    }

    public boolean isSetupCompressor(Player p) {
        return setupCompressor.containsKey(p);
    }

    public void removeSetupCompressor(Player p) {
        setupCompressor.remove(p);
    }

    public void setSetupFuel(Player p, SetupFuel sm) {
        setupFuel.put(p, sm);
    }

    public SetupFuel getSetupFuel(Player p) {
        return setupFuel.get(p);
    }

    public boolean isSetupFuel(Player p) {
        return setupFuel.containsKey(p);
    }

    public void removeSetupFuel(Player p) {
        setupFuel.remove(p);
    }

}