package net.jetcobblestone.mutantmayhem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.jetcobblestone.mutantmayhem.assets.classitem.AbilityListener;
import net.jetcobblestone.mutantmayhem.assets.gui.GUI;
import net.jetcobblestone.mutantmayhem.assets.gui.GuiListener;


public class MutantMayhem extends JavaPlugin {
	private MutantMayhem instance;
	
	public MutantMayhem getInstance() {
		return instance;
	}
	
	@Override
	public void onEnable(){
		instance = this;
		getCommand("TestCommand").setExecutor(new TestCommand());
		getServer().getPluginManager().registerEvents(new AbilityListener(), this);
		getServer().getPluginManager().registerEvents(new GuiListener(), this);
		TestClass.getInstance().createTestItems();
		
		GUI gui = new GUI(ChatColor.GOLD + "Test Inventory", 3, event -> {
			event.getWhoClicked().sendMessage("You clicked the inventory :DDDD");
		});
		
		Player player = Bukkit.getPlayer("JetCobblestone");
		if (player != null) {
			gui.dupeOpen(player);
		}
	
	}
}
