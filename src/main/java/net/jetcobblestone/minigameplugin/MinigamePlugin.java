package net.jetcobblestone.minigameplugin;

import net.jetcobblestone.minigameplugin.assets.classitem.AbilityListener;
import net.jetcobblestone.minigameplugin.assets.gui.GuiListener;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.commands.KitSelector;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.game.kits.Pirate;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.TestClass;
import org.bukkit.plugin.java.JavaPlugin;


public class MinigamePlugin extends JavaPlugin {
	private static MinigamePlugin instance;
	public static MinigamePlugin getInstance() {
		return instance;
	}

	@Override
	public void onEnable(){
		instance = this;
		getCommand("TestCommand").setExecutor(new TestCommand());
		getCommand("KitSelector").setExecutor(new KitSelector());
		getServer().getPluginManager().registerEvents(new AbilityListener(), this);
		getServer().getPluginManager().registerEvents(new GuiListener(), this);
		getServer().getPluginManager().registerEvents(new PermissionsListener(), this);
		
		TestClass.getInstance().createKit();
		Pirate.createKit();
	}
}
