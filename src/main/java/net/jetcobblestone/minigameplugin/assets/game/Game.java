package net.jetcobblestone.minigameplugin.assets.game;


import net.jetcobblestone.minigameplugin.assets.game.map.GameMap;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public abstract class Game {

    //Main class
    protected final GameMap gameMap;
    protected List<GamePlayer> gamePlayerList = new ArrayList<>();


    public Game(GameMap gameMap) {
        this.gameMap = gameMap;

    }

    public void forceStop() {
        for (GamePlayer gamePlayer : gamePlayerList) {
            gamePlayer.getPlayer().teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        }
    }

    public void setAll(GamePlayer.Setting setting, boolean bool) {
        for (GamePlayer gamePlayer : gamePlayerList) {
            gamePlayer.setAllowed(setting, bool);
        }
    }

}
