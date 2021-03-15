package net.jetcobblestone.minigameplugin.assets.game;

import lombok.Getter;
import net.jetcobblestone.minigameplugin.MinigamePlugin;
import net.jetcobblestone.minigameplugin.assets.game.factory.GameFactory;
import net.jetcobblestone.minigameplugin.assets.game.factory.GameRegister;
import net.jetcobblestone.minigameplugin.assets.game.map.GameMap;
import net.jetcobblestone.minigameplugin.assets.game.map.MapManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class Lobby {

    private final GameFactory<? extends Game> factory;
    private final HashSet<Player> players = new HashSet<>();
    private final MinigamePlugin plugin;
    private final MapManager mapManager;
    private final GameRegister gameRegister;
    private final GameManager gameManager;
    @Getter private final Player creator;

    public Lobby(GameFactory<? extends Game> factory, Player creator, MinigamePlugin plugin) {
        this.factory = factory;
        this.plugin = plugin;
        this.mapManager = plugin.getMapManager();
        this.gameRegister = plugin.getGameRegister();
        this.gameManager = plugin.getGameManager();
        this.creator = creator;
        addPlayer(creator);
    }

    public void addPlayer(Player player) {
        players.add(player);
        if (players.size() >= factory.getSize()) {
            start();
        }
    }

    public boolean hasPlayer(Player player) {
        return players.contains(player);
    }

    public void start() {
        final GameMap map = mapManager.getMaps(gameRegister.getFactoryClass(factory)).get(0);
        gameManager.removeLobby(this);
        factory.createInstance(map, players, plugin);
    }

}
