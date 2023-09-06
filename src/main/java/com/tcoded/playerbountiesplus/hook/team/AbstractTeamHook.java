package com.tcoded.playerbountiesplus.hook.team;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public abstract class AbstractTeamHook {

    protected PlayerBountiesPlus plugin;
    protected JavaPlugin teamPlugin;

    public AbstractTeamHook(PlayerBountiesPlus plugin, Plugin teamPlugin) {
        this.plugin = plugin;
        this.teamPlugin = (JavaPlugin) teamPlugin;
    }

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

    public String getPluginName() {
        return this.teamPlugin.getName();
    }

    public String getAuthor() {
        List<String> authors = this.teamPlugin.getDescription().getAuthors();
        if (authors.isEmpty()) return "N/A";
        return authors.get(0);
    }

    public abstract boolean isFriendly(Player player1, Player player2);

}
