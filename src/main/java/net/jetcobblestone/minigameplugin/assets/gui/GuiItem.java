package net.jetcobblestone.minigameplugin.assets.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class GuiItem {
	private final ItemStack item;
	private final Consumer<InventoryClickEvent> clickEvent;

	public GuiItem(ItemStack item, Consumer<InventoryClickEvent> clickEvent) {
		this.item = item;
		this.clickEvent = clickEvent;
	}
	
	public ItemStack getItem() {
		return item.clone();
	}
	
	public void click(InventoryClickEvent event) {
		if (event == null) return;
		clickEvent.accept(event);
	}

	@SuppressWarnings("MethodDoesntCallSuperMethod")
	public GuiItem clone() {
		return new GuiItem(item.clone(), clickEvent);
	}
}
