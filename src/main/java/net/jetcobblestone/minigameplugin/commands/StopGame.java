package net.jetcobblestone.minigameplugin.commands;

import net.jetcobblestone.minigameplugin.assets.game.Game;
import net.jetcobblestone.minigameplugin.assets.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StopGame implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2, @NotNull String[] arg3) {

        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Only a player can use this (as of yet)");
            return false;
        }
        Player player = (Player) sender;
        Game game = GameManager.getInstance().getGameFromPlayer(player);
        if (game == null) {
            player.sendMessage(ChatColor.RED + "You are not in a game");
            return false;
        }
        game.forceStop();
        return true;
    }
}
