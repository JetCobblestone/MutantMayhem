package net.jetcobblestone.minigameplugin;

import lombok.Getter;
import net.jetcobblestone.minigameplugin.assets.customdamage.DamageManager;
import net.jetcobblestone.minigameplugin.assets.events.OnTickEvent;
import net.jetcobblestone.minigameplugin.assets.game.GameManager;
import net.jetcobblestone.minigameplugin.assets.game.factory.GameRegister;
import net.jetcobblestone.minigameplugin.assets.game.player.PlayerManager;
import net.jetcobblestone.minigameplugin.assets.gui.GuiManager;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.MutantMayhem;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.MutantMayhemFactory;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kitsystem.KitRegister;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kitsystem.classitem.itemability.AbilityListener;
import net.jetcobblestone.minigameplugin.assets.game.map.MapManager;
import net.jetcobblestone.minigameplugin.assets.gui.GuiListener;
import net.jetcobblestone.minigameplugin.commands.StartGame;
import net.jetcobblestone.minigameplugin.commands.StopGame;
import net.jetcobblestone.minigameplugin.assets.game.player.SettingsListener;
import net.jetcobblestone.minigameplugin.commands.TestCommand;
import net.jetcobblestone.minigameplugin.commands.Warp;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.commands.KitSelector;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kitsystem.kits.Pirate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class MinigamePlugin extends JavaPlugin {

	@Getter private GuiManager guiManager;
	@Getter private MapManager mapManager;
	@Getter private GameRegister gameRegister;
	@Getter private GameManager gameManager;
	@Getter private PlayerManager playerManager;
	@Getter private DamageManager damageManager;
	@Getter private KitRegister kitRegister;


	@Override
	public void onEnable(){
		guiManager = new GuiManager();
		mapManager = new MapManager(this);
		gameRegister = new GameRegister();
		gameManager = new GameManager(this);
		playerManager = new PlayerManager(guiManager);
		damageManager = new DamageManager(this);
		kitRegister = new KitRegister(guiManager);

		//Reads maps
		if (!mapManager.readMaps()){
			Bukkit.getLogger().severe("Maps could not be read, shutting down");
		}

		registerCommands();
		registerEvents();
		registerKits();
		registerGames();

		tickEventDriver();
	}

	private void tickEventDriver() {
		new BukkitRunnable() {
			@Override
			public void run() {
				OnTickEvent onTickEvent = new OnTickEvent();
				Bukkit.getPluginManager().callEvent(onTickEvent);
			}
		}.runTaskTimer(this, 10, 1);
	}

	@SuppressWarnings("ConstantConditions")
	private void registerCommands() {
		getCommand("TestCommand").setExecutor(new TestCommand());
		getCommand("KitSelector").setExecutor(new KitSelector(kitRegister));
		getCommand("StopGame").setExecutor(new StopGame(playerManager));
		getCommand("StartGame").setExecutor(new StartGame(this));
		getCommand("Warp").setExecutor(new Warp());
	}

	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new AbilityListener(playerManager), this);
		getServer().getPluginManager().registerEvents(new GuiListener(guiManager), this);
		getServer().getPluginManager().registerEvents(new SettingsListener(playerManager), this);
	}

	private void registerKits() {
		kitRegister.registerKit(new Pirate());
	}

	private void registerGames() {
		gameRegister.registerGame(MutantMayhem.class, new MutantMayhemFactory(ChatColor.RED + "Mutant Mayhem", this));
	}
}
