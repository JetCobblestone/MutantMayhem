package net.jetcobblestone.minigameplugin.assets.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiItem {
	private final ItemStack item;
	private final ClickEvent clickEvent;
	
	public GuiItem(ItemStack item, ClickEvent clickEvent) {
		final ItemMeta meta = item.getItemMeta();
		meta.setUnbreakable(true);
		item.setItemMeta(meta);
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
