package com.tcoded.playerbountiesplus.hook.team;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public abstract class AbstractTeamHook {

    public static AbstractTeamHook findTeamHook(PlayerBountiesPlus plugin) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        // loving11ish - ClansLite - https://www.spigotmc.org/resources/clanslite-1-19-4-support.97163/
        Plugin clansLitePlugin = pluginManager.getPlugin("ClansLite");
        if (clansLitePlugin != null && clansLitePlugin.isEnabled()) {
            return new ClansLiteHook(plugin, clansLitePlugin);
        }

        // AlessioDP - Parties - https://www.spigotmc.org/resources/parties-an-advanced-parties-manager.3709/
        Plugin partiesPlugin = pluginManager.getPlugin("Parties");
        if (partiesPlugin != null && partiesPlugin.isEnabled()) {
            return new AlessioPartiesHook(plugin, partiesPlugin);
        }

        return null;
    }

    public abstract boolean isFriendly(Player player1, Player player2);

}
