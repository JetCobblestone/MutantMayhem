package net.jetcobblestone.minigameplugin.assets.events;

import lombok.Getter;
import lombok.Setter;
import net.jetcobblestone.minigameplugin.assets.customdamage.DamageableEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CustomDeathEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean isCancelled;
    @Getter @Setter private boolean defaultDeath = true;
    @Getter private final DamageableEntity damageableEntity;


    public CustomDeathEvent(DamageableEntity damageableEntity) {
        this.damageableEntity = damageableEntity;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
