package com.tcoded.playerbountiesplus.hook.placeholder;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public interface PlaceholderHook {

    @NotNull
    static PlaceholderHook findPlaceholderHook(PlayerBountiesPlus plugin) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        Plugin papiPlugin = pluginManager.getPlugin("PlaceholderAPI");
        if (papiPlugin != null) {
            return new PlaceholderAPIHook(plugin, papiPlugin);
        }

        plugin.getLogger().warning("PlaceholderAPI not found, placeholders will be unavailable.");
        return new DummyPlaceholderHook();
    }

    void enable();

    void disable();

}
