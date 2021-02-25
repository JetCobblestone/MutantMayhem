package net.jetcobblestone.minigameplugin.games.mutantmayhem.kits;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.jetcobblestone.minigameplugin.games.mutantmayhem.kits.classitem.ClassItem;

public class AbstractKit {
	protected final Map<Integer, ClassItem> itemList = new HashMap<>();
	protected String name;
	protected ItemStack icon;
	protected Double defence;
	
	public void equip(Player player) {
		player.getInventory().clear();
		for (Integer key : itemList.keySet()) {
			player.getInventory().setItem(key, itemList.get(key).getItem());
		}
		
		Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(defence);
	}

	public ItemStack getIcon() {
		return icon.clone();
	}
	
	public String getName() {
		return name;
	}
}