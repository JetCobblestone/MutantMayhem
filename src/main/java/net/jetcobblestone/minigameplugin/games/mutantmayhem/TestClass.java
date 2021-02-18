package net.jetcobblestone.minigameplugin.games.mutantmayhem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.jetcobblestone.minigameplugin.assets.classitem.ClassItem;
import net.jetcobblestone.minigameplugin.assets.classitem.itemability.Ability;
import net.jetcobblestone.minigameplugin.assets.classitem.itemability.Action;
import net.jetcobblestone.minigameplugin.assets.classitem.itemability.Check;
import net.jetcobblestone.minigameplugin.assets.customclass.CustomClass;
import net.jetcobblestone.minigameplugin.assets.util.ItemFactory;

public class TestClass {
	private static TestClass instance;
	
	public static TestClass getInstance() {
		if (instance == null) {
			instance = new TestClass();
		}
		return instance;
	}
	
	public void createKit() {
		final List<String> lore = new ArrayList<>();
		lore.add(ChatColor.RESET + "" + ChatColor.GREEN + "This is a test kit!");
		
		final ItemStack icon = ItemFactory.createItem(ChatColor.RED + "Tester", Material.GOLDEN_APPLE, lore);
		final CustomClass testClass = new CustomClass(ChatColor.RED + "Tester", icon, 10d);
		
		final ItemStack item = ItemFactory.createItem(ChatColor.RED + "Test item", Material.STICK, null);
		final ClassItem testClassItem = new ClassItem(item);
		final Ability testRightClick = new Ability("Test right click", 3000, PlayerInteractEvent.class, new TestRightClick(), new rightClickCheck());
		final Ability testLeftClick = new Ability("Test left click", 5000, PlayerInteractEvent.class, new TestLeftClick(), new leftClickCheck());
		testClassItem.add(testRightClick);
		testClassItem.add(testLeftClick);
		
		testClass.addItem(0, testClassItem);
	}

	
	private class TestRightClick implements Action{
		public boolean run(Event e) {
			PlayerInteractEvent event = (PlayerInteractEvent) e;
			event.getPlayer().sendMessage("You right clicked the item");
			return true;
		}
	}
	private class TestLeftClick implements Action{
		public boolean run(Event e) {
			PlayerInteractEvent event = (PlayerInteractEvent) e;
			event.getPlayer().sendMessage("You left clicked the item");
			return true;
		}
	}
	
	private class rightClickCheck implements Check {
		@Override
		public boolean doCheck(Event event) {
			if (!(event instanceof PlayerInteractEvent)) return false;
			PlayerInteractEvent interactEvent = (PlayerInteractEvent) event;
			if (interactEvent.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_AIR) return false;
			return true;
		}
	}
	
	private class leftClickCheck implements Check {
		@Override
		public boolean doCheck(Event event) {
			if (!(event instanceof PlayerInteractEvent)) return false;
			PlayerInteractEvent interactEvent = (PlayerInteractEvent) event;
			if (interactEvent.getAction() != org.bukkit.event.block.Action.LEFT_CLICK_AIR) return false;
			return true;
		}
	}
}
