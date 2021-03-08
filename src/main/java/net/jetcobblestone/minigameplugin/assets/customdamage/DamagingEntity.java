package net.jetcobblestone.minigameplugin.assets.customdamage;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;

public class DamagingEntity {
    @Getter protected final Entity entity;
    @Getter @Setter protected double damage;
    protected final DamageManager damageManager;

    public DamagingEntity(Entity entity, double damage, DamageManager damageManager) {
        this.entity = entity;
        this.damage = damage;
        this.damageManager = damageManager;

        damageManager.addDamager(this);
    }

}
