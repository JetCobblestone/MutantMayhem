package net.jetcobblestone.minigameplugin.games.mutantmayhem;

import net.jetcobblestone.minigameplugin.MinigamePlugin;
import net.jetcobblestone.minigameplugin.assets.customdamage.DamageManager;
import net.jetcobblestone.minigameplugin.assets.customdamage.DamageableEntity;
import net.jetcobblestone.minigameplugin.assets.customdamage.DamageablePlayer;
import net.jetcobblestone.minigameplugin.assets.events.CustomDeathEvent;
import net.jetcobblestone.minigameplugin.assets.events.OnTickEvent;
import net.jetcobblestone.minigameplugin.assets.game.Game;
import net.jetcobblestone.minigameplugin.assets.game.player.GamePlayer;
import net.jetcobblestone.minigameplugin.assets.game.player.PlayerManager;
import net.jetcobblestone.minigameplugin.assets.game.map.GameMap;
import net.jetcobblestone.minigameplugin.assets.util.Cooldown;
import net.jetcobblestone.minigameplugin.assets.util.Pair;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kitsystem.classitem.ClassItem;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kitsystem.classitem.ItemManager;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kitsystem.classitem.itemability.Ability;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.scoreboard.*;
import org.decimal4j.util.DoubleRounder;

import java.io.IOException;
import java.util.*;

public class MutantMayhem extends Game implements Listener {

    private final World world;
    private final List<Location> spawnpoints;
    private final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    private final DamageManager damageManager;
    private final PlayerManager playerManager;
    private GameState gameState;


    public enum GameState {
        COUNTDOWN,
        FIGHTING,
    }

    public MutantMayhem(GameMap map, HashSet<Player> players, MinigamePlugin plugin) throws IOException {
        super(map);

        //Make map copy
        world = map.getWorldCopy();
        spawnpoints = gameMap.getSpawnPoints();

        //Register listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        playerManager = plugin.getPlayerManager();
        damageManager = plugin.getDamageManager();

        //Adding people to list
        for (Player player : players) {
            final DamageablePlayer damageablePlayer = new DamageablePlayer(player, 20d, 1d, 1d, damageManager);
            gamePlayerList.add(new MutantMayhemPlayer(damageablePlayer, this, playerManager));
        }

        //Making sure there are fewer people than spawnpoints
        if (spawnpoints.size() < gamePlayerList.size()) {
            forceStop();
            Bukkit.broadcastMessage(ChatColor.RED + "Game of MutantMayhem couldn't be started as there are fewer spawnpoints than there are people");
            return;
        }

        //Teleportation to starting positions
        List<Location> clone = new ArrayList<>(spawnpoints);
        for (GamePlayer gamePlayer : gamePlayerList) {
            final int rand = new Random().nextInt(clone.size());
            final Location randSpawn = clone.get(rand);
            final Player player = gamePlayer.getPlayer();

            randSpawn.setWorld(world);
            player.teleport(randSpawn);
            clone.remove(rand);

        }


        //Permissions
        setAll(GamePlayer.Setting.MINE, false);
        setAll(GamePlayer.Setting.MOVE, false);
        setAll(GamePlayer.Setting.ABILITY, false);
        setAll(GamePlayer.Setting.FALLDAMAGE, false);
        gameState = GameState.COUNTDOWN;
        updateScoreboard();
        time = System.currentTimeMillis();
    }

    //===UTIL===
    @Override
    public void addPlayer(Player player) {
        gamePlayerList.add(
                new MutantMayhemPlayer(
                        new DamageablePlayer(
                                player,
                                100,
                                2,
                                10,
                                damageManager),
                        this, playerManager)
        );
    }

    //===MAIN SEQUENCE===

    //Countdown timer
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


    //===ABILITY BAR===
    @EventHandler
    private void abilityCooldownBar(OnTickEvent event) {
        for (GamePlayer gamePlayer : gamePlayerList) {

            final Player player = gamePlayer.getPlayer();
            final MutantMayhemPlayer mutantMayhemPlayer = (MutantMayhemPlayer) gamePlayer;
            final Map<ClassItem, Pair<Ability, Boolean>> abilityCooldownCache = mutantMayhemPlayer.getAbilityCooldownCache();
            final ClassItem classItem = ItemManager.classItemFromStack(player.getInventory().getItemInMainHand());


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
                    Pair<Ability, Boolean> pair = abilityCooldownCache.get(classItem);
                    if (pair == null || pair.getLeft() != ability1) {
                        abilityCooldownCache.put(classItem, new Pair<>(ability1, false));
                    }
                    else {
                        if (pair.getRight()) {
                            abilityCooldownCache.get(classItem).setRight(false);
                        }
                    }
                }
            }

