package net.jetcobblestone.mutantmayhem.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.jetcobblestone.mutantmayhem.assets.customclass.ClassManager;

public class KitSelector implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only a player can use this command");
			return false;
		}
		Player player = (Player) sender;
		ClassManager.getInstance().getGui().dupeOpen(player);
		return true;
	}

}
