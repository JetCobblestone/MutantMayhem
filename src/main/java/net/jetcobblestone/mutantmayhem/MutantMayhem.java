package net.jetcobblestone.mutantmayhem;

import org.bukkit.plugin.java.JavaPlugin;

import net.jetcobblestone.mutantmayhem.assets.classitem.AbilityListener;
import net.jetcobblestone.mutantmayhem.assets.gui.GuiListener;
import net.jetcobblestone.mutantmayhem.commands.KitSelector;
import net.jetcobblestone.mutantmayhem.game.kits.Pirate;


public class MutantMayhem extends JavaPlugin {
	private static MutantMayhem instance;
	
	public static MutantMayhem getInstance() {
		return instance;
	}
	
	@Override
	public void onEnable(){
		instance = this;
		getCommand("TestCommand").setExecutor(new TestCommand());
		getCommand("KitSelector").setExecutor(new KitSelector());
		getServer().getPluginManager().registerEvents(new AbilityListener(), this);
		getServer().getPluginManager().registerEvents(new GuiListener(), this);
		
		TestClass.getInstance().createKit();
		Pirate.createKit();
	}
}
