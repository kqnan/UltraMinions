package io.github.Leonardo0013YT.UltraMinions.api.events;

import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import org.bukkit.Chunk;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MinionLoadEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;
    private PlayerMinion playerMinion;
    private Chunk chunk;

    public MinionLoadEvent(PlayerMinion playerMinion, Chunk chunk) {
        this.playerMinion = playerMinion;
        this.chunk = chunk;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public PlayerMinion getPlayerMinion() {
        return playerMinion;
    }

    public Chunk getChunk() {
        return chunk;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

}
