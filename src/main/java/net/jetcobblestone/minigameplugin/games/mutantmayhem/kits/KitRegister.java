package net.jetcobblestone.minigameplugin.games.mutantmayhem.kits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jetcobblestone.minigameplugin.assets.gui.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.jetcobblestone.minigameplugin.assets.gui.GUI;
import net.jetcobblestone.minigameplugin.assets.gui.GuiItem;

public class KitRegister {

	private final GuiManager guiManager;

	private final GUI kitGui;
	private final List<AbstractKit> kitList = new ArrayList<>();

	public KitRegister(GuiManager guiManager) {
		this.guiManager = guiManager;
		this.kitGui = new GUI(ChatColor.GOLD + "Kit selector", 3, guiManager);
	}

	public void registerKit(AbstractKit kit) {

		for (AbstractKit kit1 : kitList) {
			if (kit.getClass().equals(kit1.getClass())) {
				Thread.dumpStack();
				Bukkit.getLogger().warning("Kit " + kit + " tried to be registered twice");
				return;
			}
		}

		final ItemStack icon = kit.getIcon();
		final GuiItem guiIcon = new GuiItem(icon, event ->  {
			Player player = (Player) event.getWhoClicked();

			player.sendMessage(ChatColor.GOLD + "You selected: " + kit.getName());
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

		});
		kitGui.addItem(guiIcon);
	}
	
	public GUI getGui() {
		return kitGui;
	}
}
