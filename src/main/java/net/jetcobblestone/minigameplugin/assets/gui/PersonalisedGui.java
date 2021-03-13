package net.jetcobblestone.minigameplugin.assets.gui;


import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PersonalisedGui {

    private final Map<Player, Gui> guiMap = new HashMap<>();
    private final Gui defaultGui;

    public PersonalisedGui(String name, int rows, GuiManager guiManager) {
        defaultGui = new Gui(name, rows, guiManager);
    }

    public Gui getGui(Player player) {
        final Gui gui = guiMap.get(player);
        if (gui == null) {
            return defaultGui;
        }
        return gui;
    }

    public void open(Player player) {
        getGui(player).open(player);
    }

    public void setItemAll(int slot, GuiItem guiItem) {
        for (Map.Entry<Player, Gui> entry : guiMap.entrySet()) {
            entry.getValue().setItem(slot, guiItem);
        }
        defaultGui.setItem(slot, guiItem);
    }

    public void addItemAll(GuiItem guiItem) {
        for (Map.Entry<Player, Gui> entry : guiMap.entrySet()) {
            entry.getValue().addItem(guiItem);
        }
        defaultGui.addItem(guiItem);
    }

    public void setSpecific(int slot, GuiItem guiItem, Player player) {
        Gui gui = guiMap.get(player);
        if (gui == null) {
            gui = defaultGui.clone();
            guiMap.put(player, gui);
        }
        gui.setItem(slot, guiItem);
    }

    public void addSpecific(GuiItem guiItem, Player player) {
        Gui gui = guiMap.get(player);
        if (gui == null) {
            gui = defaultGui.clone();
            guiMap.put(player, gui);
        }
        getGui(player).addItem(guiItem);
    }

    public void addExcept(GuiItem guiItem, Player player) {
        for (Map.Entry<Player, Gui> entry : guiMap.entrySet()) {
            if (player.equals(entry.getKey())) continue;
            entry.getValue().addItem(guiItem);
        }
        defaultGui.addItem(guiItem);
    }
}
