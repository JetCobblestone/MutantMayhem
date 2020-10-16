package net.jetcobblestone.mutantmayhem.assets.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiItem {
	private final ItemStack item;
	private final ClickEvent clickEvent;
	
	public GuiItem(ItemStack item, ClickEvent clickEvent) {
		this.item = item;
		this.clickEvent = clickEvent;
	}
	
	public ItemStack getItem() {
		return item.clone();
	}
	
	public void click(InventoryClickEvent event) {
		if (event == null) return;
		clickEvent.run(event);
	}
}
