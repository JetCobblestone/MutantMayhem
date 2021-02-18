package net.jetcobblestone.minigameplugin.assets.gui;

import java.util.Arrays;
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
	
	
	private Inventory inventory;
	private ClickEvent guiClickEvent;
	private final Map<Integer, GuiItem> overlay;
	private final int rows;
	private final String name;
	private final GuiManager guiManager = GuiManager.getInstance();
	
	
	public void runGuiAction(InventoryClickEvent event) {
		if (guiClickEvent == null) return;
		guiClickEvent.run(event);
	}
	
	public void clickItem(int slot, InventoryClickEvent event) {
		final GuiItem guiItem = overlay.get(slot);
		if (guiItem == null) return;
		guiItem.click(event);
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
	
	public void addItem(GuiItem guiItem) {
		if (overlay.size() == 0) {
			setItem(0, guiItem);
			return;
		}
		
		final Integer[] slots = overlay.keySet().toArray(new Integer[overlay.size()]);
		Arrays.sort(slots);

		for (int i = 0; i < slots.length; i++) {
			//avoiding out of bounds
			if (i == 0) {
				
				if (i == slots.length - 1) {
					if (slots[i] < rows * 9) {
						setItem(slots[i] + 1, guiItem);
					}
				}
				
				//if slot 0 is free
				if (slots[i] > 0) {
					setItem(0, guiItem);
					return;
				}
				continue;
			}
			
			//Checks if the difference between the current term and previous term is greater than 1
			if (slots[i] > slots[i-1] + 1) {
				setItem(slots[i-1] + 1, guiItem);
				return;
			}
		
		}
		
		Bukkit.getLogger().warning("Item could not be added to GUI " + name);
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
