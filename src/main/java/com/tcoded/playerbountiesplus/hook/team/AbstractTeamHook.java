package com.tcoded.playerbountiesplus.hook.team;

import com.tcoded.playerbountiesplus.PlayerBountiesPlus;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public abstract class AbstractTeamHook implements TeamHook {

    protected PlayerBountiesPlus plugin;
    protected JavaPlugin teamPlugin;

    public AbstractTeamHook(PlayerBountiesPlus plugin, Plugin teamPlugin) {
        this.plugin = plugin;
        this.teamPlugin = (JavaPlugin) teamPlugin;
    }

    @Override
    public String getPluginName() {
        return this.teamPlugin.getName();
    }

    @Override
    public String getAuthor() {
        List<String> authors = this.teamPlugin.getDescription().getAuthors();
        if (authors.isEmpty()) return "N/A";
        return authors.get(0);
    }

    @Override
    public String[] getAuthors() {
        List<String> authors = this.teamPlugin.getDescription().getAuthors();
        if (authors.isEmpty()) return new String[0];
        return authors.toArray(new String[0]);
    }

}
