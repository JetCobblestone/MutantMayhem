package net.jetcobblestone.minigameplugin;

import net.jetcobblestone.minigameplugin.assets.customclass.classitem.AbilityListener;
import net.jetcobblestone.minigameplugin.assets.game.map.MapManager;
import net.jetcobblestone.minigameplugin.assets.gui.GuiListener;
import net.jetcobblestone.minigameplugin.commands.StartGame;
import net.jetcobblestone.minigameplugin.commands.StopGame;
import net.jetcobblestone.minigameplugin.assets.game.SettingsListener;
import net.jetcobblestone.minigameplugin.commands.TestCommand;
import net.jetcobblestone.minigameplugin.commands.Warp;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.commands.KitSelector;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.game.kits.Pirate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class MinigamePlugin extends JavaPlugin {
	private static MinigamePlugin instance;
	public static MinigamePlugin getInstance() {
		return instance;
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onEnable(){
		instance = this;
		//Reads maps
		if (!MapManager.getInstance().readMaps()){
			Bukkit.getLogger().severe("Maps could not be read, shutting down");
		}

		getCommand("TestCommand").setExecutor(new TestCommand());
		getCommand("KitSelector").setExecutor(new KitSelector());
		getCommand("StopGame").setExecutor(new StopGame());
		getCommand("StartGame").setExecutor(new StartGame());
		getCommand("Warp").setExecutor(new Warp());

		getServer().getPluginManager().registerEvents(new AbilityListener(), this);
		getServer().getPluginManager().registerEvents(new GuiListener(), this);
		getServer().getPluginManager().registerEvents(new SettingsListener(), this);

		Pirate.createKit();
	}
}
