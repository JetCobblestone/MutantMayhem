package net.jetcobblestone.minigameplugin.assets.game;

import net.jetcobblestone.minigameplugin.MinigamePlugin;
import net.jetcobblestone.minigameplugin.assets.game.map.GameMap;
import org.bukkit.inventory.ItemStack;

public abstract class GameFactory<T extends Game> {

    protected final String name;

    public GameFactory(String name) {
        this.name = name;
    }

    public abstract T createInstance(GameMap map, MinigamePlugin plugin);

    public abstract ItemStack getAddGameIcon();

    public abstract ItemStack getJoinGameIcon(Game game);

}
