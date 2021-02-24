package net.jetcobblestone.minigameplugin.assets.customclass.classitem.itemability;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.decimal4j.util.DoubleRounder;

public class Ability {

	final String name;
	final Predicate<Event> action;
	final long time;
	final Map<Player, Cooldown> cooldowns = new HashMap<>();
	final Class<? extends Event> eventClass;
	final Predicate<Event> check;

	public Ability(Class<? extends Event> eventClass, String name, long time, Predicate<Event> action, Predicate<Event> check) {
		this.name = name;
		this.action = action;
		this.time = time;
		this.eventClass = eventClass;
		this.check = check;
	}
	public Ability(Class<? extends Event> eventClass, String name, long time, Predicate<Event> action) {
		this(eventClass, name, time, action, null);
	}


	public void run(Player player, Event event) {
		if (check != null && !check.test(event)) return;

		Cooldown cooldown = cooldowns.get(player);

		if (cooldown != null && cooldown.isFinished()) {
			cooldowns.remove(player);
			cooldown = null;
		}

		if (cooldown == null) {
			if(action.test(event)) {
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