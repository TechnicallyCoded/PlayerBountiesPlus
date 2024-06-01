package com.tcoded.playerbountiesplus.hook.currency;

import org.bukkit.entity.Player;

public class DummyEcoHook extends AbstractEconomyHook {

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void giveEco(Player player, double amount) {
        throw new UnsupportedOperationException("Economy isn't setup. Dummy hook is the active fallback.");
    }

    @Override
    public boolean takeEco(Player player, double amount) {
        throw new UnsupportedOperationException("Economy isn't setup. Dummy hook is the active fallback.");
    }

    @Override
    public boolean takeEco(Player player, double amount, boolean force) {
        throw new UnsupportedOperationException("Economy isn't setup. Dummy hook is the active fallback.");
    }

}
