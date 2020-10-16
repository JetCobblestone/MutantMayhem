package net.jetcobblestone.mutantmayhem.assets.gui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GUI {
	
	public GUI(String name, int rows) {
		if (rows > 6 || rows < 0) {
			Bukkit.getLogger().severe("Tried to create a GUI with an invalid row size - must be between 1 and 6.");
			rows = 3;
		}
		this.rows = rows;
		this.name = name;
		
		overlay = new HashMap<>();
		guiClickEvent = null;
		
	}
	
	public GUI(String name, int rows, ClickEvent event) {
		this(name, rows);
		this.guiClickEvent = event;
	}
	
	
	private final Map<Integer, GuiItem> overlay;
	private ClickEvent guiClickEvent;
	private final int rows;
	private final String name;
	private Inventory inventory;
	private final GuiManager guiManager = GuiManager.getInstance();
	
	
	public void runGuiAction(InventoryClickEvent event) {
		guiClickEvent.run(event);
	}
	
	public void clickItem(int slot, InventoryClickEvent event) {
		if (slot < 0)
		overlay.get(slot).click(event);
	}
	
	private Inventory generateInventory() {
		Inventory inventory = Bukkit.createInventory(null, rows*9, name);
		
		inventory.clear();
		for (Integer slot : overlay.keySet()) {
			inventory.setItem(slot, overlay.get(slot).getItem());
		}
		
		return inventory;
	}

	
	public void setItem(int slot, GuiItem guiItem) {
		overlay.put(slot, guiItem);
	}
	
	public GuiItem getItem(int slot) {
		return overlay.get(slot);
	}
	
	
	public void open(Player player) {
		if (inventory == null) {
			inventory = generateInventory();
		}
		
		player.openInventory(inventory);
		guiManager.addGui(player, this);
	}
	
	public void dupeOpen(Player player) {
		player.openInventory(generateInventory());
		guiManager.addGui(player, this);
	}
	

}
