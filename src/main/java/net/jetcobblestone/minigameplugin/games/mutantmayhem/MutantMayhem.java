package net.jetcobblestone.minigameplugin.games.mutantmayhem;

import net.jetcobblestone.minigameplugin.assets.game.map.GameMap;
import net.jetcobblestone.minigameplugin.assets.game.map.MapManager;
import net.jetcobblestone.minigameplugin.MinigamePlugin;
import net.jetcobblestone.minigameplugin.assets.game.Game;
import net.jetcobblestone.minigameplugin.assets.game.GameManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MutantMayhem extends Game {

    private static final GameManager gameManager = GameManager.getInstance();
    private static final MapManager mapManager = MapManager.getInstance();
    private World world;


    public MutantMayhem(List<Player> players, GameMap map) {

        super(players, map);
        //Making sure there are fewer people than spawnpoints
        List<Location> spawnpoints = map.getSpawnPoints();
        if (spawnpoints.size() < players.size()) {
            gameManager.removeGame(this, players);
            Bukkit.broadcastMessage(ChatColor.RED + "Game of MutantMayhem couldn't be started as there are fewer spawnpoint than there are people");
            return;
        }

        //Gets the copy of the map
        try {
            Bukkit.getLogger().warning("Running the code to copy the map....");
            world = map.getWorldCopy();
        } catch (IOException e) {
            Bukkit.getLogger().warning("Mutant Mayhem failed to start, IO exception");
            forceStop();
            e.printStackTrace();
            return;
        }

        //Teleportation to starting positions
        List<Location> clone = new ArrayList<>(spawnpoints);
        for (Player player : players) {
            int rand = new Random().nextInt(clone.size());
            Location randSpawn = clone.get(rand);
            randSpawn.setWorld(world);
            player.teleport(randSpawn);
            clone.remove(rand);
            Bukkit.getLogger().info("PLAYER " + player.getName() + " HAS BEEN WARPED TO WORLD " + world.getName());
        }

        //Permissions
        setAllowed(Setting.MINE, false);
        setAllowed(Setting.MOVE, false);
        setAllowed(Setting.ABILITY, false);
        setAllowed(Setting.FALLDAMAGE, false);

        //Countdown
        AtomicInteger counter = new AtomicInteger(10);
        new BukkitRunnable() {
            public void run() {
                final int count = counter.get();

                if (count == 0) {
                    cancel();
                    for (Player player : players) {
                        player.sendTitle(ChatColor.RED + "FIGHT!!", null, 0, 20, 0);
                        player.playNote(player.getLocation(), Instrument.PLING, Note.natural(0, Note.Tone.C));
                    }
                    setAllowed(Setting.MOVE, true);
                    setAllowed(Setting.ABILITY, true);
                }

                if (!isCancelled()) {
                    for (Player player : players) {
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
        }.runTaskTimer(MinigamePlugin.getInstance(), 0, 20);

    }


}
