package net.jetcobblestone.minigameplugin.games.mutantmayhem.kitsystem.classitem.itemability;

import net.jetcobblestone.minigameplugin.assets.game.player.GamePlayer;
import net.jetcobblestone.minigameplugin.assets.game.player.PlayerManager;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kitsystem.classitem.ClassItem;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kitsystem.classitem.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class AbilityListener implements Listener{

	private final PlayerManager playerManager;

	public AbilityListener(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	private boolean canAbility(Player player) {
		GamePlayer gamePlayer = playerManager.getGameFromPlayer(player);
		if (gamePlayer != null && !gamePlayer.isAllowed(GamePlayer.Setting.MOVE)) {
			player.sendMessage(ChatColor.RED + "You can't use abilities right now");
			return false;
		}
		return true;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if (!canAbility(player)) {
			return;
		}

		final ClassItem classItem = ItemManager.classItemFromStack(player.getInventory().getItemInMainHand());

		if (classItem == null) return;
		classItem.trigger(player, event);
	}

	@EventHandler
	public void onConsume(PlayerItemConsumeEvent event) {
		final Player player = event.getPlayer();
		if (!canAbility(player)) {
			return;
		}

		final ClassItem classItem = ItemManager.classItemFromStack(player.getInventory().getItemInMainHand());

		if (classItem == null) return;
		classItem.trigger(player, event);
	}
}
