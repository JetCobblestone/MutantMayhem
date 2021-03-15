package net.jetcobblestone.minigameplugin.assets.game;

import com.google.common.collect.ImmutableSet;
import net.jetcobblestone.minigameplugin.MinigamePlugin;
import net.jetcobblestone.minigameplugin.assets.game.factory.GameFactory;
import net.jetcobblestone.minigameplugin.assets.game.factory.GameRegister;
import net.jetcobblestone.minigameplugin.assets.game.factory.GameRegisterEvent;
import net.jetcobblestone.minigameplugin.assets.gui.Gui;
import net.jetcobblestone.minigameplugin.assets.gui.GuiItem;
import net.jetcobblestone.minigameplugin.assets.gui.GuiManager;
import net.jetcobblestone.minigameplugin.assets.gui.PersonalisedGui;
import net.jetcobblestone.minigameplugin.assets.util.ItemFactory;
import net.jetcobblestone.minigameplugin.assets.util.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GameManager implements Listener {

    private final GameRegister gameRegister;

    private final HashMap<Lobby, Pair<GuiItem, GuiItem>> lobbyMap = new HashMap<>();

    private final Gui gameMenu;
    private final Gui newGameMenu;
    private final PersonalisedGui pendingGames;

    //=======================================================================================================

    public GameManager(MinigamePlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        GuiManager guiManager = plugin.getGuiManager();
        this.gameRegister = plugin.getGameRegister();
        this.gameMenu = new Gui(ChatColor.GOLD + "Game Menu", 1, guiManager);
        this.newGameMenu = new Gui(ChatColor.GOLD + "Create new game", 3, guiManager);
        this.pendingGames = new PersonalisedGui(ChatColor.GOLD + "View games", 3, guiManager);


        final ItemStack newGameStack = ItemFactory.createItem(
                ChatColor.GREEN + "Create game",
                Material.LIME_WOOL,
                Collections.singletonList(ChatColor.GRAY + "Click to create a game"));

        final GuiItem newGame = new GuiItem(newGameStack, event -> {
           final Player player = (Player) event.getWhoClicked();
           newGameMenu.dupeOpen(player);
        });
        gameMenu.addItem(newGame);

        final ItemStack viewGameStack = ItemFactory.createItem(
                ChatColor.GREEN + "View games",
                Material.ORANGE_WOOL,
                Collections.singletonList(ChatColor.GRAY + "Click to create a game"));

        final GuiItem viewGame = new GuiItem(viewGameStack, event -> {
            final Player player = (Player) event.getWhoClicked();
            pendingGames.open(player);
        });
        gameMenu.addItem(viewGame);
    }

    //=======================================================================================================

    @EventHandler
    public void addGame(GameRegisterEvent registerEvent) {
        final GameFactory<? extends Game> factory = gameRegister.getFactory(registerEvent.getGameClass());

        final GuiItem gameIcon = new GuiItem(factory.getAddGameIcon(), event -> {
            final Player player = (Player) event.getWhoClicked();
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            player.sendMessage("You started a game of " + factory.getName());
            createLobby(factory, player);
        });

        newGameMenu.addItem(gameIcon);
    }

    public Gui getGameMenu() {
        return gameMenu;
    }

    private void createLobby(GameFactory<? extends Game> factory, Player creator) {
        final Lobby lobby = factory.createNewLobby(creator);
        final ItemStack icon = factory.getJoinGameIcon(lobby);

        //General Display
        GuiItem display = new GuiItem(icon, event -> {
            final Player clicker = (Player) event.getWhoClicked();
            if (lobby.hasPlayer(clicker)) {
                clicker.playSound(clicker.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                clicker.sendMessage(ChatColor.RED + "You are already in this game");
            }
            else {
                lobby.addPlayer(clicker);
                clicker.playSound(clicker.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                clicker.sendMessage(ChatColor.GOLD + "You joined " + lobby.getCreator().getName() + "'s game");
            }
        });

        //Creator specific display
        final List<String> specificLore = new ArrayList<>(Objects.requireNonNull(Objects.requireNonNull(icon.getItemMeta()).getLore()));
        specificLore.addAll(Collections.singletonList(
                ChatColor.GOLD + "This is your game! Click to force start!."
        ));

        final ItemStack creatorSpecificStack = ItemFactory.setItemMeta(icon, specificLore);
        final GuiItem creatorSpecific = new GuiItem(creatorSpecificStack, event -> {
            lobby.start();
        });

        lobbyMap.put(lobby, new Pair<>(display, creatorSpecific));
        pendingGames.addSpecific(creatorSpecific, creator);
        pendingGames.addItem(display, ImmutableSet.of(creator));
    }

    public void removeLobby(Lobby lobby) {
        final Pair<GuiItem, GuiItem> pair = lobbyMap.get(lobby);
        pendingGames.removeItem(pair.getLeft(), null);
        pendingGames.removeItem(pair.getRight(), null);
    }
}
