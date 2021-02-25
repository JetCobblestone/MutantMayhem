package net.jetcobblestone.minigameplugin.assets.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class OnTickEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean isCancelled;

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
