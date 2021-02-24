package net.jetcobblestone.minigameplugin.assets.game;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class SettingsListener implements Listener {

    private final GameManager gameManager = GameManager.getInstance();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Game game = gameManager.getGameFromPlayer(event.getPlayer());
        if (game == null || game.isAllowed(Game.Setting.MINE)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Game game = gameManager.getGameFromPlayer(event.getPlayer());
        if (game == null || game.isAllowed(Game.Setting.PLACE)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Game game = gameManager.getGameFromPlayer(event.getPlayer());
        if (game == null || game.isAllowed(Game.Setting.MOVE)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onFall(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;

        Game game = gameManager.getGameFromPlayer(player);
        if (game == null || (game.isAllowed(Game.Setting.FALLDAMAGE) || !(event.getCause() == EntityDamageEvent.DamageCause.FALL))) return;
        event.setCancelled(true);
    }
}
