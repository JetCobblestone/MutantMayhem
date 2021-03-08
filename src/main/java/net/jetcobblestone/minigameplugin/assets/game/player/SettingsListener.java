package net.jetcobblestone.minigameplugin.assets.game.player;

import net.jetcobblestone.minigameplugin.assets.game.player.GamePlayer;
import net.jetcobblestone.minigameplugin.assets.game.player.PlayerManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class SettingsListener implements Listener {

    private final PlayerManager playerManager;

    public SettingsListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        GamePlayer gamePlayer = playerManager.getGameFromPlayer(event.getPlayer());
        if (gamePlayer == null || gamePlayer.isAllowed(GamePlayer.Setting.MINE)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        GamePlayer gamePlayer = playerManager.getGameFromPlayer(event.getPlayer());
        if (gamePlayer == null || gamePlayer.isAllowed(GamePlayer.Setting.PLACE)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        GamePlayer gamePlayer = playerManager.getGameFromPlayer(event.getPlayer());
        if (gamePlayer == null || gamePlayer.isAllowed(GamePlayer.Setting.MOVE)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onFall(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;

        GamePlayer gamePlayer = playerManager.getGameFromPlayer(player);
        if (gamePlayer == null || (gamePlayer.isAllowed(GamePlayer.Setting.FALLDAMAGE) || !(event.getCause() == EntityDamageEvent.DamageCause.FALL))) return;
        event.setCancelled(true);
    }
}
