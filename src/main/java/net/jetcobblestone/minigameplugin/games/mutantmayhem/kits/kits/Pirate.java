package net.jetcobblestone.minigameplugin.games.mutantmayhem.kits.kits;

import net.jetcobblestone.minigameplugin.MinigamePlugin;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kits.classitem.ClassItem;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kits.classitem.itemability.Ability;
import net.jetcobblestone.minigameplugin.games.mutantmayhem.kits.AbstractKit;
import net.jetcobblestone.minigameplugin.assets.util.ItemFactory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Pirate extends AbstractKit{

	public Pirate() {
		this.name = ChatColor.DARK_BLUE + "Pirate";
		this.icon = createIcon();
		itemList.put(0, createCutlass());
		itemList.put(1, createRumBottle());
	}

	private ItemStack createIcon() {
		final List<String> iconLore = new ArrayList<>();
		iconLore.add(ChatColor.RESET + "" + ChatColor.WHITE + "Sail the seven seas!");
		return ItemFactory.createItem(ChatColor.DARK_BLUE + "Pirate", Material.PARROT_SPAWN_EGG, iconLore);
	}

	private ClassItem createCutlass() {

		final List<String> cutlassLore = new ArrayList<>();
		cutlassLore.add(ChatColor.RESET + "Right click to pirate leap");

		final ItemStack cutlassStack = ItemFactory.createItem(ChatColor.DARK_BLUE + "Cutlass", Material.IRON_SWORD, cutlassLore);
		final ClassItem cutlass = new ClassItem(cutlassStack);

		final Ability rightClick = new Ability(
				PlayerInteractEvent.class,
				ChatColor.RED + "Pirate Lunge",
				3000L,
				pirateLunge,
				rightClickCheck
		);

		cutlass.add(rightClick);

		return cutlass;
	}
	
	private final Predicate<Event> pirateLunge = e -> {
		if (e.getClass() != PlayerInteractEvent.class) return false;
		PlayerInteractEvent event = (PlayerInteractEvent) e;
		final Player player = event.getPlayer();
		final Vector velocity = player.getVelocity();
		final Vector direction = player.getLocation().getDirection();

		velocity.add(direction);
		player.setVelocity(velocity);
		return true;
	};


	private final Predicate<Event> rightClickCheck = e -> {
		if (e.getClass() != PlayerInteractEvent.class) return false;
		PlayerInteractEvent event = (PlayerInteractEvent) e;
		return event.getAction() == Action.RIGHT_CLICK_AIR;
	};

	private ClassItem createRumBottle() {
		final ClassItem rum = new ClassItem(getRumItemStack());
		final Ability drinkRum = new Ability(
				PlayerItemConsumeEvent.class,
				ChatColor.RED + "Drunk Rage",
				30000,
				rumBottle);
		rum.add(drinkRum);
		return rum;
	}
	
	private static ItemStack getRumItemStack() {
		final List<String> rumLore = new ArrayList<>();
		rumLore.add(ChatColor.RESET + "Drink to get drunk, and strong!");
		return ItemFactory.createItem(ChatColor.DARK_RED + "Rum", Material.POTION, rumLore);
	}

	private final ItemStack emptyBottle = getRumItemStack();
	//emptyBottle.setType(Material.GLASS_BOTTLE);

	private final Predicate<Event> rumBottle = e -> {
		if (e.getClass() != PlayerItemConsumeEvent.class) return false;
		PlayerItemConsumeEvent event = (PlayerItemConsumeEvent) e;

		event.setCancelled(true);
		final Player player = event.getPlayer();
		player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1, 10));
		player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 2, 10));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1, 10));


		player.getInventory().setItemInMainHand(emptyBottle);

		return true;
	};

}
