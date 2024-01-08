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

        // booksaw - BetterTeams - https://www.spigotmc.org/resources/better-teams.17129/
        Plugin betterTeamsPlugin = pluginManager.getPlugin("BetterTeams");
        if (betterTeamsPlugin != null && betterTeamsPlugin.isEnabled()) {
            return new BetterTeamsHook(plugin, betterTeamsPlugin);
        }

        // RoinujNosde - SimpleClans - https://www.spigotmc.org/resources/simpleclans.71242/
        Plugin simpleClansPlugin = pluginManager.getPlugin("SimpleClans");
        if (simpleClansPlugin != null && simpleClansPlugin.isEnabled()) {
            return new SimpleClansHook(plugin, simpleClansPlugin);
        }

        // SaberLLC - Factions (SaberFactions) - https://www.spigotmc.org/resources/simpleclans.71242/
        Plugin saberFactionsPlugin = pluginManager.getPlugin("Factions");
        if (saberFactionsPlugin != null &&
                saberFactionsPlugin.getDescription().getMain().equals("com.massivecraft.factions.FactionsPlugin") &&
                saberFactionsPlugin.isEnabled()) {
            return new SaberFactionsHook(plugin, saberFactionsPlugin);
        }

        // LlmDl - Towny - https://www.spigotmc.org/resources/towny-advanced.72694/
        Plugin townyPlugin = pluginManager.getPlugin("Towny");
        if (townyPlugin != null && townyPlugin.isEnabled()) {
            return new TownyHook(plugin, simpleClansPlugin);
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

    public String[] getAuthors() {
        List<String> authors = this.teamPlugin.getDescription().getAuthors();
        if (authors.isEmpty()) return new String[0];
        return authors.toArray(new String[0]);
    }

    public abstract boolean isFriendly(Player player1, Player player2);

}
