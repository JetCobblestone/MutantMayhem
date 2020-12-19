package net.jetcobblestone.mutantmayhem.assets.customclass;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.jetcobblestone.mutantmayhem.assets.gui.GUI;
import net.jetcobblestone.mutantmayhem.assets.gui.GuiItem;

public class ClassManager {
	private static ClassManager instance;
	private ClassManager() {}
	
	public static ClassManager getInstance() {
		if (instance == null) {
			instance = new ClassManager();
		}
		return instance;
	}
	
	
	private final GUI kitGui = new GUI(ChatColor.GOLD + "Kit selector", 3);
	private final Map<Player, CustomClass> kits = new HashMap<>();
	
	public void addKit(CustomClass kit) {
		final ItemStack icon = kit.getIcon();
		final GuiItem guiIcon = new GuiItem(icon, event ->  {
			Player player = (Player) event.getWhoClicked();
			kits.put(player, kit);
			player.sendMessage(ChatColor.GOLD + "You selected: " + kit.getName());
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
			
			//Testing
			kit.equip(player);
		});
		
		kitGui.addItem(guiIcon);
	}
	
	public GUI getGui() {
		return kitGui;
	}
}
