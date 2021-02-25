package net.jetcobblestone.minigameplugin;

import net.jetcobblestone.minigameplugin.assets.events.OnTickEvent;
import net.jetcobblestone.minigameplugin.assets.game.PlayerManager;
import net.jetcobblestone.minigameplugin.assets.gui.GuiManager;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kits.KitRegister;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kits.classitem.AbilityListener;
import net.jetcobblestone.minigameplugin.assets.game.map.MapManager;
import net.jetcobblestone.minigameplugin.assets.gui.GuiListener;
import net.jetcobblestone.minigameplugin.commands.StartGame;
import net.jetcobblestone.minigameplugin.commands.StopGame;
import net.jetcobblestone.minigameplugin.assets.game.SettingsListener;
import net.jetcobblestone.minigameplugin.commands.TestCommand;
import net.jetcobblestone.minigameplugin.commands.Warp;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.commands.KitSelector;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kits.kits.Pirate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class MinigamePlugin extends JavaPlugin {

	private final GuiManager guiManager = new GuiManager();
	private final MapManager mapManager = new MapManager(this);
	private final PlayerManager playerManager = new PlayerManager(guiManager);
	private final KitRegister kitRegister = new KitRegister(guiManager);


	@SuppressWarnings("ConstantConditions")
	@Override
	public void onEnable(){
		//Reads maps
		if (!mapManager.readMaps()){
			Bukkit.getLogger().severe("Maps could not be read, shutting down");
		}

		getCommand("TestCommand").setExecutor(new TestCommand());
		getCommand("KitSelector").setExecutor(new KitSelector(kitRegister));
		getCommand("StopGame").setExecutor(new StopGame(playerManager));
		getCommand("StartGame").setExecutor(new StartGame(this));
		getCommand("Warp").setExecutor(new Warp());

		getServer().getPluginManager().registerEvents(new AbilityListener(playerManager), this);
		getServer().getPluginManager().registerEvents(new GuiListener(guiManager), this);
		getServer().getPluginManager().registerEvents(new SettingsListener(playerManager), this);

		kitRegister.registerKit(new Pirate());

		tickEventDriver();
	}

	private void tickEventDriver() {
		new BukkitRunnable() {
			@Override
			public void run() {
				OnTickEvent onTickEvent = new OnTickEvent();
				Bukkit.getPluginManager().callEvent(onTickEvent);
			}
		}.runTaskTimer(this, 0, 1);
	}

	public GuiManager getGuiManager() {
		return guiManager;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public MapManager getMapManager() {
		return mapManager;
	}
}
