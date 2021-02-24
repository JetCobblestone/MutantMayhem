package net.jetcobblestone.minigameplugin.assets.customclass.classitem;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public class ItemManager {
	private static final Map<ItemStack, ClassItem> itemMap = new HashMap<>();
	
	public static ClassItem classItemFromStack(ItemStack item) {
		return itemMap.get(item);
	}
	public static void add(ClassItem classItem) {
		itemMap.put(classItem.getItem(), classItem);
	}
	
	public static ItemStack testGet(int i) {
		ClassItem[] array = itemMap.values().toArray(new ClassItem[0]);
		return array[i].getItem();
	}
}
