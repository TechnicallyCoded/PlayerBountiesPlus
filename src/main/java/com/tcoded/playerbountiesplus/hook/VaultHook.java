package com.tcoded.playerbountiesplus.hook;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

public class VaultHook {

    private final PlayerBountiesPlus plugin;

    private Economy eco;

    public VaultHook(PlayerBountiesPlus plugin) {
        this.plugin = plugin;

    }

    public boolean init() {
        if (this.plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = this.plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        eco = rsp.getProvider();
        return true;
    }

    public void addMoney(Player player, double amount) {
        this.eco.depositPlayer(player, amount);
    }

    public boolean takeMoney(Player player, double amount) {
        double balance = this.eco.getBalance(player);
        if (balance < amount) return false;

        this.eco.withdrawPlayer(player, amount);
        return true;
    }
}
