package net.jetcobblestone.minigameplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.jetcobblestone.minigameplugin.assets.classitem.ItemManager;

public class TestCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		final Player player = (Player) sender;
		ItemStack item = ItemManager.testGet(Integer.parseInt(arg3[0]));
		player.getInventory().addItem(item);
		return true;
	}

}
