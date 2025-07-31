package com.zTPA.api.event.player;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class DenyTeleportRequestEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private boolean isCancelled;

    protected Player target;

    public DenyTeleportRequestEvent(Player who, Player target) {
        super(who);
        this.target = target;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getTarget() {
        return this.target;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

}
