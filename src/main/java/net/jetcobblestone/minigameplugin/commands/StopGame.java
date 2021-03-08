package net.jetcobblestone.minigameplugin.commands;

import net.jetcobblestone.minigameplugin.assets.game.player.GamePlayer;
import net.jetcobblestone.minigameplugin.assets.game.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StopGame implements CommandExecutor {

    private final PlayerManager playerManager;

    public StopGame(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2, @NotNull String[] arg3) {

        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Only a player can use this (as of yet)");
            return false;
        }
        Player player = (Player) sender;
        GamePlayer gamePlayer = playerManager.getGameFromPlayer(player);
        if (gamePlayer == null || gamePlayer.getGame() == null) {
            player.sendMessage(ChatColor.RED + "You are not in a game");
            return false;
        }
        gamePlayer.getGame().forceStop();
        return true;
    }
}
