package net.jetcobblestone.minigameplugin.assets.game.factory;

import lombok.Getter;
import net.jetcobblestone.minigameplugin.assets.game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GameRegisterEvent extends Event {
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

    @Getter private final Class<? extends Game> gameClass;

    public GameRegisterEvent(Class<? extends Game> gameClass) {
        this.gameClass = gameClass;
    }
}
