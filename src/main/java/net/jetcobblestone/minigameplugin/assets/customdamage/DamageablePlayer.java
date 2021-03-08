package net.jetcobblestone.minigameplugin.assets.customdamage;

import org.bukkit.entity.Player;

public class DamageablePlayer extends DamageableEntity {

    public DamageablePlayer(Player player, double maxHealth, double defence, double damage, DamageManager damageManager) {
        super(player, maxHealth, defence, damage, damageManager);
    }

    @Override
    public Player getLivingEntity() {
        return (Player) livingEntity;
    }

    public void respawn() {
        livingEntity.setHealth(0);
        health = maxHealth;
    }
}
