package com.tcoded.playerbountiesplus.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public interface IPbpGui extends InventoryHolder {

    void open();

    void setContents();

    void handleClick(InventoryClickEvent event);
}

