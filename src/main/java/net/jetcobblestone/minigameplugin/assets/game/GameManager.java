package net.jetcobblestone.minigameplugin.assets.game;

import net.jetcobblestone.minigameplugin.MinigamePlugin;
import net.jetcobblestone.minigameplugin.assets.gui.Gui;
import net.jetcobblestone.minigameplugin.assets.gui.GuiItem;
import net.jetcobblestone.minigameplugin.assets.gui.GuiManager;
import net.jetcobblestone.minigameplugin.assets.gui.PersonalisedGui;
import net.jetcobblestone.minigameplugin.assets.util.ItemFactory;
import net.jetcobblestone.minigameplugin.assets.util.Pair;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.MutantMayhem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GameManager {

    private final Set<Class<? extends Game>> gameClassList = new HashSet();
    private Map<Class<? extends Game>, GameFactory<? extends Game>> gameRegister = new HashMap<>();

    private final MinigamePlugin plugin;
    private final GuiManager guiManager;
    private final HashMap<Game, Pair<Player, Pair<GuiItem, GuiItem>>> gameList = new HashMap<>();
    private final Gui gameMenu;
    private final Gui newGameMenu;
    private final PersonalisedGui pendingGames;

    public GameManager(MinigamePlugin plugin) {
        this.plugin = plugin;
        this.guiManager = plugin.getGuiManager();
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

    public void registerGame(Class<? extends Game> gameClass, GameFactory<? extends Game> factory) {
        gameClassList.add(gameClass);
        gameRegister.put(gameClass, factory);
        addGame(gameClass);
    }

    public void addGame(Class<? extends Game> gameClass) {
        final GameFactory<? extends Game> factory = gameRegister.get(gameClass);

        final GuiItem gameIcon = new GuiItem(factory.getAddGameIcon(), event -> {
            final Player player = (Player) event.getWhoClicked();
            createGame(factory, player);
        });

        newGameMenu.addItem(gameIcon);
    }

    public Gui getGameMenu() {
        return gameMenu;
    }

    private void createGame(GameFactory<? extends Game> factory, Player creator) {
        final Game game = factory.createInstance(plugin.getMapManager().getMaps(MutantMayhem.class).get(0), plugin);

        final ItemStack icon = factory.getJoinGameIcon(game);
        GuiItem display = new GuiItem(icon, event -> {
            Player clicker = (Player) event.getWhoClicked();
            game.addPlayer(clicker);
            update(factory, game);
        });

        final List<String> specificLore = new ArrayList<>(Objects.requireNonNull(Objects.requireNonNull(icon.getItemMeta()).getLore()));

        specificLore.addAll(Arrays.asList(
               ChatColor.GOLD + "This is your game! Click to edit."
        ));

        final ItemStack creatorSpecificStack = ItemFactory.setItemMeta(icon, specificLore);
        final GuiItem creatorSpecific = new GuiItem(creatorSpecificStack, event -> {
            creator.sendMessage("Edit menu!");
        });

        gameList.put(game, new Pair<>(creator, new Pair<>(display, creatorSpecific)));
        pendingGames.addExcept(display, creator);
        pendingGames.addSpecific(creatorSpecific, creator);
        game.addPlayer(creator);
    }

    //
    private void update(GameFactory<? extends Game> factory, Game game) {
    }
}
