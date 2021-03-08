package net.jetcobblestone.minigameplugin.games.mutantmayhem.kitsystem.classitem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import net.jetcobblestone.minigameplugin.games.mutantmayhem.kitsystem.classitem.itemability.Ability;

public class ClassItem {
	private final Map<Class<? extends Event>, Ability> abilityMap;
	private final ItemStack item;
	
	public ClassItem(ItemStack item) {
		this.item = item;
		ItemManager.add(this);
		abilityMap = new HashMap<>();
	}
	
	public void trigger(Player player, Event event) {
		final Ability ability = abilityMap.get(event.getClass());
		
		if (ability == null) return;
		if (ability.getEvent() == event.getClass()){
			ability.run(player, event);
		}
	}
	
	public void add(Ability ability) {
		final Class<? extends Event> eventClass = ability.getEvent();
		Ability ability1 = abilityMap.get(eventClass);
		
		if (ability1 != null) {
			Bukkit.getLogger().severe("Attempted ability override for " + item);
		}
		abilityMap.put(eventClass, ability);
	}
	
	public ItemStack getItem() {
		return item.clone();
	}

	public List<Ability> getAbilities() {
		List<Ability> abilities = new ArrayList<>();
		for (Class<? extends Event> eventClass : abilityMap.keySet()) {
			abilities.add(abilityMap.get(eventClass));
		}
		return abilities;
	}
}
