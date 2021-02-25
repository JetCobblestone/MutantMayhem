package net.jetcobblestone.minigameplugin.assets.game;

import net.jetcobblestone.minigameplugin.assets.game.Game;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.Set;

public abstract class GamePlayer {

    private final Player player;
    private final Game game;
    private final Set<Setting> allowed = EnumSet.allOf(Setting.class);

    public GamePlayer (Player player, Game game, PlayerManager playerManager){
        this.player = player;
        this.game = game;
        playerManager.addPlayer(player, this);
    }

    public Player getPlayer() {
        return player;
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

    public Game getGame() {
        return game;
    }
}
