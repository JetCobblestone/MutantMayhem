package net.jetcobblestone.minigameplugin.games.mutantmayhem;

import net.jetcobblestone.minigameplugin.MinigamePlugin;
import net.jetcobblestone.minigameplugin.assets.events.OnTickEvent;
import net.jetcobblestone.minigameplugin.assets.game.Game;
import net.jetcobblestone.minigameplugin.assets.game.GamePlayer;
import net.jetcobblestone.minigameplugin.assets.game.PlayerManager;
import net.jetcobblestone.minigameplugin.assets.game.map.GameMap;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MutantMayhem extends Game implements Listener {

    private final World world;
    private final List<Location> spawnpoints;

    private final MinigamePlugin plugin;
    private final PlayerManager playerManager;

    private GameState gameState;

    public enum GameState {
        WAITING,
        START_COUNTDOWN,
        IN_COUNTDOWN,
    }

    public MutantMayhem(GameMap map, MinigamePlugin plugin) throws IOException {
        super(map);

        world = map.getWorldCopy();
        spawnpoints = gameMap.getSpawnPoints();

        this.plugin = plugin;
        playerManager = plugin.getPlayerManager();

        gameState = GameState.WAITING;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);



    }

    @EventHandler
    private void tick(OnTickEvent event) {
        if (gameState == GameState.WAITING) {

            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

            //Making sure there are fewer people than spawnpoints
            if (spawnpoints.size() < players.size()) {
                forceStop();
                Bukkit.broadcastMessage(ChatColor.RED + "Game of MutantMayhem couldn't be started as there are fewer spawnpoints than there are people");
                return;
            }

            for (Player player : players) {
                gamePlayerList.add(new MutantMayhemPlayer(player, this, playerManager));
            }
            gameState = GameState.START_COUNTDOWN;
        }

        else if (gameState == GameState.START_COUNTDOWN) {

            //Teleportation to starting positions
            List<Location> clone = new ArrayList<>(spawnpoints);
            for (GamePlayer gamePlayer : gamePlayerList) {
                int rand = new Random().nextInt(clone.size());
                Location randSpawn = clone.get(rand);
                randSpawn.setWorld(world);
                gamePlayer.getPlayer().teleport(randSpawn);
                clone.remove(rand);
            }


            //Permissions
            setAll(GamePlayer.Setting.MINE, false);
            setAll(GamePlayer.Setting.MOVE, false);
            setAll(GamePlayer.Setting.ABILITY, false);
            setAll(GamePlayer.Setting.FALLDAMAGE, false);

            //Countdown
            AtomicInteger counter = new AtomicInteger(10);
            new BukkitRunnable() {
                public void run() {
                    final int count = counter.get();

                    if (count == 0) {
                        cancel();
                        for (GamePlayer gamePlayer: gamePlayerList) {
                            Player player = gamePlayer.getPlayer();
                            player.sendTitle(ChatColor.RED + "FIGHT!!", null, 0, 20, 0);
                            player.playNote(player.getLocation(), Instrument.PLING, Note.natural(0, Note.Tone.C));
                        }
                        setAll(GamePlayer.Setting.MOVE, true);
                        setAll(GamePlayer.Setting.ABILITY, true);
                    }

                    if (!isCancelled()) {
                        for (GamePlayer gamePlayer : gamePlayerList) {
                            Player player = gamePlayer.getPlayer();
                            player.sendTitle(ChatColor.GOLD + "" + counter, null, 0, 20, 0);
                            if (count <= 3) {
                                player.playNote(player.getLocation(), Instrument.PLING, Note.natural(0, Note.Tone.G));
                            }
                            else {
                                player.playNote(player.getLocation(), Instrument.STICKS, Note.natural(0, Note.Tone.G));
                            }
                        }
                    }

                    counter.set(count - 1);

                }
            }.runTaskTimer(plugin, 0, 20);
        }
    }

}
