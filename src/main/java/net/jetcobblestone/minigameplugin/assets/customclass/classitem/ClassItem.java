package net.jetcobblestone.minigameplugin.assets.customclass.classitem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import net.jetcobblestone.minigameplugin.assets.customclass.classitem.itemability.Ability;

public class ClassItem {
	private final Map<Class<? extends Event>, List<Ability>> abilityMap;
	private final ItemStack item;
	
	public ClassItem(ItemStack item) {
		this.item = item;
		ItemManager.add(this);
		abilityMap = new HashMap<>();
	}
	
	public void trigger(Player player, Event event) {
		final List<Ability> abilities = abilityMap.get(event.getClass());
		
		if (abilities == null) return;
		for (Ability ability : abilities) {
			if (ability.getEvent() == event.getClass()){
				ability.run(player, event);
			}
		}
	}
	
	public void add(Ability ability) {
		final Class<? extends Event> eventClass = ability.getEvent();
		List<Ability> abilities = abilityMap.get(eventClass);
		
		if (abilities == null) {
			abilities = new ArrayList<>();
		}
		
		abilities.add(ability);
		abilityMap.put(eventClass, abilities);
	}
	
	public ItemStack getItem() {
		return item.clone();
	}
}
