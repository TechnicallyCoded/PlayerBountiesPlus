package com.tcoded.playerbountiesplus.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BountyClaimEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player claimant;
    private final Player victim;
    private final double amount;

    private boolean cancelled;

    public BountyClaimEvent(@NotNull Player claimant, @NotNull Player victim, double amount) {
        this.claimant = claimant;
        this.victim = victim;
        this.amount = amount;
        this.cancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public Player getClaimant() {
        return claimant;
    }

    public Player getVictim() {
        return victim;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}