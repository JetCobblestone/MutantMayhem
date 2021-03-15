package net.jetcobblestone.minigameplugin.assets.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class Gui {

	private final GuiManager guiManager;
	private final String name;
	private final int rows;
	private final Map<Integer, GuiItem> overlay = new HashMap<>();;
	private final Consumer<InventoryClickEvent> guiClickEvent;
	private final HashSet<HumanEntity> viewers = new HashSet<>();
	private Inventory inventory = null;

	public Gui(String name, int rows, GuiManager guiManager) {
		this(name, rows, null, guiManager);
	}
	
	public Gui(String name, int rows, Consumer<InventoryClickEvent> event, GuiManager guiManager) {
		this.guiManager = guiManager;

		if (rows > 6 || rows < 0) {
			Bukkit.getLogger().severe("Tried to create a GUI with an invalid row size - must be between 1 and 6.");
			rows = 3;
		}
		this.rows = rows;
		this.name = name;

		guiClickEvent = event;
	}
	
	@SuppressWarnings("MethodDoesntCallSuperMethod")
	public Gui clone() {
		Gui clone = new Gui(name, rows, guiClickEvent, guiManager);
		for (Integer i : overlay.keySet()) {
			clone.setItem(i, overlay.get(i).clone());
		}
		return clone;
	}

	public void runGuiAction(InventoryClickEvent event) {
		if (guiClickEvent == null) return;
		guiClickEvent.accept(event);
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
		refresh();
	}
	
	public void addItem(GuiItem guiItem) {
		if (overlay.size() == 0) {
			setItem(0, guiItem);
			return;
		}
		
		final Integer[] slots = overlay.keySet().toArray(new Integer[0]);
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

	public void setMap(Map<Integer, GuiItem> itemMap) {
		overlay.clear();
		overlay.putAll(itemMap);
		refresh();
	}

	public GuiItem getItem(int slot) {
		return overlay.get(slot);
	}

	public void remove(GuiItem guiItem) {
		boolean check = false;
		final List<Integer> toRemove = new ArrayList<>();

		for (Map.Entry<Integer, GuiItem> entry : overlay.entrySet()) {
			if (guiItem.equals(entry.getValue())) {
				toRemove.add(entry.getKey());
				check = true;
			}
		}

		for (int slot : toRemove) {
			overlay.remove(slot);
		}

		if (check) {
			refresh();
		}
	}

	public void refresh() {
		if (inventory != null) {
			inventory = generateInventory();
			for (HumanEntity humanEntity : viewers) {
				open(humanEntity);
			}
		}
	}

	public void open(HumanEntity humanEntity) {
		if (inventory == null) {
			inventory = generateInventory();
		}
		viewers.add(humanEntity);
		humanEntity.openInventory(inventory);
		guiManager.addGui(humanEntity, this);
	}

	public void dupeOpen(Player player) {
		player.openInventory(generateInventory());
		guiManager.addGui(player, this);
	}
	
	public void removeViewer(HumanEntity humanEntity) {
		viewers.remove(humanEntity);
	}
}
