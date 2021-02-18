package net.jetcobblestone.minigameplugin.games.mutantmayhem.game.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.jetcobblestone.minigameplugin.MinigamePlugin;
import net.jetcobblestone.minigameplugin.assets.classitem.ClassItem;
import net.jetcobblestone.minigameplugin.assets.classitem.itemability.Ability;
import net.jetcobblestone.minigameplugin.assets.classitem.itemability.Action;
import net.jetcobblestone.minigameplugin.assets.classitem.itemability.Check;
import net.jetcobblestone.minigameplugin.assets.customclass.CustomClass;
import net.jetcobblestone.minigameplugin.assets.util.ItemFactory;

public final class Pirate {

	public static void createKit() {
		final List<String> iconLore = new ArrayList<>();
		iconLore.add(ChatColor.RESET + "" + ChatColor.WHITE + "Sail the seven seas!");
		final ItemStack icon = ItemFactory.createItem(ChatColor.DARK_BLUE + "Pirate", Material.PARROT_SPAWN_EGG, iconLore);

		final CustomClass pirate = new CustomClass(ChatColor.DARK_BLUE + "Pirate", icon, 12d);
		pirate.addItem(0, createCutless());
		pirate.addItem(1, createRumBottle());
		
	}
	
	private static ClassItem createCutless() {
		final List<String> cutlessLore = new ArrayList<>();
		cutlessLore.add(ChatColor.RESET + "Right click to pirate leap");
		final ItemStack cutlessStack = ItemFactory.createItem(ChatColor.DARK_BLUE + "Cutless", Material.IRON_SWORD, cutlessLore);
		final ClassItem cutless = new ClassItem(cutlessStack);
		final Ability rightClick = new Ability(ChatColor.RED + "Pirate lunge", 3000, PlayerInteractEvent.class, new PirateLunge(), new rightClickCheck());
		cutless.add(rightClick);
		return cutless;
	}
	
	private static class PirateLunge implements Action{

		@Override
		public boolean run(Event e) {
			final PlayerInteractEvent event =  (PlayerInteractEvent) e;
			final Player player = event.getPlayer();
			final Vector velocity = player.getVelocity();
			final Vector direction = player.getLocation().getDirection();
			
			velocity.add(direction);
			player.setVelocity(velocity);
			return true;
		}
	}
	
	private static class rightClickCheck implements Check {
		@Override
		public boolean doCheck(Event event) {
			if (!(event instanceof PlayerInteractEvent)) return false;
			PlayerInteractEvent interactEvent = (PlayerInteractEvent) event;
			return interactEvent.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_AIR;
		}
	}
	
	
	
	private static ClassItem createRumBottle() {
		final ClassItem rum = new ClassItem(getRumItemStack());
		final Ability drinkRum = new Ability(ChatColor.RED + "Drunk Rage", 30000, PlayerItemConsumeEvent.class, new RumBottle());
		rum.add(drinkRum);
		return rum;
	}
	
	private static ItemStack getRumItemStack() {
		final List<String> rumLore = new ArrayList<>();
		rumLore.add(ChatColor.RESET + "Drink to get drunk, and strong!");
		return ItemFactory.createItem(ChatColor.DARK_RED + "Rum", Material.POTION, rumLore);
	}
	
	private static class RumBottle implements Action{
		private static final ItemStack emptyBottle = getRumItemStack();
		static {
			emptyBottle.setType(Material.GLASS_BOTTLE);
		}

		@Override
		public boolean run(Event e) {
			final PlayerItemConsumeEvent event = (PlayerItemConsumeEvent) e;
			event.setCancelled(true);
			final Player player = event.getPlayer();
			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1, 10));
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 2, 10));
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1, 10));
			
			
			player.getInventory().setItemInMainHand(emptyBottle);
			
			new BukkitRunnable() {

				@Override
				public void run() {
					
				}
				//runnable
			}.runTaskTimer(MinigamePlugin.getInstance(), 3000l, 1l);
			
			return true;
		}
	}
}
