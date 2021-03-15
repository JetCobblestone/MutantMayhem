package net.jetcobblestone.minigameplugin.games.mutantmayhem;

import net.jetcobblestone.minigameplugin.MinigamePlugin;
import net.jetcobblestone.minigameplugin.assets.game.Lobby;
import net.jetcobblestone.minigameplugin.assets.game.factory.GameFactory;
import net.jetcobblestone.minigameplugin.assets.game.map.GameMap;
import net.jetcobblestone.minigameplugin.assets.util.ItemFactory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class MutantMayhemFactory extends GameFactory<MutantMayhem> {

    private final ItemStack createGameIcon = ItemFactory.createItem(name, Material.IRON_AXE, Arrays.asList(ChatColor.GRAY + "Fight in epic fashion"));
    private final ItemStack joinGameIcon = ItemFactory.createItem(name, Material.IRON_AXE, Arrays.asList("Wow amazing lore!"));
    private final MinigamePlugin plugin;

    public MutantMayhemFactory(String name, MinigamePlugin plugin) {
        super(name, 4);
        this.plugin = plugin;
    }

    @Override
    public void createInstance(GameMap map, HashSet<Player> players, MinigamePlugin plugin) {
        try {
            new MutantMayhem(map, players, plugin);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ItemStack getAddGameIcon() {
        return createGameIcon.clone();
    }

    public ItemStack getJoinGameIcon(Lobby lobby) {
        return joinGameIcon.clone();
    }

    @Override
    public Lobby createNewLobby(Player creator) {
        return new Lobby(this, creator, plugin);
    }
}
