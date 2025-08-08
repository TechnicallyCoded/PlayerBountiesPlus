package com.tcoded.playerbountiesplus.listener;

import com.tcoded.playerbountiesplus.gui.IPbpGui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Get the inventory holder
        InventoryHolder holder = event.getView().getTopInventory().getHolder();

        // Ensure the holder is a GUI
        if (!(holder instanceof IPbpGui gui)) {
            return;
        }

        // Cancel normal click behaviour
        event.setCancelled(true);

        // Delegate the click handling
        gui.handleClick(event);
    }
}

