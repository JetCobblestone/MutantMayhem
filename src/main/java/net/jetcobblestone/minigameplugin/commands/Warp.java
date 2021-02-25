package net.jetcobblestone.minigameplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Warp implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2, @NotNull String[] arg3) {

        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Only a player can use this (as of yet)");
            return false;
        }
        Player player = (Player) sender;
        player.sendMessage(arg3[0]);
        World world = Bukkit.getWorld(arg3[0]);

        if (world == null) {
            player.sendMessage(ChatColor.RED + "That world couldn't be found");
            return false;
        }

        Location location = player.getLocation();
        location.setWorld(world);
        player.teleport(location);

        return true;
    }
}
