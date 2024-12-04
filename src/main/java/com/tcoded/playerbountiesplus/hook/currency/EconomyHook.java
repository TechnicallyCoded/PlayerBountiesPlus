package com.tcoded.playerbountiesplus.hook.currency;

import org.bukkit.entity.Player;

public interface EconomyHook {

    boolean isValid();

    void giveEco(Player player, double amount);

    boolean takeEco(Player player, double amount);

    boolean takeEco(Player player, double amount, boolean force);

}
