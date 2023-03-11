package com.tcoded.playerbountiesplus.hook.clan;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractClanHook {

    public static AbstractClanHook findClanHook(PlayerBountiesPlus plugin) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        Plugin clansLitePlugin = pluginManager.getPlugin("ClansLite");
        if (clansLitePlugin != null && clansLitePlugin.isEnabled()) {
            return new ClansLiteHook(plugin, clansLitePlugin);
        }

        return null;
    }

    @Nullable
    public abstract String getClanId(Player player);

}
