package net.jetcobblestone.minigameplugin.assets.util;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

//The purpose of this class it to make it easy to create the ItemStacks needed for GUIs, as well as call the item loading functions from every GUI.

public class ItemFactory {
	
	public static ItemStack createItem(String name, Material material, List<String> lore) {
		
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		
		if (name != null) {
			meta.setDisplayName(name);
		}
		if (lore != null) {
			meta.setLore(lore);
		}
		item.setItemMeta(meta);
		
		return item;
	}
	
}
