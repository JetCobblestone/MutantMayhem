package net.jetcobblestone.minigameplugin.games.mutantmayhem.kits.classitem.itemability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import net.jetcobblestone.minigameplugin.assets.util.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.decimal4j.util.DoubleRounder;

public class Ability {

	final String name;
	final Predicate<Event> action;
	final long time;
	final Map<UUID, Cooldown> cooldowns = new HashMap<>();
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
		UUID uuid = player.getUniqueId();
		Cooldown cooldown = cooldowns.get(uuid);

		if (cooldown != null && cooldown.isFinished()) {
			cooldowns.remove(uuid);
			cooldown = null;
		}

		if (cooldown == null) {
			if(action.test(event)) {
				cooldowns.put(uuid, new Cooldown(time));
			}
			return;
		}

		final double remaining = (double) cooldown.getRemainingTime()/1000;
		final double rounded = DoubleRounder.round(remaining, 1);
		player.sendMessage(name + " is on cooldown for " + rounded + " seconds");
	}

	public Cooldown getCooldown(Player player) {
		return cooldowns.get(player.getUniqueId());
	}

	public Class<? extends Event> getEvent() {
		return eventClass;
	}

	public String getName() {
		return name;
	}
}