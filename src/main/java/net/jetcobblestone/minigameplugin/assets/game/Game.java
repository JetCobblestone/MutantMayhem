package net.jetcobblestone.minigameplugin.assets.game;

import net.jetcobblestone.minigameplugin.assets.game.map.GameMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public abstract class Game {

    //Main class

    private final GameManager manager = GameManager.getInstance();
    private final GameMap gameMap;
    private final Set<Setting> allowed = EnumSet.allOf(Setting.class);
    private final List<Player> playerList;

    public Game(List<Player> players, GameMap gameMap) {
        manager.addGame(this, players);
        this.gameMap = gameMap;
        playerList = players;
    }

    public void finish() {
        manager.removeGame(this, playerList);
    }

    public void forceStop() {
        manager.removeGame(this, playerList);
        for (Player player : playerList) {
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        }
    }

    public void tick() {

    }

    //List of permissions

    public enum Setting {
        MINE,
        PLACE,
        MOVE,
        ABILITY,
        ATTACK,
        FALLDAMAGE
    }



    public boolean isAllowed(Setting setting) {
        return allowed.contains(setting);
    }

    public void setAllowed(Setting setting, boolean bool) {
        if (bool) {
            allowed.add(setting);
            return;
        }
        allowed.remove(setting);
    }
}
