package net.jetcobblestone.minigameplugin.assets.customdamage;


import lombok.Getter;
import net.jetcobblestone.minigameplugin.assets.events.CustomDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

import java.util.Objects;

public class DamageableEntity extends DamagingEntity {

    protected final LivingEntity livingEntity;
    @Getter protected double maxHealth;
    protected double health;
    protected double defence;
    protected final DamageManager damageManager;

    public DamageableEntity(LivingEntity livingEntity, double maxHealth, double defence, double damage, DamageManager damageManager) {
        super(livingEntity,damage, damageManager);
        this.livingEntity = livingEntity;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.defence = defence;
        this.damageManager = damageManager;

        damageManager.addDamageable(this);
    }

    public LivingEntity getLivingEntity() {
        return livingEntity;
    }

    public void damage(double damage) {
        health = health - (damage/defence);
        livingEntity.sendMessage("Your health is " + health);
        if (health <= 0) {
            Bukkit.getPluginManager().callEvent(new CustomDeathEvent(this));
            return;
        }

        livingEntity.setHealth((health/maxHealth)* Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
    }

    public void destroy() {
        livingEntity.setHealth(0);
        damageManager.remove(this);
    }

    public void setHealth(double health) {
        this.health = health;
        final double vanillaMaxHealth = Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
        final double vanillaHealth = (health/maxHealth)  * vanillaMaxHealth;
        livingEntity.setHealth(vanillaHealth);
    }
}
