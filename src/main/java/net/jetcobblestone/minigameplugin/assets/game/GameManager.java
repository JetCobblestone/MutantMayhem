package net.jetcobblestone.minigameplugin.assets.game;

import net.jetcobblestone.minigameplugin.MinigamePlugin;
import net.jetcobblestone.minigameplugin.assets.gui.GUI;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.MutantMayhem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameManager {

    // == Start of Singleton ==

    private static GameManager instance;
    private GameManager(){}
    public static GameManager getInstance() {
        if (instance == null){
            instance = new GameManager();
            instance.startGameTick();
        }
        return instance;
    }

    // == End of Singleton ==//


    // Fields
    private final List<Game> gameList = new ArrayList<>();
    private final Map<Player, Game> playerMap = new HashMap<>();

    // Game-list management
    protected void addGame(Game game, List<Player> playerList) {
        gameList.add(game);

        for (Player player: playerList) {
            playerMap.put(player, game);
        }
    }

    public void removeGame(Game game, List<Player> players) {
        gameList.remove(game);
        for (Player player : players) {
            playerMap.remove(player);
        }
    }

    public Game getGameFromPlayer(Player player) {
        return playerMap.get(player);
    }

    // Global game tick
    private void startGameTick(){
        new BukkitRunnable() {

            @Override
            public void run() {

                for (Game game : gameList){
                    game.tick();
                }

            }
        }.runTaskTimer(MinigamePlugin.getInstance(), 0L, 1L);
    }

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    private List<Class<? extends Game>> gameClassList = new ArrayList<>(Arrays.asList(MutantMayhem.class));

    public GUI makeGameMenu() {
        GUI gameMenu = new GUI(ChatColor.GOLD + "Game Menu", 3);
        return null;
    }

}
