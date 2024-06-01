package com.tcoded.playerbountiesplus.hook.currency;

import org.bukkit.entity.Player;

public abstract class AbstractEconomyHook {

    public abstract boolean isValid();

    public abstract void giveEco(Player player, double amount);

    public abstract boolean takeEco(Player player, double amount);

    public abstract boolean takeEco(Player player, double amount, boolean force);

}
