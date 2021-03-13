package net.jetcobblestone.minigameplugin.games.mutantmayhem;

import net.jetcobblestone.minigameplugin.MinigamePlugin;
import net.jetcobblestone.minigameplugin.assets.game.Game;
import net.jetcobblestone.minigameplugin.assets.game.GameFactory;
import net.jetcobblestone.minigameplugin.assets.game.map.GameMap;
import net.jetcobblestone.minigameplugin.assets.util.ItemFactory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Arrays;

public class MutantMayhemFactory extends GameFactory<MutantMayhem> {

    final ItemStack createGameIcon = ItemFactory.createItem(name, Material.IRON_AXE, Arrays.asList(ChatColor.GRAY + "Fight in epic fashion"));
    final ItemStack joinGameIcon = ItemFactory.createItem(name, Material.IRON_AXE, Arrays.asList("Wow amazing lore!"));

    public MutantMayhemFactory(String name) {
        super(name);
    }

    @Override
    public MutantMayhem createInstance(GameMap map, MinigamePlugin plugin) {
        try {
            return new MutantMayhem(map, plugin);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ItemStack getAddGameIcon() {
        return createGameIcon.clone();
    }

    public ItemStack getJoinGameIcon(Game game) {
        return joinGameIcon.clone();
    }
}
