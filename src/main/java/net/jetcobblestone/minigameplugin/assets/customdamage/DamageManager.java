package net.jetcobblestone.minigameplugin.assets.customdamage;

import net.jetcobblestone.minigameplugin.MinigamePlugin;
import net.jetcobblestone.minigameplugin.assets.events.CustomDeathEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class DamageManager implements Listener {

    private final Map<LivingEntity, DamageableEntity> damageableEntityMap = new HashMap<>();
    private final Map<Entity, DamagingEntity> damagerEntityMap = new HashMap<>();

    public DamageManager(MinigamePlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void addDamageable(DamageableEntity damageableEntity) {
        damageableEntityMap.put(damageableEntity.getLivingEntity(), damageableEntity);
    }

    public void addDamager(DamagingEntity damagingEntity) {
        damagerEntityMap.put(damagingEntity.getEntity(), damagingEntity);
    }

    public void remove(DamageableEntity damageableEntity) {
        damageableEntityMap.remove(damageableEntity.getLivingEntity());
    }

    @EventHandler
    public void interceptDamage(EntityDamageByEntityEvent event) {
        final Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) return;

        final LivingEntity livingEntity = (LivingEntity) entity;
        final DamageableEntity damageableEntity = damageableEntityMap.get(livingEntity);
        if (damageableEntity == null) return;

        event.setDamage(0);

        final Entity damager = event.getDamager();
        final DamagingEntity damagingEntity = damagerEntityMap.get(damager);
        if (damagingEntity == null) return;

        damageableEntity.damage(damagingEntity.getDamage());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void defaultDeath(CustomDeathEvent event) {
        if (!event.isDefaultDeath()) return;
        final DamageableEntity damageableEntity = event.getDamageableEntity();
        final Entity entity = damageableEntity.getLivingEntity();
        if (!(entity instanceof Player)) {
            damageableEntity.destroy();
            return;
        }
        entity.sendMessage(ChatColor.RED + "You died a default death");
        final DamageablePlayer damageablePlayer = (DamageablePlayer) damageableEntity;
        damageablePlayer.respawn();
    }
}
