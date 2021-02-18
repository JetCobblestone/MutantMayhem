package net.jetcobblestone.minigameplugin.assets.classitem.itemability;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.decimal4j.util.DoubleRounder;

public class Ability {
	final String name;
	final Action action;
	final long time;
	final Class<? extends Event> eventClass;
	final Map<Player, Cooldown> cooldowns = new HashMap<>();
	Check check;
	
	public Ability(String name, long time, Class<? extends Event> eventClass, Action action) {
		this.name = name;
		this.action = action;
		this.time = time;
		this.eventClass = eventClass;
		this.check = null;
	}
	
	public Ability(String name, long time, Class<? extends Event> eventClass, Action action, Check check) {
		this(name, time, eventClass, action);
		this.check = check;
	}
	
	public void run(Player player, Event event) {
		if (check != null && !check.doCheck(event)) return;
		
		Cooldown cooldown = cooldowns.get(player);

		if (cooldown != null && cooldown.isFinished()) {
			cooldowns.remove(player);
			cooldown = null;
		}
		
		if (cooldown == null) {
			if(action.run(event)) {
				cooldowns.put(player, new Cooldown(time));
			}
			return;
		}

		final double remaining = ((double)(time - cooldown.getTime()))/1000;
		final double rounded = DoubleRounder.round(remaining, 1);
		player.sendMessage(name + " is on cooldown for " + rounded + " seconds");
	}
	
	public Class<? extends Event> getEvent() {
		return eventClass;
	}
}
