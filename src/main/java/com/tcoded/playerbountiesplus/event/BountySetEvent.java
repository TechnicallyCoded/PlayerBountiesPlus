package com.tcoded.playerbountiesplus.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BountySetEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player setter;
    private final Player target;

    private float amount;
    private boolean cancelled;

    public BountySetEvent(Player setter, @NotNull Player target, float amount) {
        this.setter = setter;
        this.target = target;
        this.amount = amount;
        this.cancelled = false;
    }

    /**
     * @return The player who set the bounty or null if it's set by console/command block.
     */
    public Player getSetter() {
        return setter;
    }

    public Player getTarget() {
        return target;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
