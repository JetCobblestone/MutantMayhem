package net.jetcobblestone.minigameplugin.assets.game.player;

import net.jetcobblestone.minigameplugin.assets.game.Game;
import net.jetcobblestone.minigameplugin.assets.gui.Gui;
import net.jetcobblestone.minigameplugin.assets.gui.GuiManager;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.MutantMayhem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerManager {

    // Fields
    private final Map<Player, GamePlayer> playerMap = new HashMap<>();
    private final GuiManager guiManager;

    public PlayerManager(GuiManager guiManager) {
        this.guiManager = guiManager;
    }

    // Game-list management
    protected void addPlayer(Player player, GamePlayer gamePlayer) {
        playerMap.put(player, gamePlayer);
    }

    public void removeGame(Game game, List<Player> players) {
        for (Player player : players) {
            playerMap.remove(player);
        }
    }

    public GamePlayer getGameFromPlayer(Player player) {
        return playerMap.get(player);
    }


    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    private final List<Class<? extends Game>> gameClassList = new ArrayList<>(Arrays.asList(MutantMayhem.class));

    public Gui makeGameMenu() {
        Gui gameMenu = new Gui(ChatColor.GOLD + "Game Menu", 3, guiManager);
        return null;
    }

}
