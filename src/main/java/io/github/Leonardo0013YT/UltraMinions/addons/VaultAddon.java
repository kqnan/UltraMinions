package io.github.Leonardo0013YT.UltraMinions.addons;

import io.github.Leonardo0013YT.UltraMinions.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;

public class VaultAddon {

    private Economy econ;

    public VaultAddon(Main plugin) {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            econ = rsp.getProvider();
        }
    }

    public void addCoins(Player p, double amount) {
        if (econ != null) {
            econ.depositPlayer(p, amount);
        }
    }

    public void removeCoins(Player p, double amount) {
        if (econ != null) {
            econ.withdrawPlayer(p, amount);
        }
    }

    public double getCoins(Player p) {
        return econ.getBalance(p);
    }

}