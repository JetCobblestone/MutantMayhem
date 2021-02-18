package net.jetcobblestone.minigameplugin.assets.classitem;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class AbilityListener implements Listener{

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		final ClassItem classItem = ItemManager.classItemFromStack(player.getInventory().getItemInMainHand());
		
		if (classItem == null) return;
		classItem.trigger(player, event);
	}
}
