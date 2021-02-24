package net.jetcobblestone.minigameplugin.assets.customclass.classitem;

import net.jetcobblestone.minigameplugin.assets.game.Game;
import net.jetcobblestone.minigameplugin.assets.game.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class AbilityListener implements Listener{

	private static final GameManager gameManager = GameManager.getInstance();

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		final Player player = event.getPlayer();

		Game game = gameManager.getGameFromPlayer(event.getPlayer());
		if (game != null && !game.isAllowed(Game.Setting.MOVE)) {
			player.sendMessage(ChatColor.RED + "You can't use abilities right now");
			return;
		}

		final ClassItem classItem = ItemManager.classItemFromStack(player.getInventory().getItemInMainHand());

		if (classItem == null) return;
		classItem.trigger(player, event);
	}
}
