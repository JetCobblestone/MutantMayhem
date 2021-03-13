package net.jetcobblestone.minigameplugin.commands;

import net.jetcobblestone.minigameplugin.MinigamePlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class StartGame implements CommandExecutor {

    private final MinigamePlugin plugin;

    public StartGame(MinigamePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2, @NotNull String[] arg3) {

        if (!(sender instanceof Player)) return false;
        plugin.getGameManager().getGameMenu().open((Player) sender);

        return true;
    }
}
