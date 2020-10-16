package net.jetcobblestone.mutantmayhem.assets.classitem;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public class ItemManager {
	private static Map<ItemStack, ClassItem> itemMap = new HashMap<ItemStack, ClassItem>();
	
	public static ClassItem classItemFromStack(ItemStack item) {
		return itemMap.get(item);
	}
	public static void add(ClassItem classItem) {
		itemMap.put(classItem.getItem(), classItem);
	}
	
	public static ItemStack testGet(int i) {
		ClassItem[] array = itemMap.values().toArray(new ClassItem[itemMap.size()]);
		return array[i].getItem();
	}
}
