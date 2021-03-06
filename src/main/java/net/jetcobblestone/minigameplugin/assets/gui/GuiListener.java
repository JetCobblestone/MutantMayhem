package net.jetcobblestone.minigameplugin.assets.gui;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

//This class is the listener responsible for managing the crate creator GUI
public class GuiListener implements Listener{
	
	private final GuiManager guiManager;

	public GuiListener(GuiManager guiManager) {
		this.guiManager = guiManager;
	}
	
	@EventHandler
	public void onGUIClick(InventoryClickEvent event) {
		final Inventory clickedInventory = event.getClickedInventory();
		final HumanEntity humanEntity = event.getWhoClicked();
		final ClickType click = event.getClick();
		final Gui gui = guiManager.getGui(humanEntity);
		
		if (clickedInventory == null) return;
		
		if (click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT || click == ClickType.DOUBLE_CLICK) {
			if (gui != null) {
				event.setCancelled(true);
			}
		}
		
		if (clickedInventory.equals(event.getView().getBottomInventory())) return;

		if (gui != null) {
			event.setCancelled(true);
			
			gui.clickItem(event.getSlot(), event);
			gui.runGuiAction(event);
		}

	}
	
	@EventHandler
	public void onDrag(InventoryDragEvent event) {
		if (guiManager.getGui(event.getWhoClicked()) != null) {
			event.setCancelled(true);
		}
	}
	
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		final HumanEntity humanEntity = event.getPlayer();
		guiManager.getGui(humanEntity).removeViewer(humanEntity);
		guiManager.clearPlayer(humanEntity);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		final HumanEntity humanEntity = event.getPlayer();
		guiManager.getGui(humanEntity).removeViewer(humanEntity);
		guiManager.clearPlayer(humanEntity);
	}
}

