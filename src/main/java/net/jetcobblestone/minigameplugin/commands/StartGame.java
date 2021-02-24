package net.jetcobblestone.minigameplugin.commands;

import net.jetcobblestone.minigameplugin.assets.game.map.MapManager;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.MutantMayhem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StartGame implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2, @NotNull String[] arg3) {

        new MutantMayhem(new ArrayList<>(Bukkit.getOnlinePlayers()), MapManager.getInstance().getMaps(MutantMayhem.class).get(0));

        return false;
    }
}
