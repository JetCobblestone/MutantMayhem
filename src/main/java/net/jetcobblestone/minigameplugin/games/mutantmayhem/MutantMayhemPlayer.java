package net.jetcobblestone.minigameplugin.games.mutantmayhem;

import net.jetcobblestone.minigameplugin.assets.game.GamePlayer;
import net.jetcobblestone.minigameplugin.assets.game.PlayerManager;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kits.AbstractKit;
import net.jetcobblestone.minigameplugin.assets.game.Game;
import org.bukkit.entity.Player;

public class MutantMayhemPlayer extends GamePlayer {

    private AbstractKit kit;

    public MutantMayhemPlayer(Player player, Game game, PlayerManager playerManager) {
        super(player, game, playerManager);
    }

}