            if (ability == null) {
                final Pair<Ability, Boolean> pair = abilityCooldownCache.get(classItem);
                Ability ability1;
                if (pair == null) {
                    ability1 = abilities.get(0);
                    if (ability1 == null) {
                        return;
                    }
                }
                else {
                    if (!pair.getRight()) {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.8f);
                        pair.setRight(true);
                    }
                    ability1 = pair.getLeft();
                }
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ability1.getName() + " is ready"));
                return;
            }


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

    @EventHandler
    private void cooldownSoundFix(PlayerItemHeldEvent event) {
        final Player player = event.getPlayer();
        final GamePlayer gamePlayer = playerManager.getGameFromPlayer(player);
        if (gamePlayer == null) return;
        if (!(gamePlayerList.contains(gamePlayer))) {
            return;
        }

        final MutantMayhemPlayer mutantMayhemPlayer = (MutantMayhemPlayer) gamePlayer;
        final Map<ClassItem, Pair<Ability, Boolean>> abilityCooldownCache = mutantMayhemPlayer.getAbilityCooldownCache();
        final ClassItem classItem = ItemManager.classItemFromStack(player.getInventory().getItemInMainHand());

        if (classItem == null) return;

        final Pair<Ability, Boolean> pair = abilityCooldownCache.get(classItem);

        if (pair == null) return;
        pair.setRight(true);
    }


    //===SCOREBOARD SYSTEM===

    //update method
    private void updateScoreboard() {
        assert scoreboardManager != null;
        final Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        final Objective objective = scoreboard.registerNewObjective("MutantMayhem", "Dummy", ChatColor.RED + "MUTANT MAYHEM");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (int i = 0; i < gamePlayerList.size(); i++) {
            final GamePlayer gamePlayer = gamePlayerList.get(i);
            final MutantMayhemPlayer mutantMayhemPlayer = (MutantMayhemPlayer) gamePlayer;
            final Player player = gamePlayer.getPlayer();
            final Score score = objective.getScore(player.getName() + ": " + mutantMayhemPlayer.getKills());

            score.setScore(i);
            player.setScoreboard(scoreboard);
        }
    }

    //on kill
    @EventHandler
    private void countdownTick(PlayerDeathEvent event) {
        Player player = event.getEntity();
        GamePlayer gamePlayer = playerManager.getGameFromPlayer(player);
        if (gamePlayer == null) {
            return;
        }
        if (!(gamePlayerList.contains(gamePlayer))) {
            return;
        }

        Player killer = player.getKiller();
        if (killer != null) {
            MutantMayhemPlayer mutantMayhemPlayer = (MutantMayhemPlayer) playerManager.getGameFromPlayer(killer);
            mutantMayhemPlayer.makeKill();
            updateScoreboard();
        }
    }


    // ===RESPAWN SYSTEM===

    //killed
    @EventHandler(priority = EventPriority.LOW)
    private void onDeath(CustomDeathEvent event) {
        final DamageableEntity damageableEntity = event.getDamageableEntity();
        final LivingEntity livingEntity = damageableEntity.getLivingEntity();
        if (!(livingEntity instanceof Player)) return;
        final DamageablePlayer damageablePlayer = (DamageablePlayer) damageableEntity;
        final Player player = damageablePlayer.getLivingEntity();
        final GamePlayer gamePlayer = playerManager.getGameFromPlayer(player);
        if (!gamePlayerList.contains(gamePlayer)) return;

        event.setDefaultDeath(false);
        final MutantMayhemPlayer mutantMayhemPlayer = (MutantMayhemPlayer) gamePlayer;
        respawn(mutantMayhemPlayer);
    }

    //fall into void
    @EventHandler
    private void fallIntoVoid(EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        if (!(entity instanceof Player)) return;

        final Player player = (Player) entity;
        final GamePlayer gamePlayer = playerManager.getGameFromPlayer(player);
        if (!gamePlayerList.contains(gamePlayer)) return;

        if (!(event.getCause() == EntityDamageEvent.DamageCause.VOID)) return;

        event.setCancelled(true);
        final Location location = player.getLocation();
        location.setY(100);
        player.teleport(location);
        player.sendMessage(ChatColor.GOLD + "You fell into the void!");
        respawn((MutantMayhemPlayer) gamePlayer);
    }

    //respawn method
    private void respawn(MutantMayhemPlayer mutantMayhemPlayer) {
        final DamageablePlayer damageablePlayer = mutantMayhemPlayer.getDamageablePlayer();
        final Player player = mutantMayhemPlayer.getPlayer();

        respawnCooldownMap.add(new RespawnTimer(mutantMayhemPlayer, 5000));
        player.setGameMode(GameMode.SPECTATOR);
        damageablePlayer.setHealth(damageablePlayer.getMaxHealth());
    }

    //respawn timer
    private final HashSet<RespawnTimer> respawnCooldownMap = new HashSet<>();
    private class RespawnTimer {
        final MutantMayhemPlayer mutantMayhemPlayer;
        final Cooldown cooldown;
        int counter;

        private RespawnTimer(MutantMayhemPlayer mutantMayhemPlayer, long respawnTime) {
            this.mutantMayhemPlayer = mutantMayhemPlayer;
            this.cooldown = new Cooldown(respawnTime);
            counter = (int) respawnTime/1000;
        }

        private void tick() {
            final long remainingTime = cooldown.getRemainingTime();
            final Player player = mutantMayhemPlayer.getPlayer();

            if (cooldown.isFinished()) {
                final int rand = new Random().nextInt(spawnpoints.size());
                final Location randSpawn = spawnpoints.get(rand);

                randSpawn.setWorld(world);
                randSpawn.setDirection(player.getLocation().getDirection());
                player.teleport(randSpawn);
                player.setGameMode(GameMode.SURVIVAL);
                respawnCooldownMap.remove(this);
                player.sendTitle(ChatColor.RED + "GO!", null, 0, 20, 0);
                player.playNote(player.getLocation(), Instrument.PLING, Note.natural(0, Note.Tone.C));
                return;
            }

            if (remainingTime/1000 < counter) {
                mutantMayhemPlayer.getPlayer().sendMessage(ChatColor.GOLD + "You will respawn in " + ChatColor.RED + counter);
                player.playNote(player.getLocation(), Instrument.STICKS, Note.natural(0, Note.Tone.G));
                counter--;
            }
        }
    }

    @EventHandler
    private void respawnTimer(OnTickEvent event) {
        for (RespawnTimer respawnTimer : respawnCooldownMap) {
            respawnTimer.tick();
        }
    }
}
