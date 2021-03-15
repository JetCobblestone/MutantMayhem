package net.jetcobblestone.minigameplugin.assets.game.factory;

import lombok.Getter;
import net.jetcobblestone.minigameplugin.MinigamePlugin;
import net.jetcobblestone.minigameplugin.assets.game.Game;
import net.jetcobblestone.minigameplugin.assets.game.Lobby;
import net.jetcobblestone.minigameplugin.assets.game.map.GameMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

public abstract class GameFactory<T extends Game> {

    @Getter protected final String name;
    @Getter protected final int size;

    public GameFactory(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public abstract void createInstance(GameMap map, HashSet<Player> players, MinigamePlugin plugin);

    public abstract ItemStack getAddGameIcon();

    public abstract ItemStack getJoinGameIcon(Lobby lobby);

    public abstract Lobby createNewLobby(Player creator);

}
