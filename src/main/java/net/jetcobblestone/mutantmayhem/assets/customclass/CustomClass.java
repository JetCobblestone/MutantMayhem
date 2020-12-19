package net.jetcobblestone.mutantmayhem.assets.customclass;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.jetcobblestone.mutantmayhem.assets.classitem.ClassItem;

public class CustomClass {
	private final Map<Integer, ClassItem> itemList = new HashMap<Integer, ClassItem>();
	private final String name;
	private final ItemStack icon;
	private final Double defence;
	
	public CustomClass(String name, ItemStack icon, Double defence) {
		this.name = name;
		this.icon = icon;
		this.defence = defence;
		ClassManager.getInstance().addKit(this);
	}
	
	public void addItem(int slot, ClassItem item) {
		if (itemList.get(slot) != null) {
			Bukkit.getLogger().warning("Slot " + slot + " was overriden in class " + name);
		}
		
		itemList.put(slot, item);
	}
	
	public void equip(Player player) {
		player.getInventory().clear();
		for (Integer key : itemList.keySet()) {
			player.getInventory().setItem(key, itemList.get(key).getItem());
		}
		
		player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(defence);
	}

	public ItemStack getIcon() {
		return icon.clone();
	}
	
	public String getName() {
		return name;
	}
}