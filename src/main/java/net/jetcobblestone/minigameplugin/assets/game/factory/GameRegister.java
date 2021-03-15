package net.jetcobblestone.minigameplugin.assets.game.factory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.jetcobblestone.minigameplugin.assets.game.Game;
import org.bukkit.Bukkit;

public class GameRegister {

    private final BiMap<Class<? extends Game>, GameFactory<? extends Game>> gameRegister = HashBiMap.create();

    public void registerGame(Class<? extends Game> gameClass, GameFactory<? extends Game> factory) {
        gameRegister.put(gameClass, factory);
        Bukkit.getPluginManager().callEvent(new GameRegisterEvent(gameClass));
    }

    public GameFactory<? extends Game> getFactory(Class<? extends Game> gameClass) {
        return gameRegister.get(gameClass);
    }

    public Class<? extends Game> getFactoryClass(GameFactory<? extends Game> factory) {
        return gameRegister.inverse().get(factory);
    }
}
