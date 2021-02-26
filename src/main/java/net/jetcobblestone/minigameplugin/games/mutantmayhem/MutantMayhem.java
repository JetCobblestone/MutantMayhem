package net.jetcobblestone.minigameplugin.games.mutantmayhem;

import net.jetcobblestone.minigameplugin.MinigamePlugin;
import net.jetcobblestone.minigameplugin.assets.events.OnTickEvent;
import net.jetcobblestone.minigameplugin.assets.game.Game;
import net.jetcobblestone.minigameplugin.assets.game.GamePlayer;
import net.jetcobblestone.minigameplugin.assets.game.PlayerManager;
import net.jetcobblestone.minigameplugin.assets.game.map.GameMap;
import net.jetcobblestone.minigameplugin.assets.util.Cooldown;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kits.classitem.ClassItem;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kits.classitem.ItemManager;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kits.classitem.itemability.Ability;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.decimal4j.util.DoubleRounder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MutantMayhem extends Game implements Listener {

    private final World world;
    private final List<Location> spawnpoints;

    private final MinigamePlugin plugin;
    private final PlayerManager playerManager;

    private GameState gameState;

    public enum GameState {
        LOBBY,
        COUNTDOWN,
        FIGHTING,
    }

    public MutantMayhem(GameMap map, MinigamePlugin plugin) throws IOException {
        super(map);

        world = map.getWorldCopy();
        spawnpoints = gameMap.getSpawnPoints();

        this.plugin = plugin;
        playerManager = plugin.getPlayerManager();

        gameState = GameState.LOBBY;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler
    private void lobbyTick(OnTickEvent event) {
        if (gameState != GameState.LOBBY) return;

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
        gameState = GameState.COUNTDOWN;
        time = System.currentTimeMillis();
    }


    long time = 0;
    int counter = 0;
    @EventHandler
    private void countdownTick(OnTickEvent event) {

        if (gameState != GameState.COUNTDOWN) return;
        long secondsDif = (System.currentTimeMillis() - time)/1000;

        if (secondsDif > counter) {

            int display = 10 - counter;

            if (display == 0) {
                for (GamePlayer gamePlayer: gamePlayerList) {
                    Player player = gamePlayer.getPlayer();
                    player.sendTitle(ChatColor.RED + "FIGHT!!", null, 0, 20, 0);
                    player.playNote(player.getLocation(), Instrument.PLING, Note.natural(0, Note.Tone.C));
                }

                setAll(GamePlayer.Setting.MOVE, true);
                setAll(GamePlayer.Setting.ABILITY, true);

                gameState = GameState.FIGHTING;
                return;
            }


            for (GamePlayer gamePlayer : gamePlayerList) {

                Player player = gamePlayer.getPlayer();
                player.sendTitle(ChatColor.GOLD + "" + display, null, 0, 20, 0);

                if (display <= 3) {
                    player.playNote(player.getLocation(), Instrument.PLING, Note.natural(0, Note.Tone.G));
                }
                else {
                    player.playNote(player.getLocation(), Instrument.STICKS, Note.natural(0, Note.Tone.G));
                }
            }
            counter ++;
        }
    }

    @EventHandler
    private void fightingTick(OnTickEvent event) {
        if (gameState != GameState.FIGHTING) return;

        for (GamePlayer gamePlayer : gamePlayerList) {

            Player player = gamePlayer.getPlayer();
            ClassItem classItem = ItemManager.classItemFromStack(player.getInventory().getItemInMainHand());

            if (classItem == null) return;


            List<Ability> abilities = classItem.getAbilities();
            double shortest = Double.POSITIVE_INFINITY;

            Ability ability = null;
            Cooldown cooldown = null;

            for (Ability ability1 : abilities) {
                Cooldown cooldown1 = ability1.getCooldown(player);
                if (cooldown1 == null || cooldown1.isFinished()) {
                    continue;
                }
                long time = cooldown1.getRemainingTime();
                if (time < shortest) {
                    shortest  = (double) time;
                    ability = ability1;
                    cooldown = cooldown1;
                }
            }

            if (ability == null) return;


            final StringBuilder bar = new StringBuilder("███████████████████");
            final DoubleRounder doubleRounder = new DoubleRounder(1);
            final int breakPoint = (int) ((1 - shortest/ ((double) cooldown.getTime())) * bar.length()) + 1;
            final StringBuilder timeDisplay = new StringBuilder(doubleRounder.round(shortest / 1000) + "s");
            final StringBuilder abilityName = new StringBuilder(ability.getName());


            if (abilityName.length() > timeDisplay.length()) {
                int difference = abilityName.length() - timeDisplay.length();
                for (int i = 0; i < difference; i++) {
                    timeDisplay.insert(timeDisplay.length(), " ");
                }
            }
            else {
                int difference = timeDisplay.length() - abilityName.length();
                for (int i = 0; i < difference; i++) {
                    abilityName.insert(0, " ");
                }
            }




            bar.insert(breakPoint, ChatColor.RED + "");
            bar.insert(0, abilityName.toString() + "  -  " +ChatColor.GREEN);
            bar.insert(bar.length(), "  -  " + timeDisplay.toString());

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(bar.toString()));
        }

    }
}
