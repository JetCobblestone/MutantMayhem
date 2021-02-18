package net.jetcobblestone.minigameplugin.assets.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.HumanEntity;

public class GuiManager {
	
	private GuiManager() {
		guiLink = new HashMap<>();
	}

	private static GuiManager instance;
	public static GuiManager getInstance() {
		if (instance == null) {
			instance = new GuiManager();
		}
		return instance;
	}
	
	
	private final Map<UUID, GUI> guiLink;
	
	
	protected void addGui(HumanEntity humanEntity, GUI gui) {
		guiLink.put(humanEntity.getUniqueId(), gui);
	}
	
	public GUI getGui(HumanEntity humanEntity) {
		return guiLink.get(humanEntity.getUniqueId());
	}
	
	public void clearPlayer(HumanEntity humanEntity) {
		guiLink.remove(humanEntity.getUniqueId());
	}
	
}
