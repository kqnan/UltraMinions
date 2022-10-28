package io.github.Leonardo0013YT.UltraMinions.api.events;

import io.github.Leonardo0013YT.UltraMinions.database.PlayerMinion;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MinionPlaceEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private PlayerMinion playerMinion;
    private Block block;

    public MinionPlaceEvent(PlayerMinion playerMinion, Block block) {
        this.playerMinion = playerMinion;
        this.block = block;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public PlayerMinion getPlayerMinion() {
        return playerMinion;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

}
