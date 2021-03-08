package net.jetcobblestone.minigameplugin.commands;

import net.jetcobblestone.minigameplugin.MinigamePlugin;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.MutantMayhem;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class StartGame implements CommandExecutor {

    private final MinigamePlugin plugin;

    public StartGame(MinigamePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2, @NotNull String[] arg3) {

        try {
            new MutantMayhem(plugin.getMapManager().getMaps(MutantMayhem.class).get(0), plugin);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
